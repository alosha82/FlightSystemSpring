package com.example.FlightSystemsSpring.dao;


import com.example.FlightSystemsSpring.entities.*;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

import static com.example.FlightSystemsSpring.dao.GenericDAO.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericDAOTest {

    @Test
    void getAll()
    {
        ArrayList<UserRoles> userRoles;
        GenericDAO<UserRoles> userRolesDAO= getUserRolesDAO();
        userRoles = userRolesDAO.getAll();
        ArrayList<UserRoles> myUserRoles=new ArrayList<>();
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        myUserRoles.add(userRolesForTest);
        val userRolesForTest1 = new UserRoles();
        userRolesForTest1.setId(2);
        userRolesForTest1.setRoleName("Airline_Company");
        myUserRoles.add(userRolesForTest1);
        val userRolesForTest2 = new UserRoles();
        userRolesForTest2.setId(3);
        userRolesForTest2.setRoleName("Customer");
        myUserRoles.add(userRolesForTest2);
        assertEquals(userRoles,myUserRoles);
    }

    @Test
    void runSQLFunction()
    {
        ArrayList<Users> users= new ArrayList<>();
        ArrayList<Users> testUsers=new ArrayList<>();
        GenericDAO<Users> usersDAO = getUsersDAO();
        Users userForTest = new Users();
        userForTest.setId(1);
        userForTest.setUserName("someusername");
        userForTest.setPassword("12345");
        userForTest.setEmail("a@b");
        userForTest.setUserRole(1);
//        usersDAO.add(userForTest);
        testUsers.add(userForTest);
        userForTest=new Users(usersDAO.runSQLFunction("get_user_by_username",List.of("\'someusername\'"),new Users()).get(0));
        users.add(userForTest);
        assertEquals(users,testUsers);
    }

    @Test
    void getById()
    {
        UserRoles userRoles;
        GenericDAO<UserRoles> userRolesDAO= getUserRolesDAO();
        userRoles = userRolesDAO.getById(1);
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        System.out.println(userRoles);
        System.out.println(userRolesForTest);
        assertEquals(userRoles,userRolesForTest);
    }

    @Test
    void getByFieldType()
    {
        UserRoles userRoles;
        GenericDAO<UserRoles> userRolesDAO= getUserRolesDAO();
        userRoles = userRolesDAO.getByFieldType("\'Administrator\'","Role_Name");
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        assertEquals(userRoles,userRolesForTest);
    }

    @Test
    void getByFieldTypeArr()
    {
        ArrayList<UserRoles> userRoles;
        GenericDAO<UserRoles> userRolesDAO= getUserRolesDAO();
        userRoles = userRolesDAO.getByFieldTypeArr("\'Administrator\'","Role_Name");
        ArrayList<UserRoles> myUserRoles=new ArrayList<>();
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        myUserRoles.add(userRolesForTest);
        assertEquals(userRoles,myUserRoles);
    }

    @Test
    @SneakyThrows
    void remove()
    {
        GenericDAO<Users> usersDAO=getUsersDAO();
        Users newUserToAdd1 = new Users();
        newUserToAdd1.setId(4);
        newUserToAdd1.setUserName("createfromnewuser1");
        newUserToAdd1.setPassword("11111111");
        newUserToAdd1.setEmail("u@t1");
        newUserToAdd1.setUserRole(1);
        Users newUserToAdd2 = new Users();
        newUserToAdd2.setId(8);
        newUserToAdd2.setUserName("createfromnewuser2");
        newUserToAdd2.setPassword("11111112");
        newUserToAdd2.setEmail("u@t2");
        newUserToAdd2.setUserRole(2);
        Users newUserToAdd3 = new Users();
        newUserToAdd3.setId(9);
        newUserToAdd3.setUserName("createfromnewuser3");
        newUserToAdd3.setPassword("11111113");
        newUserToAdd3.setEmail("u@t3");
        newUserToAdd3.setUserRole(3);
        usersDAO.remove(newUserToAdd1);
        usersDAO.remove(newUserToAdd2);
        usersDAO.remove(newUserToAdd3);
    }

    @Test
    void add()
    {
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2022-03-26 23:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2022-03-26 12:10:25"));
        flightsForTest.setRemainingTickets(100);
        GenericDAO<Flights> flightsDAO=getFlightsDAO();
        flightsDAO.add(flightsForTest);
    }

    @Test
    void update()
    {
        Users newUserToAdd3 = new Users();
        GenericDAO<Users> x =getUsersDAO();
        newUserToAdd3.setId(12);
        newUserToAdd3.setUserName("createfromnewuser3");
        newUserToAdd3.setPassword("11111114");
        newUserToAdd3.setEmail("u@t3");
        newUserToAdd3.setUserRole(3);
        x.update(newUserToAdd3,newUserToAdd3.getId());

        assertEquals(x.getById(12),newUserToAdd3);

    }

    @Test
    void generateJoinMultipleByQuery()
    {
        GenericDAO<Flights> t =getFlightsDAO();
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Customers", List.of("First_Name", "Last_Name"));
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new LinkedHashMap<>();
        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
        foreignsToOrigins.put(new Pair<>("Customers", "Id"),new Pair<>("Tickets", "Customer_Id"));
        String generatedString = t.generateJoinMultipleByQuery(tablesToColumnsMap,"Flights",foreignsToOrigins,"");
        String stringForTest = "SELECT \"Customers\".\"First_Name\",\"Customers\".\"Last_Name\" FROM \"Flights\" JOIN \"Tickets\" ON \"Tickets\".\"Flight_Id\"=\"Flights\".\"Id\" JOIN \"Customers\" ON \"Customers\".\"Id\"=\"Tickets\".\"Customer_Id\" ";
        System.out.println(generatedString);
        assertEquals(generatedString,stringForTest);
    }

    @Test
    void joinTwoBy()
    {
        ArrayList<Flights> flights;
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Flights", List.of("Id", "Airline_Company_Id",
                "Origin_Country_Id","Destination_Country_Id",
                "Departure_Time","Landing_Time","Remaining_Tickets"));
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        flights=flightsDAO.joinTwoBy(tablesToColumnsMap,"Origin_Country_Id","Countries","Id",new Flights());
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void joinTwoByWithWhereClause()
    {
        ArrayList<Flights> flights;
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Flights", List.of("Id", "Airline_Company_Id",
                "Origin_Country_Id","Destination_Country_Id",
                "Departure_Time","Landing_Time","Remaining_Tickets"));
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        flights=flightsDAO.joinTwoByWithWhereClause(tablesToColumnsMap,"Origin_Country_Id",
                "Countries","Id",new Flights(),"WHERE \"Countries\".\"Id\"="+1);
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);

    }

    @Test
    void joinMultipleBy()
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Flights", List.of("Id", "Airline_Company_Id",
                "Origin_Country_Id","Destination_Country_Id",
                "Departure_Time","Landing_Time","Remaining_Tickets"));
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new LinkedHashMap<>();
        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
        foreignsToOrigins.put(new Pair<>("Customers", "Id"),new Pair<>("Tickets", "Customer_Id"));
        flights=flightsDAO.joinMultipleBy(tablesToColumnsMap,"Flights",foreignsToOrigins,new Flights());
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void joinMultipleByWithWhereClause()
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = getFlightsDAO();
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Flights", List.of("Id", "Airline_Company_Id",
                "Origin_Country_Id","Destination_Country_Id",
                "Departure_Time","Landing_Time","Remaining_Tickets"));
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new LinkedHashMap<>();
        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
        foreignsToOrigins.put(new Pair<>("Customers", "Id"),new Pair<>("Tickets", "Customer_Id"));
        flights=flightsDAO.joinMultipleByWithWhereClause(tablesToColumnsMap,"Flights",
                foreignsToOrigins,new Flights(),"WHERE \"Customers\".\"Id\"="+1);
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }


}