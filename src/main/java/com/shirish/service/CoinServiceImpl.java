package com.shirish.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shirish.modal.Coin;
import com.shirish.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CoinRepository coinRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔥 CACHE
    private String cachedTop50 = null;
    private long top50Time = 0;

    private String cachedTrending = null;
    private long trendingTime = 0;

    private String cachedChart = null;
    private long chartTime = 0;

    private String cachedDetails = null;
    private long detailsTime = 0;

    private static final long CACHE_DURATION = 60000; // 60 sec

    // 🔥 COMMON API CALL WITH RETRY
    private String callApi(String url) throws Exception {

        int retries = 3;

        while (retries-- > 0) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Accept", "application/json");
                headers.set("User-Agent", "Mozilla/5.0");

                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        String.class
                );

                return response.getBody();

            } catch (HttpClientErrorException e) {

                if (e.getStatusCode().value() == 429) {
                    System.out.println("🔥 Rate limit hit, retrying...");
                    Thread.sleep(1500); // wait before retry
                } else {
                    throw new Exception(e.getMessage());
                }
            }
        }

        return null; // after retries failed
    }

    // 🔥 DEFAULT FALLBACK
    private String getDefaultCoins() {
        return """
        [
          {"id":"bitcoin","symbol":"btc","name":"Bitcoin","current_price":78000},
          {"id":"ethereum","symbol":"eth","name":"Ethereum","current_price":2300},
          {"id":"tether","symbol":"usdt","name":"Tether","current_price":1}
        ]
        """;
    }

    @Override
    public List<Coin> getCoins(int page) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;

        String response = callApi(url);
        if (response == null) return List.of();

        return objectMapper.readValue(response, new TypeReference<List<Coin>>() {});
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {

        long now = System.currentTimeMillis();

        if (cachedChart != null && (now - chartTime) < CACHE_DURATION) {
            return cachedChart;
        }

        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;

        String response = callApi(url);

        if (response != null) {
            cachedChart = response;
            chartTime = now;
            return response;
        }

        return cachedChart != null ? cachedChart : "{}";
    }

    @Override
    public String getCoinDetails(String coinId) throws Exception {

        long now = System.currentTimeMillis();

        if (cachedDetails != null && (now - detailsTime) < CACHE_DURATION) {
            return cachedDetails;
        }

        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;

        String response = callApi(url);

        if (response != null) {
            cachedDetails = response;
            detailsTime = now;

            try {
                var jsonNode = objectMapper.readTree(response);
                Coin coin = new Coin();

                coin.setId(jsonNode.get("id").asText());
                coin.setName(jsonNode.get("name").asText());
                coin.setSymbol(jsonNode.get("symbol").asText());
                coin.setImage(jsonNode.get("image").get("large").asText());

                coinRepository.save(coin);
            } catch (Exception ignored) {}

            return response;
        }

        return cachedDetails != null ? cachedDetails : "{}";
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if (optionalCoin.isEmpty()) throw new Exception("coin not found");
        return optionalCoin.get();
    }

    @Override
    public String searchCoin(String keyword) throws Exception {
        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;

        String response = callApi(url);
        return response != null ? response : "{}";
    }

    @Override
    public String getTop50CoinsByMarketCapRank() throws Exception {

        long now = System.currentTimeMillis();

        if (cachedTop50 != null && (now - top50Time) < CACHE_DURATION) {
            return cachedTop50;
        }

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";

        String response = callApi(url);

        if (response != null) {
            cachedTop50 = response;
            top50Time = now;
            return response;
        }

        return cachedTop50 != null ? cachedTop50 : getDefaultCoins();
    }

    @Override
    public String getTreadingCoins() throws Exception {

        long now = System.currentTimeMillis();

        if (cachedTrending != null && (now - trendingTime) < CACHE_DURATION) {
            return cachedTrending;
        }

        String url = "https://api.coingecko.com/api/v3/search/trending";

        String response = callApi(url);

        if (response != null) {
            cachedTrending = response;
            trendingTime = now;
            return response;
        }

        return cachedTrending != null ? cachedTrending : "{}";
    }
}
