package org.example.springbootlog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootlog.domain.account.Account;
import org.example.springbootlog.domain.account.AccountRepository;
import org.example.springbootlog.request.DepositRequest;
import org.example.springbootlog.response.AccountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;

    public AccountResponse getAccount(String name) {
        Account account = accountRepository.findByName(name).orElseThrow(IllegalArgumentException::new);
        return AccountResponse.builder()
                .name(account.getName())
                .balance(account.getBalance())
                .build();
    }

    public void createAccount(String name) {
        accountRepository.save(Account.empty(name));
        log.info("Account created: {}", name);
    }

    public void deposit(DepositRequest request) {
        Account account = accountRepository.findByName(request.getName()).orElse(Account.empty(request.getName()));
        account.setBalance(account.getBalance() + request.getAmount());
        accountRepository.save(account);
    }
}
