package com.farmted.boardservice.domain;


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
public class Board extends TimeStamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 경매, 고객센터, 공지사항
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardContent;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private boolean boardStatus;

    // member-service와 통신용 FK
    @Column(nullable = false)
    private String memberUuid;

    // 다른 MS와 통신하기 위한 UUID
    @Column(nullable = false, updatable = false, unique = true)
    private String boardUuID;

    ////////////////////////////////////////////////
    // 엔티티 최초 생성 시 자동 초기화할 값
        // UUID, Status, viewCount
    @PrePersist
    public void createUuid(){
        boardUuID = UUID.randomUUID().toString();
        boardStatus = true;
        viewCount = 0;
    }
    ////////////////////////////////////////////////
    // 게시글 비활성화 로직
    public void deactiveStatus(){
        boardStatus = false;
    }
    public void activeStatus(){
        boardStatus = true;
    }
}
