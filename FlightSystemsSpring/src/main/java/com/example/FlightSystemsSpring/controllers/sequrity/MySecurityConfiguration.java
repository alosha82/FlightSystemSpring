package com.example.FlightSystemsSpring.controllers.sequrity;

import com.example.FlightSystemsSpring.dao.GenericDAO;

import com.example.FlightSystemsSpring.entities.Users;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@EnableWebSecurity
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("1")
//                .password("1")
//                .roles("USER");
//        // admin
//        // customer
//        // anonymous
//        // publisher
//        // editor
//        // readers
//    }

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
//    @Bean
//    public PasswordEncoder getpassEncoder(){
//        return NoOpPasswordEncoder.getInstance();
//    }

    public ArrayList<Users> getUsers()
    {
        GenericDAO<Users> userDAO = new GenericDAO<>("Users",new Users());
        ArrayList<Users> users;
        users=userDAO.getAll();

        return users;
    }

    @Override
    //TODO ask how can I loop antMatchers on all the roles from the data base?
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/Administrator").hasRole("Administrator")
                .antMatchers("/AirlineCompany").hasAnyRole("Airline_Company","Administrator")
                .antMatchers("/Customer").hasAnyRole("Customer","Administrator")
                .antMatchers("/").permitAll() //allow any user to connect to the page
                .and().formLogin();

    }

}

