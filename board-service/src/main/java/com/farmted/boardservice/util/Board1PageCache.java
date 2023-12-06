package com.farmted.boardservice.util;

import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// 메인 페이지인 상품(판매+경매)의 1페이지를 가지는 캐싱 코드
public class Board1PageCache {
    private Page<ResponseGetBoardDto> page1;
    private final BoardRepository boardRepository;

    // yml에 cron 설정 저장
    @Scheduled(cron = "${schedules.cron}")
    private void updateCache(){
        page1 = boardRepository.findByBoardType(BoardType.PRODUCT,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        System.out.println("@@@@ 페이징 캐싱 완료! : 콘텐츠 갯수 - "+page1.getContent().size());
    }
    public Page<ResponseGetBoardDto> getPage1(){
        return page1;
    }
}