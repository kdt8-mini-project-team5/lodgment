package com.accommodation.accommodation.domain.auth.repository;

import com.accommodation.accommodation.domain.auth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}