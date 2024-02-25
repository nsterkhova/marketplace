package ru.inno.market;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.inno.market.model.PromoCodes.HAPPY_HOUR;
import static ru.inno.market.model.PromoCodes.HAPPY_NEW_YEAR;

public class MarketServiceTest {

    @Test
    @DisplayName("Создание заказа")
    public void testCreateNewOrder() {
        MarketService service = new MarketService();
        Client client = new Client(1, "NewClient");

        int orderId = service.createOrderFor(client);

        assertEquals(0, orderId);
    }

    @Test
    @DisplayName("Добавление товарной позиции в заказ")
    public void testAddItemToOrder() {
        MarketService service = new MarketService();
        Client client1 = new Client(1, "NewClient");
        Client client2 = new Client(2, "NewClient2");
        Catalog catalog = new Catalog();
        Item item = catalog.getItemById(5);
        int orderId1 = service.createOrderFor(client1);
        int orderId2 = service.createOrderFor(client2);

        service.addItemToOrder(5, orderId1);
        Order order1 = service.getOrderInfo(orderId1);
        Map<Item, Integer> cart1 = order1.getCart();
        Order order2 = service.getOrderInfo(orderId2);
        Map<Item, Integer> cart2 = order2.getCart();


        assertEquals(0, cart2.size());
        assertEquals(1, cart1.size());
        assertEquals(item, cart1.keySet().toArray()[0]);
    }

    @Test
    @DisplayName("Получение данных созданного заказа")
    public void testGetOrderInfoById() {
        MarketService service = new MarketService();
        Client client = new Client(1, "NewClient");
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        Item item2 = catalog.getItemById(5);
        int orderId1 = service.createOrderFor(client);
        service.addItemToOrder(1, orderId1);
        service.addItemToOrder(1, orderId1);
        service.addItemToOrder(5, orderId1);

        Order order = service.getOrderInfo(orderId1);

        Map<Item, Integer> cartToCheck = order.getCart();
        double totalPriceToCheck = order.getTotalPrice();
        Client clientToCheck = order.getClient();
        boolean discountAppliedToCheck = order.isDiscountApplied();

        assertEquals(client, clientToCheck);
        assertEquals(285970.0, totalPriceToCheck);
        assertFalse(discountAppliedToCheck);
        assertEquals(2, Arrays.stream(cartToCheck.keySet().toArray()).count());   //в корзине содержится 2 товара
        assertEquals(2, cartToCheck.get(item1));   //в корзине содержится item1 И его количество = 2
        assertEquals(1, cartToCheck.get(item2));   //в корзине содержится item2 И его количество = 1
    }

    @Test
    @DisplayName("Превышение доступного количества товара из каталога в корзине")
    public void testAddAllQuantityOfItemToOrder() {
        MarketService service = new MarketService();
        Client client = new Client(1, "NewClient");
        int orderId = service.createOrderFor(client);

        service.addItemToOrder(7, orderId);
        service.addItemToOrder(7, orderId);

        assertThrowsExactly(NoSuchElementException.class, () -> service.addItemToOrder(7, orderId), "Товар закончился");
    }

    @Test
    @DisplayName("Добавлние скидки по промокоду")
    public void testApplyDiscountByPromocode() {
        MarketService service = new MarketService();
        Client client = new Client(1, "NewClient");
        int orderId = service.createOrderFor(client);
        service.addItemToOrder(7, orderId);
        service.addItemToOrder(10, orderId);

        service.applyDiscountForOrder(orderId, HAPPY_NEW_YEAR);

        Order order = service.getOrderInfo(orderId);
        double orderTotalPrice = order.getTotalPrice();

        assertEquals(82790.1, orderTotalPrice);
        assertTrue(order.isDiscountApplied());
    }

    @Test
    @DisplayName("Применение нескольких промокодов не выполняется")
    public void testApplyFewDiscountsByPromocode() {
        MarketService service = new MarketService();
        Client client = new Client(1, "NewClient");
        int orderId = service.createOrderFor(client);
        service.addItemToOrder(7, orderId);
        service.addItemToOrder(10, orderId);

        service.applyDiscountForOrder(orderId, HAPPY_NEW_YEAR);
        service.applyDiscountForOrder(orderId, HAPPY_HOUR);

        Order order = service.getOrderInfo(orderId);
        double orderTotalPrice = order.getTotalPrice();

        assertEquals(82790.1, orderTotalPrice);
        assertTrue(order.isDiscountApplied());
    }
}
