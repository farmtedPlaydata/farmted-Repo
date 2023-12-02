package com.farmted.passservice.domain;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "pass")
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Pass extends TimeStamp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
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
