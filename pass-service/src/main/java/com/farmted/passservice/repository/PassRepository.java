package com.farmted.passservice.repository;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.enums.SocialType;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassRepository extends JpaRepository<Pass, Long> {
    Optional<Pass> findBySocialTypeAndSocialId(SocialType socialType, String id);
    Optional<Pass> findByUuid(String uuid);
    Optional<Pass> findByEmail(String email);

}

