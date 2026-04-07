package com.shirish.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Coin {

    @Id
    @JsonProperty("id")
    private String Id;

    @JsonProperty("symbol")
    private String Symbol;

    @JsonProperty("name")
    private String Name;

    @JsonProperty("image")
    private String Image;

    @JsonProperty("current_price")
    private Double CurrentPrice;

    @JsonProperty("market_cap")
    private Long MarketCap;

    @JsonProperty("market_cap_rank")
    private Integer MarketCapRank;

    @JsonProperty("fully_diluted_valuation")
    private BigDecimal FullyDilutedValuation;

    @JsonProperty("total_volume")
    private Long TotalVolume;

    @JsonProperty("high_24h")
    private Double High24h;

    @JsonProperty("low_24h")
    private Double Low24h;

    @JsonProperty("price_change_24h")
    private Double PriceChange24h;

    @JsonProperty("price_change_percentage_24h")
    private Double PriceChangePercentage24h;

    @JsonProperty("market_cap_change_24h")
    private Long MarketCapChange24h;

    @JsonProperty("market_cap_change_percentage_24h")
    private Long MarketCapChangePercentage24h;

    @JsonProperty("circulating_supply")
    private BigDecimal CirculatingSupply;

    @JsonProperty("total_supply")
    private Long TotalSupply;

    @JsonProperty("max_supply")
    private BigDecimal MaxSupply;

    @JsonProperty("ath")
    private BigDecimal Ath; // All-Time High

    @JsonProperty("ath_change_percentage")
    private BigDecimal AthChangePercentage;

    @JsonProperty("ath_date")
    private LocalDateTime AthDate;

    @JsonProperty("atl")
    private BigDecimal Atl; // All-Time Low

    @JsonProperty("atl_change_percentage")
    private BigDecimal AtlChangePercentage;

    @JsonProperty("atl_date")
    private LocalDateTime AtlDate;

    @JsonProperty("roi")
    @JsonIgnore
    private String Roi; // ROI can be null

    @JsonProperty("last_updated")
    private LocalDateTime LastUpdated;
}