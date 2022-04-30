package com.example.FlightSystemsSpring.controllers.sequrity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.FlightSystemsSpring.dao.GenericDAO;
import com.example.FlightSystemsSpring.entities.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.example.FlightSystemsSpring.dao.GenericDAO.getUsersDAO;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException
    {
        GenericDAO<Users> usersDAO =getUsersDAO();
        String id ="";
        String roleName ="";
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Users", List.of("Id", "Username","Password"));
        tablesToColumnsMap.put("User_Roles", List.of("Role_Name"));

        ResultSet joined = usersDAO.joinTwoByWithWhereClauseGetResultSet(tablesToColumnsMap,"User_Role","User_Roles","Id","WHERE \"Users\".\"Username\" = '"+username+"'");
        try {
            joined.next();
            id=joined.getString("Id");
            roleName=joined.getString("Role_Name");
        } catch (SQLException e) {
            throw new UsernameNotFoundException("Could not findUser with username = " + username);
        }

        return JWT.create()
                .withSubject("User Details")
                .withClaim("Username", username)
                .withClaim("Id",id)
                .withClaim("Role_Name",roleName)
                .withIssuedAt(new Date())
                .withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
                .sign(Algorithm.HMAC256(secret));
    }

//    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
//        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
//                .withSubject("User Details")
//                .withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
//                .build();
//        DecodedJWT jwt = verifier.verify(token);
//
//        return jwt.getClaim("username").asString();
//    }
    public ArrayList<String> validateTokenAndRetrieveSubject(String token)throws JWTVerificationException
    {
        ArrayList<String> credentials = new ArrayList<>();
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        credentials.add(jwt.getClaim("Username").asString());
        credentials.add(jwt.getClaim("Id").asString());
        credentials.add(jwt.getClaim("Role_Name").asString());
        return credentials;
    }
}
