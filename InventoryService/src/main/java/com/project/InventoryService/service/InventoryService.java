package com.project.InventoryService.service;

import com.project.InventoryService.entity.Event;
import com.project.InventoryService.entity.Venue;
import com.project.InventoryService.repository.EventRepository;
import com.project.InventoryService.repository.VenueRepository;
import com.project.InventoryService.response.EventInventoryResponse;
import com.project.InventoryService.response.VenueInventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public InventoryService(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }


    public List<EventInventoryResponse> getAllEvents() {
        final List<Event> events = eventRepository.findAll();
        return events.stream().map(event -> EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .build()).collect(Collectors.toList());

    }


    public VenueInventoryResponse getVenueInformation(final Long venueId) {
        final Venue venue=venueRepository.findById(venueId).orElse(null);

        return VenueInventoryResponse.builder()
                .venueId(venue.getId())
                .venueName(venue.getName())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }

    public EventInventoryResponse getEventInventory(final Long eventId) {
         final  Event event= eventRepository.findById(eventId).orElse(null);

         return  EventInventoryResponse.builder()
                 .event(event.getName())
                 .capacity(event.getLeftCapacity())
                 .venue(event.getVenue())
                 .ticketPrice(event.getTicketPrice())
                 .eventId(event.getId())
                 .build();
    }

    public void updateEventCapacity(Long eventId, Long ticketBooked) {
        final Event event= eventRepository.findById(eventId).orElse(null);
        event.setLeftCapacity(event.getLeftCapacity()-ticketBooked);
        eventRepository.saveAndFlush(event);
        log.info("Updated event capacity for eventId :{} with ticket booked: {}",eventId,ticketBooked);

    }
}

