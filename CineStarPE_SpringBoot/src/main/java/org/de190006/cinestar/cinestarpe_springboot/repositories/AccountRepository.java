package org.de190006.cinestar.cinestarpe_springboot.repositories;

import org.de190006.cinestar.cinestarpe_springboot.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmailAndPassword(String email, String password);
}