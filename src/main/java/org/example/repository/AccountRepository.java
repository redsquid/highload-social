package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.Sex;
import org.example.domain.Account;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Account insert(Account account) {
        if (account.getId() != null) {
            throw new IllegalStateException("Can't save user. Id must be null");
        }
        account.setId(UUID.randomUUID());
        Map<String, Object> p = Map.of(
                "id", account.getId() ,
                "first_name", account.getFirstName(),
                "last_name", account.getLastName(),
                "birth_date", account.getBirthDate(),
                "sex", account.getSex().toString(),
                "city", account.getCity(),
                "interests", account.getInterests()
        );
        jdbcTemplate.update("insert into account values (:id, :first_name, :last_name, :birth_date, :sex, :city, :interests)", p);
        return account;
    }

    public Account findById(UUID id) {
        return jdbcTemplate.query("select * from account where id = :id", Map.of("id", id), rs -> {
            if (rs.next()) {
                Account u = new Account();
                u.setId(UUID.fromString(rs.getString("id")));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setBirthDate(rs.getDate("birth_date").toLocalDate());
                u.setSex(Sex.valueOf(rs.getString("sex")));
                u.setCity(rs.getString("city"));
                u.setInterests(rs.getString("interests"));
                return u;
            } else {
                return null;
            }
        });
    }
}
