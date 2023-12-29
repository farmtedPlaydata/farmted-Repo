package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.feignClient.MemberFeignClient;
import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.util.FeignConverter;
import com.farmted.boardservice.vo.MemberVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {FeignConverter.class, MemberService.class, MemberFeignClient.class})
@DisplayName("Member-Service 테스트 코드")
class MemberServiceTest {
    @Mock
    private MemberFeignClient memberFeignClient;
    @Spy
    private FeignConverter<MemberVo> memberConverter;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 정보 받기 - 글 작성시")
    void getMemberInfo() {
        // given
        String memberUuid = "uuid";
        MemberVo memberDetail =
                new MemberVo("member-name", "profile-url");
        when(memberFeignClient.getMemberInfo(memberUuid))
                .thenReturn(ResponseEntity.ok(
                        GlobalResponseDto.of(memberDetail)));

        // when
        MemberVo memberInfo = memberService.getMemberInfo(memberUuid);

        // then
        assertThat(memberInfo.toString()).isEqualTo(memberDetail.toString());
    }
}