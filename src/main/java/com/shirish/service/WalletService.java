package com.shirish.service;

import com.shirish.modal.Order;
import com.shirish.modal.User;
import com.shirish.modal.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet, Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender ,Wallet receiverWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
}
