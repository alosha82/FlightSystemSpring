package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.Facades.AdministratorFacade;

import com.example.FlightSystemsSpring.Facades.AnonymousFacade;
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
@RequestMapping("Administrator")
public class AdministratorController
{

    AdministratorFacade administratorFacade;
    @GetMapping("/authenticate")
    @SneakyThrows
    public void getUserDetails(){
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val role=SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
        String pureRole=role.replace("ROLE_","");

        if (!pureRole.equals("Administrator"))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        AnonymousFacade anonymousFacade =new AnonymousFacade();
        System.out.println(anonymousFacade.getUserByUsername(username).getId());
        System.out.println(username);
        System.out.println(pureRole);

        administratorFacade=new AdministratorFacade(new LoginToken(anonymousFacade.getUserByUsername(username).getId(),username,pureRole));
    }

    @GetMapping("/getAllCustomers")
    @SneakyThrows
    public ArrayList<Customers> getAllCustomers()
    {
        ArrayList<Customers> customers;
        customers= administratorFacade.getAllCustomers();
        return customers;
    }
    @PostMapping("/addCustomer")
    @SneakyThrows
    public void addCustomer(@RequestBody Users user , @RequestBody Customers customer)
    {
        administratorFacade.addCustomer(user,customer);
    }
    @PostMapping("/addAirline")
    @SneakyThrows
    public void addAirline(@RequestBody Users user , @RequestBody AirlineCompanies airlineCompany)
    {
        administratorFacade.addAirline(user,airlineCompany);
    }
    @PostMapping("/addAdministrator")
    @SneakyThrows
    public void addAdministrator(@RequestBody Users user , @RequestBody Administrators administrator)
    {
        administratorFacade.addAdministrator(user,administrator);
    }
    @DeleteMapping("/removeCustomer")
    @SneakyThrows
    public void removeCustomer(@RequestBody Customers customer)
    {
        administratorFacade.removeCustomer(customer);
    }
    @DeleteMapping("/removeAirline")
    @SneakyThrows
    public void removeAirline(@RequestBody AirlineCompanies airlineCompany)
    {
        administratorFacade.removeAirline(airlineCompany);
    }
    @DeleteMapping("/removeAdministrator")
    @SneakyThrows
    public void removeAdministrator(@RequestBody Administrators administrator)
    {
        administratorFacade.removeAdministrator(administrator);
    }
}
