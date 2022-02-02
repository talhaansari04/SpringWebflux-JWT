package com.uhg.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtilty {
	private final static int EXPIRE_TIME = 60000 * 30;
	private final static String SECRET_KEY = "WEDONTKNOW";

	public String createToken(String userdetails) {
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		String token = Jwts.builder().setId(userdetails)
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()).compact();
		return token;
	}

	public Claims validateToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public String getUsernameFromToken(String token) {
		return validateToken(token).getSubject();
	}
}
