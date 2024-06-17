package org.example.springbootlog.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootlog.domain.account.Account;
import org.example.springbootlog.domain.account.AccountRepository;
import org.example.springbootlog.response.AccountResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountResponse getAccount(String name) {
        Account account = accountRepository.findByName(name).orElse(Account.empty(name));
        return AccountResponse.builder()
                .name(account.getName())
                .balance(account.getBalance())
                .build();
    }
}
