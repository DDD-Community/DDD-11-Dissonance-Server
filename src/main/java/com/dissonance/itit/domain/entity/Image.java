package com.dissonance.itit.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 500)
    @NotNull
    @Column(name = "image_url")
    private String imageUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "convert_image_name")
    private String convertImageName;

    @Size(max = 20)
    @NotNull
    @Column(name = "directory")
    private String directory;
}