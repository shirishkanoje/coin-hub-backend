package com.shirish.service;

import com.shirish.domain.WalletTransactionType;
import com.shirish.modal.Transaction;
import com.shirish.modal.Wallet;
import com.shirish.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Wallet wallet,
                                         WalletTransactionType transactionType,
                                         Long receiverWalletId,
                                         String purpose,
                                         BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setTransactionType(transactionType);
        transaction.setReceiverWalletId(receiverWalletId);
        transaction.setPurpose(purpose);
        transaction.setAmount(amount);
        transaction.setTransactionTime(LocalDateTime.now());

        // Save and return transaction
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByWallet(Wallet wallet) {
        return transactionRepository.findByWallet(wallet);
    }
}
