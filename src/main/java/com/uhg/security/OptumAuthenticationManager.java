package com.uhg.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class OptumAuthenticationManager implements ReactiveAuthenticationManager {
	private static final Logger LOG = LoggerFactory.getLogger(OptumAuthenticationManager.class);

	@Autowired
	private TokenUtilty tokenUtilty;

	@Override
	public Mono<Authentication> authenticate(Authentication auth) {
		String authToken = auth.getCredentials().toString();
		LOG.info("Token");
		return Mono.empty();
	}

}
