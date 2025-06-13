package com.shirish.service;


import com.shirish.domain.OrderType;
import com.shirish.modal.Coin;
import com.shirish.modal.Order;
import com.shirish.modal.OrderItem;
import com.shirish.modal.User;

import java.util.List;

public interface OrderService {
    Order createorder(User user , OrderItem orderItem , OrderType orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId , OrderType orderType , String assetSymbol);

    Order processOrder(Coin coin , double quantity , OrderType orderType, User user) throws Exception;





}
