package ru.inno.market;

import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;
import ru.inno.market.model.PromoCodes;

public class Main {

    public static void main(String[] args) {
        // сервис маркета. Создает заказы и управляет ими
        MarketService service = new MarketService();
        // каталог товаров. Знает, какие и сколько товаров есть
        Catalog catalog = new Catalog();

        Client client = new Client(1, "Dima");
        Item item =  catalog.getItemById(1);
        int id = service.createOrderFor(client);
        service.addItemToOrder(item.getId(), id);
        service.applyDiscountForOrder(id, PromoCodes.FIRST_ORDER);
        Order order = service.getOrderInfo(id);

        System.out.println(order);
        System.out.println(order.getCart().size() == 1);
        System.out.println(order.getTotalPrice() == item.getPrice()*0.8);
    }
}
