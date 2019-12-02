package com.daryl.core;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

public class JwtHelper {
    public static byte[] jwtSecret;

    public static String createAuthToken(int id){
        final JwtClaims claims = new JwtClaims();
        claims.setClaim("id", id);
        claims.setExpirationTimeMinutesInTheFuture(60);
        return createToken(claims);
    }

    private static String createToken(JwtClaims claims){
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(jwtSecret));

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e){
            return null;
        }
    }
}
