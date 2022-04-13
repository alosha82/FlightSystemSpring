package com.example.FlightSystemsSpring.Facades;

import com.example.FlightSystemsSpring.dao.GenericDAO;
import com.example.FlightSystemsSpring.entities.Customers;
import com.example.FlightSystemsSpring.entities.Flights;
import com.example.FlightSystemsSpring.entities.Tickets;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.example.FlightSystemsSpring.dao.GenericDAO.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerFacadeTest {

    @Test
    @SneakyThrows
    void updateCustomer()
    {
        LoginToken loginToken =new LoginToken();
        Customers customer;
        Customers customerAfter;
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        GenericDAO<Customers> customersDAO= getCustomersDAO();
        customer=customersDAO.getById(1);
        customer.setLastName(customer.getLastName()+1);
        customerFacade.updateCustomer(customer);
        customerAfter=customersDAO.getById(1);
        assertEquals(customer,customerAfter);
    }

    @Test
    @SneakyThrows
    void addTicket()
    {
        LoginToken loginToken =new LoginToken();
        Tickets ticket=new Tickets();
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        ticket.setId(5);
        ticket.setFlightId(5);
        ticket.setCostumersId(1);
        customerFacade.addTicket(ticket);
        GenericDAO<Tickets> ticketsDAO = getTicketsDAO();
        assertEquals(ticket, ticketsDAO.getById(5));
    }

    @Test
    @SneakyThrows
    void removeTicket()
    {
        LoginToken loginToken =new LoginToken();
        Tickets ticket=new Tickets();
        loginToken.setId(3l);
        loginToken.setName("createfromnewusercostomerrole");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        ticket.setId(3);
        ticket.setFlightId(5);
        ticket.setCostumersId(3);
        customerFacade.removeTicket(ticket);
    }

    @Test
    @SneakyThrows
    void getFlightsByCustomer()
    {
        LoginToken loginToken =new LoginToken();
        Customers customer;
        ArrayList<Flights> flights;
        Flights flightsForTest;
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        GenericDAO<Customers> customersDAO= getCustomersDAO();
        customer=customersDAO.getById(1);
        flights = customerFacade.getFlightsByCustomer(customer);
        GenericDAO<Flights> flightsDAO= getFlightsDAO();
        flightsForTest=flightsDAO.getById(4);
        assertEquals(flightsForTest,flights.get(0));
    }

    @Test
    @SneakyThrows
    void getMyTickets()
    {
        LoginToken loginToken =new LoginToken();
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        System.out.println(customerFacade.getMyTickets());
    }


}