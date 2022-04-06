package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.Facades.AdministratorFacade;

import com.example.FlightSystemsSpring.entities.*;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("Administrator")
public class AdministratorController
{
    @Autowired
    AdministratorFacade administratorFacade;
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
//        AdministratorFacade administratorFacade=new AdministratorFacade(new LoginToken());
        administratorFacade.addCustomer(user,customer);
    }
    @PostMapping("/addAirline")
    @SneakyThrows
    public void addAirline(@RequestBody Users user , @RequestBody AirlineCompanies airlineCompany)
    {
//        AdministratorFacade administratorFacade=new AdministratorFacade(new LoginToken());
        administratorFacade.addAirline(user,airlineCompany);
    }
    @PostMapping("/addAdministrator")
    @SneakyThrows
    public void addAdministrator(@RequestBody Users user , @RequestBody Administrators administrator)
    {
//        AdministratorFacade administratorFacade=new AdministratorFacade(new LoginToken());
        administratorFacade.addAdministrator(user,administrator);
    }
    @DeleteMapping("/removeCustomer")
    @SneakyThrows
    public void removeCustomer(@RequestBody Customers customer)
    {
//        AdministratorFacade administratorFacade=new AdministratorFacade(new LoginToken());
        administratorFacade.removeCustomer(customer);
    }
    @DeleteMapping("/removeAirline")
    @SneakyThrows
    public void removeAirline(@RequestBody AirlineCompanies airlineCompany)
    {
//        AdministratorFacade administratorFacade=new AdministratorFacade(new LoginToken());
        administratorFacade.removeAirline(airlineCompany);
    }
    @DeleteMapping("/removeAdministrator")
    @SneakyThrows
    public void removeAdministrator(@RequestBody Administrators administrator)
    {
//        AdministratorFacade administratorFacade=new AdministratorFacade(new LoginToken());
        administratorFacade.removeAdministrator(administrator);
    }
}
