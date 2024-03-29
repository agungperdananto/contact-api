package com.github.contact.contact.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.contact.contact.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    Optional<User> findFirstByToken(String token);
}
