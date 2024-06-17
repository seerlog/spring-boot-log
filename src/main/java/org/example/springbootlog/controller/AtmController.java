package org.example.springbootlog.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootlog.response.AccountResponse;
import org.example.springbootlog.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/atm")
public class AtmController {
    private final AccountService accountService;

    @GetMapping("/{name}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("name") String name) {
        return ResponseEntity.ok(accountService.getAccount(name));
    }
}
