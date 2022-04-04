package com.example.FlightSystemsSpring.Facades;

import com.example.FlightSystemsSpring.logintoken.LoginToken;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class AdministratorFacadeTest {

    @Test
    @SneakyThrows
    void getAllCustomers()
    {
        LoginToken loginToken =new LoginToken();
        loginToken.setId(3l);
        loginToken.setName("createfromnewuseradministratorrole");
        loginToken.setRole("Administrator");
        AdministratorFacade administratorFacade= new AdministratorFacade(loginToken);
        System.out.println(administratorFacade.getAllCustomers());
    }
}