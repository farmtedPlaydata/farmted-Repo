package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.feignClient.MemberFeignClient;
import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.MemberVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Member-Service 테스트 코드")
class MemberServiceTest {
    private final MemberFeignClient memberFeignClient = mock(MemberFeignClient.class);
    private final FeignConverter<MemberVo> memberConverter;

    @Autowired
    public MemberServiceTest(FeignConverter<MemberVo> memberConverter) {
        this.memberConverter = memberConverter;
    }

    private MemberService memberService;

    // mock으로 받을 객체값
    private static final MemberVo memberDetail = new MemberVo(
            "member-name", "profile-url");

    @BeforeEach
    void setUp(){
        memberService = new MemberService(memberFeignClient, memberConverter);
    }

    @Test
    @DisplayName("회원 정보 받기 - 글 작성시")
    void getMemberInfo() {
        // given
        String memberUuid = "uuid";
        when(memberFeignClient.getMemberInfo(memberUuid))
                .thenReturn(ResponseEntity.ok(
                        GlobalResponseDto.of(memberDetail)));

        // when
        MemberVo memberInfo = memberService.getMemberInfo(memberUuid);

        // then
        assertThat(memberInfo.toString()).isEqualTo(memberDetail.toString());
    }
}