package com.example.FlightSystemsSpring.controllers.sequrity;

import com.example.FlightSystemsSpring.dao.GenericDAO;

import com.example.FlightSystemsSpring.entities.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.util.*;

import static com.example.FlightSystemsSpring.dao.GenericDAO.getUsersDAO;

@EnableWebSecurity
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Autowired private UserRepo userRepo;
    @Autowired private JWTFilter filter;
    @Autowired private MyUserDetailsService uds;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception
//    {
//        GenericDAO<Users> usersDAO =getUsersDAO();
//        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
//        tablesToColumnsMap.put("Users", List.of("Id", "Username","Password"));
//        tablesToColumnsMap.put("User_Roles", List.of("Role_Name"));
//
//        ResultSet joined = usersDAO.joinTwoByGetResultSet(tablesToColumnsMap,"User_Role","User_Roles","Id");
//        while (joined.next())
//        {
//            auth.inMemoryAuthentication()
//                    .withUser(joined.getString("Username"))
//                    .password(joined.getString("Password"))
//                    .roles(joined.getString("Role_Name"));
//        }
//    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    public PasswordEncoder getpassEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/Administrator/authenticate").permitAll()
                .antMatchers("/AirlineCompany/authenticate").permitAll()
                .antMatchers("/Customer/authenticate").permitAll()
                .antMatchers("/Administrator/**").hasRole("Administrator")
                .antMatchers("/AirlineCompany/**").hasAnyRole("Airline_Company")
                .antMatchers("/Customer/**").hasAnyRole("Customer")
                .antMatchers("/").permitAll() //allow any user to connect to the page
                .and()
                .userDetailsService(uds)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        .fromlogin

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

