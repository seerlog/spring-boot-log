package org.example.springbootlog.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootlog.request.DepositRequest;
import org.example.springbootlog.response.AccountResponse;
import org.example.springbootlog.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/atm")
public class AtmController {
    private final AccountService accountService;

    @GetMapping
    public String test() {
        return "Hello World!";
    }

    @GetMapping("/{name}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("name") String name) {
        return ResponseEntity.ok().body(AccountResponse.builder().name(name).balance(0L).build());
    }

    @PostMapping("/{name}")
    public ResponseEntity<Void> createAccount(@PathVariable("name") String name) {
        accountService.createAccount(name);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody DepositRequest request) {
        accountService.deposit(request);
        return ResponseEntity.ok().build();
    }
}
