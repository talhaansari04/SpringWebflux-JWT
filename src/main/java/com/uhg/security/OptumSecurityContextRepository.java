package com.uhg.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class OptumSecurityContextRepository implements ServerSecurityContextRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(OptumSecurityContextRepository.class);

	@Autowired
	private OptumAuthenticationManager authenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		ServerHttpRequest req = exchange.getRequest();
		String token = req.getHeaders().getFirst("token");
		Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
		if (null != token) {
			return this.authenticationManager.authenticate(auth).map((manager) -> new SecurityContextImpl(manager));
		}

		return Mono.empty();
	}

}
