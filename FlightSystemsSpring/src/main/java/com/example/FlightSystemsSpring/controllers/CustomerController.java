package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.Facades.CustomerFacade;
import com.example.FlightSystemsSpring.entities.*;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("Customer")
public class CustomerController
{
    @PutMapping("/updateCustomer")
    @SneakyThrows
    public void updateCustomer(@RequestBody Customers customer)
    {
        CustomerFacade customerFacade=new CustomerFacade(new LoginToken());
        customerFacade.updateCustomer(customer);
    }
    @PostMapping("/addTicket")
    @SneakyThrows
    public void addTicket(@RequestBody Tickets ticket)
    {
        CustomerFacade customerFacade=new CustomerFacade(new LoginToken());
        customerFacade.addTicket(ticket);
    }
    @DeleteMapping("/removeTicket")
    @SneakyThrows
    public void removeTicket(@RequestBody Tickets ticket)
    {
        CustomerFacade customerFacade=new CustomerFacade(new LoginToken());
        customerFacade.removeTicket(ticket);
    }
    @GetMapping("/getFlightsByCustomer")
    @SneakyThrows
    public ArrayList<Flights> getFlightsByCustomer(@RequestBody Customers customer)
    {
        ArrayList<Flights> flights;
        CustomerFacade customerFacade=new CustomerFacade(new LoginToken());
        flights=customerFacade.getFlightsByCustomer(customer);
        return flights;
    }
    @GetMapping("/getMyTickets")
    @SneakyThrows
    public ArrayList<Tickets> getMyTickets()
    {
        ArrayList<Tickets> tickets;
        CustomerFacade customerFacade=new CustomerFacade(new LoginToken());
        tickets=customerFacade.getMyTickets();
        return tickets;
    }
}
