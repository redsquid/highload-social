package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Account;
import org.example.domain.Registration;
import org.example.service.credentials.CredentialService;
import org.example.service.user.AccountService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AccountService accountService;

    private final CredentialService credentialService;

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UUID create(@RequestBody Registration registration) {
        UUID accountId = accountService.create(registration.getAccount());
        credentialService.bind(registration.getUsername(), registration.getPassword(), accountId);
        return accountId;
    }

    @GetMapping("/user/user-{id}")
    public ResponseEntity<Account> find(@PathVariable("id") UUID id) {
        Account account = accountService.find(id);
        return account == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(account);
    }
}
