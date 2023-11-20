package com.farmted.passservice.global.security;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.repository.PassRepository;
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
        Pass checkPass = passRepository.findByUuid(uuid).orElse(null);
        Pass pass = passRepository.findByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new UserDetailsImpl(pass, pass.getEmail(), pass.getUuid());
    }
}
