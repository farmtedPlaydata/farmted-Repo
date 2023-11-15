package com.farmted.authservice.domain;

import com.farmted.authservice.enums.RoleEnums;
import com.farmted.authservice.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity(name = "pass")
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Pass extends TimeStamp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnums role;

    private Boolean status;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String imageUrl;

}
