package com.farmted.productservice.domain;

import com.farmted.productservice.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends TimeStamp{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String title;

    private String message;

    private boolean checked;

    public void read() {
        this.checked = true;
    }

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; //  알림 유형은 상품 등록, 경매 결과 값

}
