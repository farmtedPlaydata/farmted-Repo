package com.farmted.authservice.domain;

import com.farmted.authservice.enums.RoleEnums;
import com.farmted.authservice.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity(name = "auths")
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Auth extends TimeStamp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String authEmail;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String authPassword;

    @Enumerated(EnumType.STRING)
    private RoleEnums authRole;

    private Boolean authStatus;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String imageUrl;

}
