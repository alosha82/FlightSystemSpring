package com.example.FlightSystemsSpring.logintoken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginToken
{
    private Long id;
    private String name;
    private String role;
}
