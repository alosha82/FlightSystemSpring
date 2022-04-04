package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.Facades.AnonymousFacade;
import com.example.FlightSystemsSpring.entities.*;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;


@RestController
public class AnonymousController
{

    @GetMapping("/login/{username}/{password}")
    @SneakyThrows
    public AnonymousFacade login(@PathVariable String username,@PathVariable String password)
    {
        AnonymousFacade anonymous=new AnonymousFacade();
        return anonymous.login(username,password);
    }
    @PostMapping("/addCustomer")
    @SneakyThrows
    public void addCustomer(@RequestBody Users UserBody, @RequestBody Customers customerBody)
    {
        AnonymousFacade anonymous=new AnonymousFacade();
        anonymous.addCustomer(UserBody,customerBody);
    }
    @GetMapping("/getFlightsByParameters/{origin}/{destination}/{date}")
    public ArrayList<Flights> getFlightsByParameters(@PathVariable String origin, @PathVariable String destination, @PathVariable String date)
    {
        ArrayList<Flights> flights;
        int originId=Integer.parseInt(origin);
        int destinationId=Integer.parseInt(destination);
        AnonymousFacade anonymous=new AnonymousFacade();
        flights=anonymous.getFlightsByParameters(originId,destinationId, Timestamp.valueOf(date));
        return flights;
    }
    @GetMapping("/getAirlineByParameters/{country}")
    public ArrayList<AirlineCompanies> getAirlineByParameters(@PathVariable String country)
    {
        ArrayList<AirlineCompanies> AirlineCompanies;
        int countryId=Integer.parseInt(country);
        AnonymousFacade anonymous=new AnonymousFacade();
        AirlineCompanies=anonymous.getAirlineByParameters(countryId);
        return AirlineCompanies;
    }
    @GetMapping("/getAllFlights")
    public ArrayList<Flights> getAllFlights()
    {
        ArrayList<Flights> flights;
        AnonymousFacade anonymous=new AnonymousFacade();
        flights=anonymous.getAllFlights();
        return flights;
    }
    @GetMapping("/getAllAirlines")
    public ArrayList<AirlineCompanies> getAllAirlines()
    {
        ArrayList<AirlineCompanies> airlineCompanies;
        AnonymousFacade anonymous=new AnonymousFacade();
        airlineCompanies=anonymous.getAllAirlines();
        return airlineCompanies;
    }
    @GetMapping("/getAirlineById/{id}")
    public AirlineCompanies getAirlineById(@PathVariable String id)
    {
        AirlineCompanies airlineCompany;
        int airlineId=Integer.parseInt(id);
        AnonymousFacade anonymous=new AnonymousFacade();
        airlineCompany=anonymous.getAirlineById(airlineId);
        return airlineCompany;
    }
    @GetMapping("/getFlightsByOriginCountryId/{id}")
    public ArrayList<Flights> getFlightsByOriginCountryId(@PathVariable String id)
    {
        ArrayList<Flights> flights;
        int originId=Integer.parseInt(id);
        AnonymousFacade anonymous=new AnonymousFacade();
        flights=anonymous.getFlightsByOriginCountryId(originId);
        return flights;
    }
    @GetMapping("/getFlightsByDestinationCountryId/{id}")
    public ArrayList<Flights> getFlightsByDestinationCountryId(@PathVariable String id)
    {
        ArrayList<Flights> flights;
        int destinationId=Integer.parseInt(id);
        AnonymousFacade anonymous=new AnonymousFacade();
        flights=anonymous.getFlightsByDestinationCountryId(destinationId);
        return flights;
    }
    @GetMapping("/getFlightsByDepartureDate/{date}")
    public ArrayList<Flights> getFlightsByDepartureDate(@PathVariable String date)
    {
        ArrayList<Flights> flights;
        AnonymousFacade anonymous=new AnonymousFacade();
        flights=anonymous.getFlightsByDepartureDate(Timestamp.valueOf(date));
        return flights;
    }
    @GetMapping("/getFlightsByLandingDate/{date}")
    public ArrayList<Flights> getFlightsByLandingDate(@PathVariable String date)
    {
        ArrayList<Flights> flights;
        AnonymousFacade anonymous=new AnonymousFacade();
        flights=anonymous.getFlightsByLandingDate(Timestamp.valueOf(date));
        return flights;
    }
    @GetMapping("/getAllCountries")
    public ArrayList<Countries> getAllCountries()
    {
        ArrayList<Countries> countries;
        AnonymousFacade anonymous=new AnonymousFacade();
        countries=anonymous.getAllCountries();
        return countries;
    }
    @GetMapping("/getCountryById/{id}")
    public Countries getCountryById(@PathVariable String id)
    {
        Countries country;
        int countryId=Integer.parseInt(id);
        AnonymousFacade anonymous=new AnonymousFacade();
        country=anonymous.getCountry(countryId);
        return country;
    }
}
