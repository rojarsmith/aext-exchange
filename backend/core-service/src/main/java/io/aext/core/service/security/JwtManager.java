package io.aext.core.service.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rojar
 *
 * @date 2021-06-25
 */
@Slf4j
@Component
public class JwtManager {
	@Value("${service.security.jwt.key.private}")
	private String secretKey = "abcde";
	private Duration expiration = Duration.ofDays(1);

	/**
	 * Generate JWT
	 * 
	 * @param username user name
	 * @return JWT
	 */
	public String generate(String username) {
		Instant expiryDate = Instant.now().plusMillis(expiration.toMillis());

		return Jwts.builder().setSubject(username)
				//
				.setIssuedAt(Date.from(Instant.now()))
				//
				.setExpiration(Date.from(expiryDate))
				//
				.signWith(SignatureAlgorithm.HS512, secretKey)
				//
				.compact();
	}

	/**
	 * parse JWT
	 * 
	 * @param token JWT string
	 * @return return Claims object if success. Failed return null.
	 */
	public Claims parse(String token) {
		if (!StringUtils.hasLength(token)) {
			return null;
		}

		Claims claims = null;
		try {
			claims = Jwts.parser()
					//
					.setSigningKey(secretKey)
					//
					.parseClaimsJws(token)
					//
					.getBody();
		} catch (JwtException e) {
			log.error("token invalied:{}", e.toString());
		}
		return claims;
	}
}
