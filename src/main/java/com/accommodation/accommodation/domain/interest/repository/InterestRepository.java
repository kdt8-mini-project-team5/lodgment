package com.accommodation.accommodation.domain.interest.repository;

import com.accommodation.accommodation.domain.interest.model.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
}