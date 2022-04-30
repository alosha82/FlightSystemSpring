package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.Facades.AnonymousFacade;
import com.example.FlightSystemsSpring.Facades.CustomerFacade;
import com.example.FlightSystemsSpring.entities.*;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping("Customer")
public class CustomerController
{
    CustomerFacade customerFacade;
    @GetMapping("/authenticate")
    @SneakyThrows
    public void getUserDetails(){
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val role=SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
        String pureRole=role.replace("ROLE_","");
        if (!pureRole.equals("Customer"))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        AnonymousFacade anonymousFacade =new AnonymousFacade();
        System.out.println(anonymousFacade.getUserByUsername(username).getId());
        System.out.println(username);
        System.out.println(pureRole);

        customerFacade=new CustomerFacade(new LoginToken(anonymousFacade.getUserByUsername(username).getId(),username,pureRole));
    }
    @PutMapping("/updateCustomer")
    @SneakyThrows
    public void updateCustomer(@RequestBody Customers customer,@RequestBody LoginToken token)
    {
        customerFacade.updateCustomer(customer);
    }
    @PostMapping("/addTicket")
    @SneakyThrows
    public void addTicket(@RequestBody Tickets ticket)
    {
        customerFacade.addTicket(ticket);
    }
    @DeleteMapping("/removeTicket")
    @SneakyThrows
    public void removeTicket(@RequestBody Tickets ticket)
    {
        customerFacade.removeTicket(ticket);
    }
    @GetMapping("/getFlightsByCustomer")
    @SneakyThrows
    public ArrayList<Flights> getFlightsByCustomer(@RequestBody Customers customer)
    {
        ArrayList<Flights> flights;
        flights=customerFacade.getFlightsByCustomer(customer);
        return flights;
    }
    @GetMapping("/getMyTickets")
    @SneakyThrows
    public ArrayList<Tickets> getMyTickets()
    {
        ArrayList<Tickets> tickets;
        tickets=customerFacade.getMyTickets();
        return tickets;
    }
}
