package com.example.FlightSystemsSpring.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.FlightSystemsSpring.controllers.HttpTestFunctions.getStatusCode;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AirlineControllerTest {

    @Test
    void updateAirline() {
    }

    @Test
    void updateFlight() {
    }

    @Test
    void addFlight() {
    }

    @Test
    void removeFlight() {
    }

    @Test
    void getMyFlights()
    {
        assertEquals(200, getStatusCode("http://localhost:8080/AirlineCompany/getMyFlights/"));
    }
}