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
    public ResponseEntity<AccountResponse> getAccount(@RequestParam("name") String name) {
        return ResponseEntity.ok(accountService.getAccount(name));
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
