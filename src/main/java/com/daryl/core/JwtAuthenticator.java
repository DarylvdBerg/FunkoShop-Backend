package com.daryl.core;

import com.daryl.FunkoShopApplication;
import com.daryl.api.User;
import com.daryl.db.UserDAO;
import io.dropwizard.auth.Authenticator;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.JwtContext;

import java.util.Optional;


public class JwtAuthenticator implements Authenticator<JwtContext, User> {
    @Override
    public Optional<User> authenticate(JwtContext jwtContext) {
        try {
            final JwtClaims claims = jwtContext.getJwtClaims();
            if(NumericDate.now().isAfter(claims.getExpirationTime())){
                return Optional.empty();
            }

            UserDAO userDAO = FunkoShopApplication.jdbiCon.onDemand(UserDAO.class);
            Long userId = (Long)claims.getClaimValue("id");
            User user = userDAO.getUserFromId(userId.intValue());

            return user == null ? Optional.empty() : Optional.of(user);
        } catch(MalformedClaimException e){
            return Optional.empty();
        }
    }
}
