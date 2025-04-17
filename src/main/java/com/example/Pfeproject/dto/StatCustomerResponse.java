package com.example.Pfeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatCustomerResponse {
        public Long cars;

        public  Long savedCars;

        public Long bookings;

        public Long approvedBookings;

        public Long rejectedBookings;

        public Long pendingBookings;

}
