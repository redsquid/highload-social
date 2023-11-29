package org.example.service.user;

import lombok.RequiredArgsConstructor;
import org.example.domain.Account;
import org.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repo;

    public UUID create(Account account) {
        return repo.insert(account).getId();
    }

    public Account find(UUID accountId) {
        return repo.findById(accountId);
    }
}
