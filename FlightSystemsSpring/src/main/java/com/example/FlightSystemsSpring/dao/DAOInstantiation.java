package com.example.FlightSystemsSpring.dao;

import com.example.FlightSystemsSpring.entities.*;

public class DAOInstantiation
{
    private static GenericDAO<Administrators> administratorsDAO;
    private static GenericDAO<AirlineCompanies> airlineCompaniesDAO;
    private static GenericDAO<Countries> countriesDAO;
    private static GenericDAO<Customers> customersDAO;
    private static GenericDAO<Flights> flightsDAO;
    private static GenericDAO<Tickets> ticketsDAO;
    private static GenericDAO<UserRoles> userRolesDAO;
    private static GenericDAO<Users> usersDAO;

    public static GenericDAO<Administrators> getAdministratorsDAO()
    {
        if (administratorsDAO==null)
        {
            administratorsDAO=new GenericDAO<>("Administrators", new Administrators());
            return administratorsDAO;
        }
        return administratorsDAO;
    }

    public static GenericDAO<AirlineCompanies> getAirlineCompaniesDAO()
    {
        if (airlineCompaniesDAO==null)
        {
            airlineCompaniesDAO=new GenericDAO<>("Airline_Companies", new AirlineCompanies());
            return airlineCompaniesDAO;
        }
        return airlineCompaniesDAO;
    }

    public static GenericDAO<Countries> getCountriesDAO()
    {
        if (countriesDAO==null)
        {
            countriesDAO=new GenericDAO<>("Countries", new Countries());
            return countriesDAO;
        }
        return countriesDAO;
    }

    public static GenericDAO<Customers> getCustomersDAO()
    {
        if (customersDAO==null)
        {
            customersDAO=new GenericDAO<>("Customers", new Customers());
            return customersDAO;
        }
        return customersDAO;
    }

    public static GenericDAO<Flights> getFlightsDAO()
    {
        if (flightsDAO==null)
        {
            flightsDAO=new GenericDAO<>("Flights", new Flights());
            return flightsDAO;
        }
        return flightsDAO;
    }

    public static GenericDAO<Tickets> getTicketsDAO()
    {
        if (ticketsDAO==null)
        {
            ticketsDAO=new GenericDAO<>("Tickets", new Tickets());
            return ticketsDAO;
        }
        return ticketsDAO;
    }

    public static GenericDAO<UserRoles> getUserRolesDAO()
    {
        if (userRolesDAO==null)
        {
            userRolesDAO=new GenericDAO<>("User_Roles", new UserRoles());
            return userRolesDAO;
        }
        return userRolesDAO;
    }

    public static GenericDAO<Users> getUsersDAO()
    {
        if (usersDAO==null)
        {
            usersDAO=new GenericDAO<>("Users", new Users());
            return usersDAO;
        }
        return usersDAO;
    }
}
