package com.example.FlightSystemsSpring.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.FlightSystemsSpring.controllers.HttpTestFunctions.getStatusCode;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerControllerTest {

    @Test
    void updateCustomer() {
    }

    @Test
    void addTicket() {
    }

    @Test
    void removeTicket() {
    }

    @Test
    void getFlightsByCustomer() {
    }

    @Test
    void getMyTickets()
    {
        assertEquals(200, getStatusCode("http://localhost:8080/Customer/getMyTickets/"));
    }
}