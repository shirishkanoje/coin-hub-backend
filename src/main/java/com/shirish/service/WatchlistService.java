package com.shirish.service;

import com.shirish.modal.Coin;
import com.shirish.modal.User;
import com.shirish.modal.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;

    Watchlist createWatchlist(User user);

    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;

    Coin removeItemFromWatchlist(Coin coin, User user) throws Exception;
}

