package com.example.FlightSystemsSpring.logintoken;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LoginToken
{
    private Long id;
    private String name;
    private String role;
}
