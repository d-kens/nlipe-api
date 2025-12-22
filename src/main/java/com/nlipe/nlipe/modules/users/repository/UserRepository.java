package com.nlipe.nlipe.modules.users.repository;

import com.nlipe.nlipe.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}