package ru.inno.market.core;

import ru.inno.market.model.*;

import java.util.HashMap;
import java.util.Map;

public class MarketService {

    private int orderCounter;
    private Map<Integer, Order> orders;
    Catalog catalog;

    public MarketService() {
        orderCounter = 0;
        orders = new HashMap<>();
        catalog = new Catalog();
    }

    public int createOrderFor(Client client){
        int id = orderCounter++;
        Order order = new Order(id, client);
        orders.put(id, order);

        return order.getId();
    }

    public void addItemToOrder(int itemId, int orderId ){                     //Закрыла возможность добавить товар не из каталога, теперь метод принимает на вход itemId вместо Item
        orders.get(orderId).addItem(catalog.getItemById(itemId));
    }

    public double applyDiscountForOrder(int orderId, PromoCodes codes){
        Order order = orders.get(orderId);
        order.applyDiscount(codes.getDiscount());
        return order.getTotalPrice();
    }

    public Order getOrderInfo(int id) {
        return orders.get(id);
    }
}

