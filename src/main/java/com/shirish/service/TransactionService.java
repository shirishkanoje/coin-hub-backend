package com.shirish.service;

import com.shirish.domain.WalletTransactionType;
import com.shirish.modal.Transaction;
import com.shirish.modal.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Wallet wallet,
                                  WalletTransactionType transactionType,
                                  Long receiverWalletId,
                                  String purpose,
                                  BigDecimal amount);

    List<Transaction> getTransactionsByWallet(Wallet wallet);
}
