package com.project.orderservice.service;



import com.project.orderservice.client.InventoryServiceClient;
import com.project.orderservice.entity.Order;
import com.project.orderservice.event.BookingEvent;
import com.project.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    private  final OrderRepository  orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @KafkaListener(topics = "booking",groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent){
        log.info("Received order event: {}",bookingEvent);
        Order order= createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);
        inventoryServiceClient.updateInventory(order.getEventId(),order.getTicketCount());
        log.info("Inventory Updated for event :{} ,less ticket:{}",order.getEventId(),order.getTicketCount());
    }

    private Order createOrder(BookingEvent bookingEvent){
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId((bookingEvent.getEventId()))
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }
}
