package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.Facades.AirlineFacade;
import com.example.FlightSystemsSpring.Facades.AnonymousFacade;
import com.example.FlightSystemsSpring.entities.AirlineCompanies;
import com.example.FlightSystemsSpring.entities.Flights;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping("AirlineCompany")
public class AirlineController
{
    AirlineFacade airlineFacade;

    @GetMapping("/authenticate")
    @SneakyThrows
    public void getUserDetails(){

        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val role=SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
        String pureRole=role.replace("ROLE_","");
        if (!pureRole.equals("Airline_Company"))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        AnonymousFacade anonymousFacade =new AnonymousFacade();
        System.out.println(anonymousFacade.getUserByUsername(username).getId());
        System.out.println(username);
        System.out.println(pureRole);

        airlineFacade=new AirlineFacade(new LoginToken(anonymousFacade.getUserByUsername(username).getId(),username,pureRole));
    }

    @PutMapping("/updateAirline")
    @SneakyThrows

    public void updateAirline(@RequestBody AirlineCompanies airlineCompany)
    {
        airlineFacade.updateAirline(airlineCompany);
    }
    @PutMapping("/updateFlight")
    @SneakyThrows
    public void updateFlight(@RequestBody Flights flight)
    {
        airlineFacade.updateFlight(flight);
    }
    @PostMapping("/addFlight")
    @SneakyThrows
    public void addFlight(@RequestBody Flights flight)
    {
        airlineFacade.addFlight(flight);
    }
    @DeleteMapping("/removeFlight")
    @SneakyThrows
    public void removeFlight(@RequestBody Flights flight)
    {
        airlineFacade.removeFlight(flight);
    }

    @GetMapping("/getMyFlights")
    @SneakyThrows
    public ArrayList<Flights> getMyFlights()
    {
        ArrayList<Flights> flights;
        flights= airlineFacade.getMyFlights();
        return flights;
    }
}
