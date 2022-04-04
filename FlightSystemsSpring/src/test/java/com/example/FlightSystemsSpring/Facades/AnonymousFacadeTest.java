package com.example.FlightSystemsSpring.Facades;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class AnonymousFacadeTest {

    @Test
    @SneakyThrows
    void login()
    {
        AnonymousFacade anonymousFacade=new AnonymousFacade();
        anonymousFacade.login("createfromnewuser1","11111111");
        anonymousFacade.login("createfromnewuser3","11111114");
        anonymousFacade.login("createfromnewuser2","11111112");
    }
}