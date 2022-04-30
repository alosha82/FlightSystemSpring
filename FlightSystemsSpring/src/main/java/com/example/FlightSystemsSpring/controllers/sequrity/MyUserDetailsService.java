package com.example.FlightSystemsSpring.controllers.sequrity;

import com.example.FlightSystemsSpring.dao.GenericDAO;
import com.example.FlightSystemsSpring.entities.Users;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.awt.desktop.UserSessionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.example.FlightSystemsSpring.dao.GenericDAO.getUsersDAO;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        GenericDAO<Users> usersDAO =getUsersDAO();
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Users", List.of("Id", "Username","Password"));
        tablesToColumnsMap.put("User_Roles", List.of("Role_Name"));
        String ROLE_PREFIX = "ROLE_";

        ResultSet joined = usersDAO.joinTwoByWithWhereClauseGetResultSet(tablesToColumnsMap,"User_Role","User_Roles","Id","WHERE \"Users\".\"Username\" = '"+username+"'");
        try {
            joined.next();
        } catch (SQLException e) {
            throw new UsernameNotFoundException("Could not findUser with username = " + username);
        }

        try {
            return new org.springframework.security.core.userdetails.User(
                    username,
                    joined.getString("Password"),
                    Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX+joined.getString("Role_Name"))));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Should not happen");
        }
    }
}
