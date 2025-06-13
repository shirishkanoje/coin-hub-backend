package com.shirish.controller;

import com.shirish.domain.WalletTransactionType;
import com.shirish.modal.*;
import com.shirish.service.TransactionService;
import com.shirish.service.UserService;
import com.shirish.service.WalletService;
import com.shirish.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
    public class WithdrawalController {

        @Autowired
        private WithdrawalService withdrawalService;

        @Autowired
        private WalletService walletService;

        @Autowired
        private UserService userService;

      @Autowired
      private TransactionService transactionService;

        @PostMapping("/api/withdrawal/{amount}")
        public ResponseEntity<?> withdrawalRequest(
                @PathVariable Long amount,
                @RequestHeader("Authorization") String jwt) throws Exception {
            User user = userService.findUserProfileByJwt(jwt);
            Wallet userWallet = walletService.getUserWallet(user);

            Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
            walletService.addBalance(userWallet, -withdrawal.getAmount());

            // To initiate withdrawal service

            Transaction walletTransaction = transactionService.createTransaction(
                    userWallet,
                    WalletTransactionType.WITHDRAWAL, null,
                    "bank account withdrawal",
                    BigDecimal.valueOf(withdrawal.getAmount())
            );
            return new ResponseEntity<>(withdrawal, HttpStatus.OK);

        }

    @PatchMapping("@api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        Withdrawal withdrawal = withdrawalService.proceedWithWithdrawal(id, accept);

        Wallet userWallet = walletService.getUserWallet(user);
        if (!accept) {
            walletService.addBalance(userWallet, withdrawal.getAmount());
        }

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawal = withdrawalService.getUsersWithdrawalHistory(user);

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawal = withdrawalService.getAllWithdrawalRequest();

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }




}


