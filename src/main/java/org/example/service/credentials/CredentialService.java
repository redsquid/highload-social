package org.example.service.credentials;

import lombok.RequiredArgsConstructor;
import org.example.domain.Credential;
import org.example.repository.CredentialRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private static final int LIFETIME_SECONDS = 3600;

    private final CredentialRepository repo;

    public String createToken(String username, String password) {
        Credential c = repo.findByUsername(username);
        if (c == null || !c.getPassword().equals(password)) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        repo.saveToken(c.getId(), token, LocalDateTime.now().plusSeconds(LIFETIME_SECONDS));
        return c.getAccountId() + ":" + token;
    }

    public void bind(String username, String password, UUID accountId) {
        repo.saveCredential(username, password, accountId);
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        String[] data = token.split(":");
        if (data.length != 2) {
            return false;
        }
        try {
            return repo.unexpiredTokenIsExist(UUID.fromString(data[0]), data[1]);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
