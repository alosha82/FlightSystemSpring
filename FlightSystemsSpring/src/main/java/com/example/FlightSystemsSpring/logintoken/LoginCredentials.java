package com.example.FlightSystemsSpring.logintoken;

import lombok.*;
/** For jwt token generation*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginCredentials {

    private String username;
    private String password;

}
