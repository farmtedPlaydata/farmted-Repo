package com.farmted.passservice.service;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.dto.response.ResponseListDto;
import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.exception.PassException;
import com.farmted.passservice.feignclient.MemberFeignClient;
import com.farmted.passservice.repository.PassRepository;
import com.farmted.passservice.util.jwt.JwtProvider;
import com.farmted.passservice.util.redis.RedisRepository;
import com.farmted.passservice.util.redis.RefreshToken;
import com.farmted.passservice.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassServiceImpl implements PassService {

    private final PassRepository passRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisRepository redisRepository;
    private final JwtProvider jwtProvider;
    private final MemberFeignClient memberFeignClient;

    @Override
    @Transactional
    public void createPass(RequestCreatePassDto dto) {
        duplicateUserCheck(dto);
        String password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
        Pass pass = dto.toEntity();
        passRepository.save(pass);
    }

    @Override
    @Transactional
    public String login(RequestLoginDto dto) {
        Pass pass = passRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new PassException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), pass.getPassword()))
            throw new PassException("비밀번호가 일치하지 않습니다.");

        String accessTokenInfo = jwtProvider.createToken(pass.getUuid(), pass.getRole(), TokenType.ACCESS);
        String refreshTokenInfo = jwtProvider.createToken(pass.getUuid(), pass.getRole(), TokenType.REFRESH);

        jwtProvider.saveRefreshToken(pass.getUuid(), refreshTokenInfo);

        log.info("로그인 성공");

        return accessTokenInfo;
    }

    @Override
    @Transactional
    public void logout(String uuid) {
        try {
            Optional<RefreshToken> token = redisRepository.findById(uuid);
            if (token.isPresent()) {
                redisRepository.deleteById(uuid);
            }
        } catch (PassException e){
            throw new PassException("PassService - logout");
        }
    }

    @Override
    public List<ResponseListDto> getPassByAll(int pageNo) {
        Slice<Pass> passList = passRepository.findAll(PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "email")));

        return passList.stream()
                .map(ResponseListDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public String findUuidByEmail(String email) {
        Pass findPass = passRepository.findByEmail(email)
                .orElseThrow(() -> new PassException("PassService - findUuidByEmail"));

        return findPass.getUuid();
    }

    @Override
    public String reIssue(String uuid) {
        redisRepository.findById(uuid)
                .orElseThrow(() -> new PassException("PassService - reIssue : RefreshToken을 찾을 수 없습니다."));

        Pass pass = passRepository.findByUuid(uuid)
                .orElseThrow(() -> new PassException("PassService - reIssue : 사용자를 찾을 수 없습니다."));

        return jwtProvider.createToken(uuid, pass.getRole(), TokenType.ACCESS);
    }

    @Override
    @Transactional
    public void updateRole(MemberVo memberVo) {
        Pass findPass = passRepository.findByUuid(memberVo.getMemberUuid())
                .orElseThrow(() -> new PassException("PassService - memberCreateFeignMemberService"));

        try {
            switch (memberVo.getMemberRole()) {
                case "GUEST":
                    findPass.updateRole(RoleEnums.GUEST);
                    break;
                case "USER":
                    findPass.updateRole(RoleEnums.USER);
                    break;
                case "ADMIN":
                    findPass.updateRole(RoleEnums.ADMIN);
                    break;
                case "MASTER":
                    findPass.updateRole(RoleEnums.MASTER);
                    break;
            }

            passRepository.save(findPass);
            String refreshTokenInfo = jwtProvider.createToken(findPass.getUuid(), findPass.getRole(), TokenType.REFRESH);
            jwtProvider.saveRefreshToken(findPass.getUuid(), refreshTokenInfo);
            reIssue(findPass.getUuid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void changeRoleByMemberService(String uuid) {
        try {
            ResponseEntity<?> re = memberFeignClient.updateRole(uuid);
            Object responseBody = re.getBody();
            if (responseBody instanceof LinkedHashMap<?, ?> responseMap) {
                // data 값 가져오기
                Object dataField = responseMap.get("data");
                RoleEnums role = RoleEnums.fromKey(dataField.toString());
                Pass pass = passRepository.findByUuid(uuid)
                        .orElseThrow(() -> new PassException("passService - changeRoleByMemberService"));
                log.info(role.toString());

                pass.updateRole(role);
                passRepository.save(pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String setRole(RequestLoginDto dto) {
        Pass pass = passRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new PassException("PassService - setRole"));
        return pass.getRole().toString();
    }


    // email 중복 검사
    private void duplicateUserCheck(RequestCreatePassDto dto) {
        Optional<Pass> pass = passRepository.findByEmail(dto.getEmail());

        if(pass.isPresent()) throw new PassException("이미 존재하는 회원입니다.");
    }

}
