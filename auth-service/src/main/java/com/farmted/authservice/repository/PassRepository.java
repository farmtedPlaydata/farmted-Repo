package com.farmted.authservice.repository;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassRepository extends JpaRepository<Pass, Long> {
    Optional<Pass> findBySocialTypeAndSocialId(SocialType socialType, String id);
    Optional<Pass> findByUuid(String uuid);
}
