package com.example.service;

import javax.crypto.SecretKey;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import com.example.entity.Account;
import com.example.exception.AccountInfoException;
import com.example.util.AccountIdStruct;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class JwtService {

    private SecretKey key = Jwts.SIG.HS256.key().build();

    @Autowired
    ObjectMapper om;

    @Autowired
    public JwtService(){}

    public String generateAccountToken(Account account){
        String jws = Jwts.builder()
        .claim("accountId", account.getAccountId())
        .signWith(key).compact();
        return jws;
    }

    public Integer returnAccountIdFromToken(String token) throws JwtException, AccountInfoException{
        try {
            Claims claimsJson = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            Integer accountId = (Integer)claimsJson.get("accountId");
            return accountId;
        } catch(Exception jwtException){
            jwtException.printStackTrace();
        }
        throw new AccountInfoException();
    }

    public AccountIdStruct validateAndRefresh(String token) throws JwtException{
        try {
            Claims claimsJson = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            String accJson = om.writeValueAsString(claimsJson);
            AccountIdStruct ais = om.readValue(accJson, AccountIdStruct.class);
            return ais;
        } catch(Exception err){
            err.printStackTrace();
        }
        throw new JwtException("couldn't validate or refresh token");
    }   
}