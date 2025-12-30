package com.project.bookingservice.service;

import com.project.bookingservice.client.InventoryServiceClient;
import com.project.bookingservice.entity.Customer;
import com.project.bookingservice.event.BookingEvent;
import com.project.bookingservice.repository.CustomerRepository;
import com.project.bookingservice.request.BookingRequest;
import com.project.bookingservice.response.BookingResponse;
import com.project.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String,BookingEvent> kafkaTemplate;

    @Autowired
    public BookingService(final CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient, KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request) {
        final Customer customer= customerRepository.findById(request.getUserId()).orElse(null);
        if(customer==null){
            throw new RuntimeException("User Not Found");
        }
        final InventoryResponse inventoryResponse=inventoryServiceClient.getInventory(request.getEventId());
      //  System.out.println("Inventory Service Response"+inventoryResponse);
        log.info("Inventory Response: {}",inventoryResponse);
        if(inventoryResponse.getCapacity()<request.getTicketCount()){
            throw new RuntimeException("Not Enough Inventory");
        }
        final BookingEvent bookingEvent= createBookingEvent(request,customer,inventoryResponse);
        kafkaTemplate.send("booking",bookingEvent);
        log.info("Booking Sent To Kafka: {}",bookingEvent);

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(BookingRequest request, Customer customer, InventoryResponse inventoryResponse) {

        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();
    }
}
