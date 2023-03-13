package vn.chef.template.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.stereotype.Component;
import vn.chef.template.domain.UserPrincipal;
import  vn.chef.template.utils.Constants;

import java.util.Date;



@Component
public class JwtTokenProvider {
    public String generateTokenWithPrinciple(UserPrincipal userPrincipal) {
        return generateToken(userPrincipal);
    }
    public String generateToken(UserPrincipal userPrincipal) {
        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(Constants.SECRET.getBytes()));
    }

    public String getUserNameFromJWT(String token) {
        return JWT.require(Algorithm.HMAC256(Constants.SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC256(Constants.SECRET.getBytes())).build().verify(authToken);
            return true;
        } catch (JWTVerificationException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }

}
