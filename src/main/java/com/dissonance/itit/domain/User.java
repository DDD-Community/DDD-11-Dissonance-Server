package com.dissonance.itit.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "password")
    private String password;

    @Size(max = 50)
    @NotNull
    @Column(name = "name")
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role")
    private Role role;

    @Column(name = "provider_id")
    private String providerId;

    @Size(max = 50)
    @NotNull
    @Column(name = "provider")
    private String provider;

    @Size(max = 255)
    @Column(name = "profile_img_url")
    private String profileImgUrl;
}