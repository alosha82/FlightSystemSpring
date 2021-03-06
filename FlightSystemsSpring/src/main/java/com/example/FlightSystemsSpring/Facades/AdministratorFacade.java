package com.example.FlightSystemsSpring.Facades;

import com.example.FlightSystemsSpring.dao.GenericDAO;
import com.example.FlightSystemsSpring.entities.*;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.FlightSystemsSpring.dao.GenericDAO.*;


@Setter
public class AdministratorFacade extends AnonymousFacade
{
    @Getter
    LoginToken token;
    GenericDAO<Administrators> administratorsDAO = getAdministratorsDAO();
    Administrators administrators;
    /**Checks if there is an administrator company with a given user id.
     * If there is updates the token id to be the id of the administrator*/
    public AdministratorFacade(LoginToken token) throws Exception{
        this.token = token;
        try
        {
            administrators = administratorsDAO.getByFieldType(token.getId().toString(),"User_Id");
            this.token.setId(administrators.getId());
        }
        catch (Exception e)
        {
            throw new Exception("No administrators with your id");
        }
    }
    /**Gets all customers*/
    public ArrayList<Customers> getAllCustomers() throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        ArrayList<Customers> customers;
        GenericDAO<Customers> customersDAO = getCustomersDAO();
        customers = customersDAO.getAll();
        customersDAO.closeAllDAOConnections();
        return customers;
    }
    /**Checks the details of the user*/
    private boolean isUserHaveCorrectDetails(Users user) throws Exception
    {
        String password= user.getPassword();
        if(password.contains(" "))
            throw new Exception("Spaces are not allowed in the password.");
        if(password.length()<7)
            throw new Exception("Your password is to short. Can not add such a user with that kind of insecure password to the database.");
        if((user.getEmail().charAt(0) == '@')||(user.getEmail().charAt(0) == ' ') || !(user.getEmail().contains("@")))
            throw new Exception("Invalid Email.");
        return true;
    }
    @SneakyThrows
    /**Adds customer and its user entity*/
    public void addCustomer(Users user ,Customers customer)
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        if (isUserHaveCorrectDetails(user))
        createNewUser(user,customer);
    }
    /**Adds Airline and its user entity*/
    public void addAirline(Users user,AirlineCompanies airlineCompany) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        if (isUserHaveCorrectDetails(user))
            createNewUser(user,airlineCompany);
    }
    /**Adds Administrator and its user entity*/
    public void addAdministrator(Users user,Administrators administrator) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        if (isUserHaveCorrectDetails(user))
            createNewUser(user,administrator);
    }

    /**Removes customer its tickets and its user entity*/
    public void removeCustomer(Customers customer) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        GenericDAO<Customers> customersDAO = getCustomersDAO();
        GenericDAO<Tickets> ticketsDAO = getTicketsDAO();
        GenericDAO<Users> usersDAO = getUsersDAO();
        if (customer.getId()==null)
            System.out.println("Id must be provided inside the customer. No removal was made in the DataBase");
        else {
                getTicketsByCustomer(customer).stream()
                        .filter(dao -> !ticketsDAO.remove(dao)) //do removal but only keep streaming failures
                        .forEach(ticket -> System.out.println("Remove all other connections first on ticket: " + ticket));
                if(customersDAO.remove(customer))
                    System.out.println("Remove all other connections first on Customer: " + customer);
                if(!usersDAO.remove(usersDAO.getById(customer.getUserId())))
                    System.out.println("Remove all other connections first on User: " + usersDAO.getById(customer.getUserId()));
                customersDAO.closeAllDAOConnections();
                usersDAO.closeAllDAOConnections();
                ticketsDAO.closeAllDAOConnections();
        }
        customersDAO.closeAllDAOConnections();
        usersDAO.closeAllDAOConnections();
        ticketsDAO.closeAllDAOConnections();
    }

    /**Removes Airline its flights its tickets and its user entity*/
    public void removeAirline(AirlineCompanies airlineCompany) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = getAirlineCompaniesDAO();
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        GenericDAO<Tickets> ticketsDAO = getTicketsDAO();
        GenericDAO<Users> usersDAO = getUsersDAO();
        if (airlineCompany.getId()==null)
            System.out.println("Id must be provided inside the airlineCompany. No removal was made in the DataBase");
        else {
                getFlightsByAirlineCompany(airlineCompany)
                    .stream()
                    .map(this::getTicketsByFlight)
                    .flatMap(List::stream)
                    .filter(dao -> !ticketsDAO.remove(dao))
                    .forEach(ticket -> System.out.println("Remove all other connections first on ticket: " + ticket));
                getFlightsByAirlineCompany(airlineCompany)
                        .stream()
                        .filter(dao -> !flightsDAO.remove(dao))
                        .forEach(flight -> System.out.println("Remove all other connections first on Flight: " + flight));
                if(!airlineCompaniesDAO.remove(airlineCompany))
                    System.out.println("Remove all other connections first on Airline: " + airlineCompany);
                if(!usersDAO.remove(usersDAO.getById(airlineCompany.getUserId())))
                    System.out.println("Remove all other connections first on User: " + usersDAO.getById(airlineCompany.getUserId()));
                flightsDAO.closeAllDAOConnections();
                airlineCompaniesDAO.closeAllDAOConnections();
                usersDAO.closeAllDAOConnections();
            }
        flightsDAO.closeAllDAOConnections();
        airlineCompaniesDAO.closeAllDAOConnections();
        usersDAO.closeAllDAOConnections();
    }
    /**Removes Administrator and its user entity*/
    public void removeAdministrator(Administrators administrator) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        GenericDAO<Administrators> administratorsDAO = getAdministratorsDAO();
        GenericDAO<Users> usersDAO = getUsersDAO();
        if (administrator.getId()==null)
            System.out.println("Id must be provided inside the administrator. No removal was made in the DataBase");
        else {
                if(!administratorsDAO.remove(administrator))
                    System.out.println("Remove all other connections first on Administrator: " + administrator);
                if(!usersDAO.remove(usersDAO.getById(administrator.getUserId())))
                    System.out.println("Remove all other connections first on User: " + usersDAO.getById(administrator.getUserId()));
                administratorsDAO.closeAllDAOConnections();
                usersDAO.closeAllDAOConnections();
            }
        usersDAO.closeAllDAOConnections();
        administratorsDAO.closeAllDAOConnections();
    }
}
