package com.farmted.authservice.repository;

import com.farmted.authservice.domain.Auth;
import com.farmted.authservice.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findBySocialTypeAndSocialId(SocialType socialType, String id);
}
