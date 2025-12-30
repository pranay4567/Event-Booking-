package com.project.InventoryService.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueInventoryResponse {
    private Long venueId;
    private String venueName;
    private Long totalCapacity;


}
