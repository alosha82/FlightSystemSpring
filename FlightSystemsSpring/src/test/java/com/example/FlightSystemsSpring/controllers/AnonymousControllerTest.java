package com.example.FlightSystemsSpring.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.FlightSystemsSpring.controllers.HttpTestFunctions.getStatusCode;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AnonymousControllerTest {

    @Test
    void login() {
    }

    @Test
    void addCustomer() {
    }

    @Test
    void getFlightsByParameters() {
    }

    @Test
    void getAirlineByParameters() {
    }

    @Test
    void getAllFlights() {
    }

    @Test
    void getAllAirlines() {
    }

    @Test
    void getAirlineById() {
    }

    @Test
    void getFlightsByOriginCountryId() {
    }

    @Test
    void getFlightsByDestinationCountryId() {
    }

    @Test
    void getFlightsByDepartureDate() {
    }

    @Test
    void getFlightsByLandingDate() {
    }

    @Test
    void getAllCountries()
    {
        assertEquals(200, getStatusCode("http://localhost:8080/getAllCountries/"));
    }

    @Test
    void getCountryById()
    {
    }
}