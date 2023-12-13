package com.farmted.passservice.domain;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.SocialType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    public void updateRole(RoleEnums role) {
        this.role = role;
    }
}
