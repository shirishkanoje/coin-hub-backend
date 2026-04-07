package com.shirish.modal;

import com.shirish.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private WalletTransactionType transactionType;

    private Long receiverWalletId;

    private String purpose;

    private BigDecimal amount;

    private LocalDateTime transactionTime;
}
