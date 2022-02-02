package com.uhg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "v1")
public class UserController {

	@GetMapping(path = "user")
	public Mono<ResponseEntity<String>> getUserDetails() {
		return Mono.just(ResponseEntity.ok("Succes"));
	}
}
