package com.shirish.controller;

import com.shirish.domain.WalletTransactionType;
import com.shirish.modal.*;
import com.shirish.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    // ✅ Get the logged-in user's wallet
    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    // ✅ Get all transactions for the logged-in user
    @GetMapping("/api/transactions")
    public ResponseEntity<List<Transaction>> getWalletTransactions(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        List<Transaction> transactions = transactionService.getTransactionsByWallet(wallet);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // ✅ Wallet to Wallet Transfer (now logs transaction!)
    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req
    ) throws Exception {
        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);

        Wallet senderWallet = walletService.walletToWalletTransfer(
                senderUser, receiverWallet, req.getAmount()
        );

        // 🟢 Log the transaction correctly
        transactionService.createTransaction(
                senderWallet,
                WalletTransactionType.WALLET_TRANSFER,
                receiverWallet.getId(),
                req.getPurpose(),
                BigDecimal.valueOf(req.getAmount())
        );

        return new ResponseEntity<>(senderWallet, HttpStatus.ACCEPTED);
    }

    // ✅ Pay for an order using wallet
    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order, user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    // ✅ Deposit money into wallet after successful Razorpay payment
    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name = "payment_id") String paymentId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        PaymentOrder order = paymentService.getPaymentOrderById(orderId);

        boolean status = paymentService.ProceedPaymentOrder(order, paymentId);

        if (wallet.getBalance() == null) {
            wallet.setBalance(BigDecimal.ZERO);
        }

        if (status) {
            wallet = walletService.addBalance(wallet, order.getAmount());

            // 🟢 Optional: log deposit transaction
            transactionService.createTransaction(
                    wallet,
                    WalletTransactionType.ADD_MONEY,
                    null,
                    "Wallet Top-up",
                    BigDecimal.valueOf(order.getAmount())
            );
        }

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}
