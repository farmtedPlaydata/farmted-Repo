package com.farmted.boardservice.domain;


import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.enums.BoardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Table(name = "boards")
@Getter
@AllArgsConstructor
@NoArgsConstructor  //JPA쪽에선 Default 생성자 필요
public class Board extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 경매, 고객센터, 공지사항
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String boardContent;

    @Column
    private String productImage;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private boolean boardStatus;

    @Column(nullable = false)
    private String memberName;

    @Column
    private String memberProfile;

    // member-service와 통신용 FK
    @Column(nullable = false)
    private String memberUuid;

    // 다른 MS와 통신하기 위한 UUID
    @Column(nullable = false, updatable = false, unique = true)
    private String boardUuid;

    @Version// 낙관적 락에서 정합성을 맞추기 위해서 추가하는 필드
    private Long version; //버전 어노테이션이 붙은 필드 하나 더 선언

    ////////////////////////////////////////////////
    // 엔티티 최초 생성 시 자동 초기화할 값
    // UUID, Status, viewCount
    @PrePersist
    private void createUuid(){
        boardUuid = UUID.randomUUID().toString();
        boardStatus = true;
        viewCount = 0;
    }
    ////////////////////////////////////////////////
    // 조회수 상승 로직
    public void increaseViewCount() {viewCount += 1;}
    // 게시글 비활성화 로직
    public void deactiveStatus(){
        boardStatus = false;
    }
    // 게시글 업데이트 로직
    public void updateBoardInfo(RequestUpdateProductBoardDto updateDTO){
        boardType = updateDTO.boardType();
        boardTitle = updateDTO.boardTitle();
        boardContent = updateDTO.boardContent();
    }
    // 게시글 사진 관리 로직
    public void imageManager(String imageURL) {productImage = imageURL;}
}
