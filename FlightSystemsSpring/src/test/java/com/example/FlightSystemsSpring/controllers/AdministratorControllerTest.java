package com.example.FlightSystemsSpring.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.FlightSystemsSpring.controllers.HttpTestFunctions.getStatusCode;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AdministratorControllerTest {

    @Test
    void getAllCustomers()
    {
        assertEquals(200, getStatusCode("http://localhost:8080/Administrator/getAllCustomers/"));
    }

    @Test
    void addCustomer() {
    }

    @Test
    void addAirline() {
    }

    @Test
    void addAdministrator() {
    }

    @Test
    void removeCustomer() {
    }

    @Test
    void removeAirline() {
    }

    @Test
    void removeAdministrator() {
    }
}