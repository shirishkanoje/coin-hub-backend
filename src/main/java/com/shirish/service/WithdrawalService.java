package com.shirish.service;

import com.shirish.modal.User;
import com.shirish.modal.Withdrawal;

import java.util.List;

public interface WithdrawalService {

        Withdrawal requestWithdrawal(Long amount, User user);

        Withdrawal proceedWithWithdrawal(Long withdrawalId, boolean accept) throws Exception;

        List<Withdrawal> getUsersWithdrawalHistory(User user);

        List<Withdrawal> getAllWithdrawalRequest();
    }


