package ru.inno.market;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.inno.market.model.Category.LAPTOPS;
import static ru.inno.market.model.Category.SMARTPHONES;

public class OrderTest {

    @Test
    @DisplayName("Создание нового заказа с пустой корзиной")
    public void testCreateNewOrder() {
        int orderId = 1;
        Client client = new Client(1, "NewClient");

        Order order = new Order(orderId, client);

        assertEquals(orderId, order.getId());
        assertEquals(client, order.getClient());
        assertEquals(0, order.getTotalPrice());
        assertEquals(0, order.getCart().size());
        assertFalse(order.isDiscountApplied());
    }

    @Test
    @DisplayName("Создание нескольких заказов")
    public void testCreateFewOrderToSameClient() {
        Client client1 = new Client(1, "NewClient1");
        Client client2 = new Client(2, "NewClient2");

        Order order1 = new Order(1, client1);
        Order order2 = new Order(2, client2);

        assertEquals(client1, order1.getClient());
        assertEquals(client2, order2.getClient());
    }

    @Test
    @DisplayName("Добавление одного товара в корзину")
    public void testAddNewItemToCart() {
        Client client = new Client(1, "NewClient");
        Order order = new Order(1, client);
        Item item = new Item(1, "Новый товар", SMARTPHONES, 12.25);

        order.addItem(item);
        Map<Item, Integer> cart = order.getCart();

        assertEquals(12.25, order.getTotalPrice());
        assertEquals(1, order.getCart().size());
        assertEquals(1, cart.get(item));
        assertEquals(item, cart.keySet().toArray()[0]);
    }

    @Test
    @DisplayName("Добавление одной товарной позиции несколько раз")
    public void testAddNewItemToCartFewTimes() {
        Client client = new Client(1, "NewClient");
        Order order = new Order(1, client);
        Item item = new Item(1, "Новый товар", SMARTPHONES, 12.25);

        order.addItem(item);
        order.addItem(item);
        order.addItem(item);
        Map<Item, Integer> cart = order.getCart();

        assertEquals(36.75, order.getTotalPrice());
        assertEquals(1, order.getCart().size());
        assertEquals(3, cart.get(item));
    }

    @Test
    @DisplayName("Добавление нескольких товарных позиций")
    public void testAddFewItemsToCart() {
        Client client = new Client(1, "NewClient");
        Order order = new Order(1, client);
        Item firstItem = new Item(1, "Новый товар", SMARTPHONES, 12.25);
        Item secondItem = new Item(2, "Новый товар2", LAPTOPS, 78.52);

        order.addItem(firstItem);
        order.addItem(secondItem);
        order.addItem(secondItem);
        Map<Item, Integer> cart = order.getCart();
        double checkTotalPrice = firstItem.getPrice() + secondItem.getPrice() * 2;

        assertEquals(checkTotalPrice, order.getTotalPrice());
        assertEquals(2, order.getCart().size());
        assertEquals(1, cart.get(firstItem));
        assertEquals(2, cart.get(secondItem));
    }

    @Test
    @DisplayName("Применение дисконта к итоговой сумме заказа 1 раз")
    public void testApplyDiscount() {
        Client client = new Client(1, "NewClient");
        Order order = new Order(1, client);
        Item item = new Item(1, "Новый товар", SMARTPHONES, 12.25);
        order.addItem(item);

        double discount = 0.2;
        order.applyDiscount(discount);

        assertEquals(9.8, order.getTotalPrice());
        assertTrue(order.isDiscountApplied());
    }

    @Test
    @DisplayName("Применение дисконта к итоговой сумме заказа несколько раз невозможно")
    public void testApplyDiscountFewTimes() {
        Client client = new Client(1, "NewClient");
        Order order = new Order(1, client);
        Item item = new Item(1, "Новый товар", SMARTPHONES, 12.25);
        order.addItem(item);

        double discount = 0.2;
        order.applyDiscount(discount);
        discount = 0.3;
        order.applyDiscount(discount);

        assertEquals(9.8, order.getTotalPrice());
        assertTrue(order.isDiscountApplied());
    }

}
