package com.eshya.test.utils;


import com.eshya.test.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.function.Function;

@Component
@PropertySource("classpath:application.properties")
public class JwtUtils {
	/*
	 * Reference:
	 * https://www.bezkoder.com/spring-boot-jwt-mysql-spring-security-architecture/
	 * https://www.javainuse.com/spring/boot-jwt
	 * 
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


	public String generateJwtToken(UserDetailsImpl userPrincipal) {
		return generateTokenFromCredential(userPrincipal.getUsername());
	}
	
	

	public String generateTokenFromCredential(String username) {
		return Jwts.builder()
				.setSubject("user_portal_token")
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + Constant.TOKEN_EXPIRY_IN_MS))
				.claim("username", username)
				.signWith(SignatureAlgorithm.HS512, Constant.TOKEN_SECRET).compact();
	}



	public String getUsernameFromToken(String token) {
		logger.info("my token is {}", token);
		Object userId = Jwts.parser().setSigningKey(Constant.TOKEN_SECRET).parseClaimsJws(token).getBody().get("username");
		return (userId == null) ? "" : String.valueOf(userId);
	}

	
	public Date getIssuedAtFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}


	/**
	 * retrieve expiration date from jwt token
	 * 
	 * @param token
	 * @return Date :expiration date
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}


	/**
	 * retrieving any information from token we will need the secret key
	 * 
	 * 
	 * @param token
	 * @return Claims :Additional token information
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(Constant.TOKEN_SECRET).parseClaimsJws(token).getBody();
	}

	/**
	 * check if the token has expired
	 * 
	 * @param token
	 * @return Boolean
	 */
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(Constant.TOKEN_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

}
