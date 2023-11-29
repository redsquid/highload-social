package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.Credential;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CredentialRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void saveCredential(String username, String password, UUID accountId) {
        Map<String, Object> p = Map.of(
                "id", UUID.randomUUID(),
                "username", username,
                "password", password,
                "accountId", accountId
        );
        jdbcTemplate.update("insert into credential values (:id, :username, :password, :accountId)", p);
    }

    public Credential findByUsername(String username) {
        return jdbcTemplate.query("select * from credential where username = :username", Map.of("username", username), rs -> {
            if (rs.next()) {
                return new Credential(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("username"),
                        rs.getString("password"),
                        UUID.fromString(rs.getString("account_id"))
                );
            } else {
                return null;
            }
        });
    }

    public void saveToken(UUID credentialId, String token, LocalDateTime expiryDate) {
        Map<String, Object> p = Map.of(
                "id", UUID.randomUUID(),
                "credentialId", credentialId,
                "accessToken", token,
                "expiryDate", expiryDate
        );
        jdbcTemplate.update("insert into token values (:id, :credentialId, :accessToken, :expiryDate)", p);
    }

    public boolean unexpiredTokenIsExist(UUID accountId, String token) {
        String query = "select exists(select 1 as VALUE from credential c join token t on c.id = t.credential_id where " +
                "c.account_id = :accountId and t.access_token = :token and t.expiry_date > :date)";
        Map<String, Object> p = Map.of("accountId", accountId, "token", token, "date", LocalDateTime.now());
        Boolean result = jdbcTemplate.queryForObject(query, p, Boolean.class);
        return result != null && result;
    }
}
