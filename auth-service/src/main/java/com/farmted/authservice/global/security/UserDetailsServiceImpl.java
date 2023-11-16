package com.farmted.authservice.global.security;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.repository.PassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PassRepository passRepository;

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        Pass pass = passRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습닌다."));

        return new UserDetailsImpl(pass, pass.getEmail(), pass.getUuid());
    }
}
