package com.example.FlightSystemsSpring.Facades;

import com.example.FlightSystemsSpring.dao.GenericDAO;
import com.example.FlightSystemsSpring.entities.AirlineCompanies;
import com.example.FlightSystemsSpring.entities.Flights;
import com.example.FlightSystemsSpring.entities.Tickets;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.FlightSystemsSpring.dao.GenericDAO.*;

@Component
public class AirlineFacade extends AnonymousFacade
{
     private LoginToken token;

    public AirlineFacade(LoginToken loginToken)
    {
        this.token = loginToken;
    }
    /**Updates Airline.*/
    public void updateAirline (AirlineCompanies airlineCompany) throws Exception
    {
        if (this.token.getId()!=airlineCompany.getId())
            throw new Exception("You can not update other airline companies");
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = getAirlineCompaniesDAO();
        if (airlineCompany.getId()==null)
            System.out.println("Id must be provided inside the airlineCompany. No update was made to the DataBase");
        else
        {
            if (airlineCompany.getUserId()<0)
            {
                airlineCompaniesDAO.closeAllDAOConnections();
                throw new Exception("No negative users present. Can not update that airline company to the DataBase");
            }
            if (airlineCompany.getCountryId()<0)
            {
                airlineCompaniesDAO.closeAllDAOConnections();
                throw new Exception("No negative countries present. Can not update that airline company to the DataBase\"");
            }
            airlineCompaniesDAO.update(airlineCompany,airlineCompany.getId());
        }
        airlineCompaniesDAO.closeAllDAOConnections();
    }
    /**Updates Flight.*/
    public void updateFlight (Flights flight) throws Exception
    {
        if (token.getId()!=flight.getAirlineCompanyId())
            throw new Exception("You can not update flights of another airline company");
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        if (flight.getId()==null)
            System.out.println("Id must be provided inside the flight. No update was made to the DataBase");
        else
        {
            if (flight.getRemainingTickets()<0)
            {
                flightsDAO.closeAllDAOConnections();
                throw new Exception("Remaining tickets is negative. Can not add that flight to the DataBase");
            }
            if (flight.getOriginCountryId()==flight.getDestinationCountryId())
            {
                flightsDAO.closeAllDAOConnections();
                throw new Exception("We do net provide flights within the country. Can not add that flight to the DataBase");
            }
            if (flight.getLandingTime().before(flight.getDepartureTime()))
            {
                flightsDAO.closeAllDAOConnections();
                throw new Exception("There is a mix up in your flight times. it is impossible to land before you take flight. Can not add that flight to the DataBase");
            }
            flightsDAO.update(flight,flight.getId());
        }
        flightsDAO.closeAllDAOConnections();
    }
    /**Adds Flight.*/
    public void addFlight(Flights flight) throws Exception
    {
        ArrayList<Flights> flights;
        if (token.getId()!=flight.getAirlineCompanyId())
            throw new Exception("You can not add flights of another airline company");
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        flights=flightsDAO.getAll();
        for (int i = 0; i < flights.size(); i++)
        {
            if ((flights.get(i).getAirlineCompanyId()==flight.getAirlineCompanyId())
                    &&(flights.get(i).getOriginCountryId()==flight.getOriginCountryId())
                    &&(flights.get(i).getDestinationCountryId()==flight.getDestinationCountryId())
                    &&(flights.get(i).getDepartureTime().equals(flight.getDepartureTime()))
                    &&(flights.get(i).getLandingTime().equals(flight.getLandingTime()))
                    &&(flights.get(i).getRemainingTickets()==flight.getRemainingTickets()))
            {
                flightsDAO.closeAllDAOConnections();
                throw new Exception("That flight is already in the DataBase");
            }
        }
        if (flight.getRemainingTickets()<0)
        {
            flightsDAO.closeAllDAOConnections();
            throw new Exception("Remaining tickets is negative. Can not add that flight to the DataBase");
        }
        if (flight.getOriginCountryId()==flight.getDestinationCountryId())
        {
            flightsDAO.closeAllDAOConnections();
            throw new Exception("We do net provide flights within the country. Can not add that flight to the DataBase");
        }
        if (flight.getLandingTime().before(flight.getDepartureTime()))
        {
            flightsDAO.closeAllDAOConnections();
            throw new Exception("There is a mix up in your flight times. it is impossible to land before you take flight. Can not add that flight to the DataBase");
        }
        flightsDAO.add(flight);
        flightsDAO.closeAllDAOConnections();
    }
    /**Removes Flight and its tickets*/
    public void removeFlight(Flights flight) throws Exception
    {
        if (token.getId()!=flight.getAirlineCompanyId())
            throw new Exception("You can not remove flights of another airline company");
        ArrayList<Tickets> flightTickets;
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        GenericDAO<Tickets> ticketsDAO = getTicketsDAO();
        if (flight.getId()==null)
            System.out.println("Id must be provided inside the flight. No removal was made in the DataBase");
        else
        {
            flightTickets=getTicketsByFlight(flight);
            for (Tickets flightTicket : flightTickets)
            {
                try {
                    ticketsDAO.remove(flightTicket);
                }catch (Exception e)
                {
                    throw new Exception("Was unable to remove the tickets to the flight");
                }
            }
            try {
                flightsDAO.remove(flight);
            }catch (Exception e)
            {
                throw new Exception("Was unable to remove the the flight.");
            }
        }
        flightsDAO.closeAllDAOConnections();
    }

    /**Joins flights with airlineCompanies and filters the joined entity by Airline_Company_Id
     get Flights for the airline*/
    public ArrayList<Flights> getMyFlights() throws Exception
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Flights", List.of("Id", "Airline_Company_Id","Origin_Country_Id","Destination_Country_Id"
                ,"Departure_Time","Landing_Time","Remaining_Tickets"));
        if (token.getId()==null)
        {
            flightsDAO.closeAllDAOConnections();
            throw new Exception("Id must be provided inside the airline company. Can not get flights from DataBase");
        }
        else
        {
            flights = flightsDAO.joinTwoByWithWhereClause(tablesToColumnsMap,"Airline_Company_Id","Airline_Companies","Id"
                    ,new Flights(),"WHERE \"Flights\".\"Airline_Company_Id\"="+token.getId());
            flightsDAO.closeAllDAOConnections();
            return flights;
        }
    }
}
