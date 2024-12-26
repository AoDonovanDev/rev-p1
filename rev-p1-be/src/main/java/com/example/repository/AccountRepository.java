package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

    public Optional<Account> findByUsername(String username);

    public Optional<Account> findByAccountId(Integer accountId);
}
