package com.example.FlightSystemsSpring.controllers.sequrity;

import com.example.FlightSystemsSpring.dao.GenericDAO;

import com.example.FlightSystemsSpring.entities.Users;

import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.ResultSet;
import java.util.*;

import static com.example.FlightSystemsSpring.dao.GenericDAO.getUsersDAO;

@EnableWebSecurity
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        GenericDAO<Users> usersDAO =getUsersDAO();
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Users", List.of("Id", "Username","Password"));
        tablesToColumnsMap.put("User_Roles", List.of("Role_Name"));

        ResultSet joined = usersDAO.joinTwoByGetResultSet(tablesToColumnsMap,"User_Role","User_Roles","Id");
        while (joined.next())
        {
            auth.inMemoryAuthentication()
                    .withUser(joined.getString("Username"))
                    .password(joined.getString("Password"))
                    .roles(joined.getString("Role_Name"));
        }
    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception
//    {
//        getUsers().parallelStream().forEach(e->
//        {
//            try {
//                auth.inMemoryAuthentication()
//                        .withUser(e.getUsername())
//                        .password(e.getPassword())
//                        .roles("USER");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
//
//        // admin
//        // customer
//        // anonymous
//        // publisher
//        // editor
//        // readers
//    }
    @Bean
    public PasswordEncoder getpassEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/Administrator").hasRole("Administrator")
                .antMatchers("/AirlineCompany").hasAnyRole("Airline_Company","Administrator")
                .antMatchers("/Customer").hasAnyRole("Customer","Administrator")
                .antMatchers("/").permitAll() //allow any user to connect to the page
                .and().formLogin();

    }

}

