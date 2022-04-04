package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.Facades.AirlineFacade;
import com.example.FlightSystemsSpring.entities.AirlineCompanies;
import com.example.FlightSystemsSpring.entities.Flights;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("AirlineCompany")
public class AirlineController
{
    @PutMapping("/updateAirline")
    @SneakyThrows
    public void updateAirline(@RequestBody AirlineCompanies airlineCompany)
    {
        AirlineFacade airlineFacade=new AirlineFacade(new LoginToken());
        airlineFacade.updateAirline(airlineCompany);
    }
    @PutMapping("/updateFlight")
    @SneakyThrows
    public void updateFlight(@RequestBody Flights flight)
    {
        AirlineFacade airlineFacade=new AirlineFacade(new LoginToken());
        airlineFacade.updateFlight(flight);
    }
    @PostMapping("/addFlight")
    @SneakyThrows
    public void addFlight(@RequestBody Flights flight)
    {
        AirlineFacade airlineFacade=new AirlineFacade(new LoginToken());
        airlineFacade.addFlight(flight);
    }
    @DeleteMapping("/removeFlight")
    @SneakyThrows
    public void removeFlight(@RequestBody Flights flight)
    {
        AirlineFacade airlineFacade=new AirlineFacade(new LoginToken());
        airlineFacade.removeFlight(flight);
    }

    @GetMapping("/getMyFlights")
    @SneakyThrows
    public ArrayList<Flights> getMyFlights()
    {
        ArrayList<Flights> flights;
        AirlineFacade airlineFacade=new AirlineFacade(new LoginToken());
        flights= airlineFacade.getMyFlights();
        return flights;
    }
}
