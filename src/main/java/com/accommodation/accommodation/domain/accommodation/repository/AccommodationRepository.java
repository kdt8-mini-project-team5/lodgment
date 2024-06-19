package com.accommodation.accommodation.domain.accommodation.repository;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}