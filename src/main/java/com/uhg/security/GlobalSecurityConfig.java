package com.uhg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class GlobalSecurityConfig {

	@Autowired
	private OptumAuthenticationManager authenticationManager;
	@Autowired
	private OptumSecurityContextRepository contextRepository;

	@Bean
	public SecurityWebFilterChain optumSecurityFilterChain(ServerHttpSecurity httpSecurity) {
		return	httpSecurity
					.cors().disable()
					.exceptionHandling()
					.authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
									swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
								})).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
									swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
								}))
		.and()
					.csrf().disable()
					.authenticationManager(authenticationManager)
					.securityContextRepository(contextRepository)
					.authorizeExchange()
							.pathMatchers(HttpMethod.OPTIONS).permitAll().pathMatchers("/login").permitAll().anyExchange()
							.authenticated()
		.and()
		.build();
		
	}

}
