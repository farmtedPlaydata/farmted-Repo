package com.farmted.authservice.service;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.dto.request.RequestCreatePassDto;
import com.farmted.authservice.repository.PassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassServiceImpl implements PassService {

    private final PassRepository passRepository;

    @Override
    public void createPass(RequestCreatePassDto dto) {
        Pass pass = dto.toEntity();
        passRepository.save(pass);
    }
}
