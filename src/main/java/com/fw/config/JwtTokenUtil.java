package com.fw.config;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fw.domain.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenUtil{
	public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
	
	public String getUserroleFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
}

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Constants.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Users user) {
    	
        return doGenerateToken(user.getUserName(), user.getUserRole().toDbString());
    }

    private String doGenerateToken(String subject, String userRole) {

//        Claims claims = Jwts.claims().setSubject(subject);
//        Claims claims2 = Jwts.claims().setId(userRole);
        //claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        return Jwts.builder()
        		.setSubject(subject)
        		.setId(userRole)
                .setIssuer("http://mLogistics.biz")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final String userrole = getUserroleFromToken(token);
        boolean result = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        if(userDetails instanceof CustomUser) {
        	CustomUser user = (CustomUser)userDetails;
        	result = result && (userrole.equals(user.getUserrole()) && !isTokenExpired(token)); 
        }
        return result;
    }
}
