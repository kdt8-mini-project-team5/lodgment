package com.accommodation.accommodation.domain.auth.repository;

import com.accommodation.accommodation.domain.auth.model.entity.TokenInfo;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenInfo, String> {

    Optional<TokenInfo> findByAccessToken(String accessToken);

}
