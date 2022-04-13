package com.example.FlightSystemsSpring.Facades;

import com.example.FlightSystemsSpring.dao.GenericDAO;
import com.example.FlightSystemsSpring.entities.Administrators;
import com.example.FlightSystemsSpring.entities.AirlineCompanies;
import com.example.FlightSystemsSpring.entities.Customers;
import com.example.FlightSystemsSpring.entities.Users;
import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.FlightSystemsSpring.dao.GenericDAO.*;

public class AnonymousFacade extends FacadeBase
{
    /**Joins Users with User_Roles and filters the joined entity by username and password,
     * then decides what facade to return to the caller dy the role that was acquainted through joining*/
    @SneakyThrows
    public AnonymousFacade login(String username,String password) throws Exception
    {
        GenericDAO<Users> usersDAO =getUsersDAO();
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Users", List.of("Id", "Username","Password"));
        tablesToColumnsMap.put("User_Roles", List.of("Role_Name"));

        ResultSet joined = usersDAO.joinTwoByWithWhereClauseGetResultSet(tablesToColumnsMap,"User_Role","User_Roles","Id"
                , "WHERE \"Users\".\"Username\"=\'"+username+"\' AND \"Users\".\"Password\"=\'"+password+"\'");
        if (joined.next())
        {
            LoginToken loginToken = new LoginToken();
            loginToken.setName(username);
            loginToken.setRole(joined.getString("Role_Name"));
            long userId = joined.getLong("Id");
            String role = joined.getString("Role_Name");
            joined.close();
            switch(role)
            {
                case("Administrator")->
                        {
                            GenericDAO<Administrators> administratorsDAO=getAdministratorsDAO();
                            loginToken.setId(administratorsDAO.getByFieldType(""+userId,"User_Id").getId());
                            return new AdministratorFacade(loginToken);
                        }
                case("Customer")->
                        {
                            GenericDAO<Customers> customersDAO=getCustomersDAO();
                            loginToken.setId(customersDAO.getByFieldType(""+userId,"User_Id").getId());
                            return new CustomerFacade(loginToken);
                        }
                case("Airline_Company")->
                        {
                            GenericDAO<AirlineCompanies> airlineCompaniesDAO=getAirlineCompaniesDAO();
                            loginToken.setId(airlineCompaniesDAO.getByFieldType(""+userId,"User_Id").getId());
                            return new AirlineFacade(loginToken);
                        }
                default -> throw new Exception("Unknown role name in the database.");
            }
        }
        else
        {
            throw new Exception("No Such username or password found in database.");
        }
    }

    /**Adds customer and its user entity*/
    public void addCustomer(Users user, Customers customer)throws Exception
    {
        String password= user.getPassword();
        if(password.contains(" "))
            throw new Exception("Spaces are not allowed in the password.");
        if(password.length()<7)
            throw new Exception("Your password is to short. Can not add such a user with that kind of insecure password to the database.");
        if((user.getEmail().charAt(0) == '@')||(user.getEmail().charAt(0) == ' ') || !(user.getEmail().contains("@")))
            throw new Exception("Invalid Email.");
        createNewUser(user,customer);
    }
}
