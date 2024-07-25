package com.dissonance.itit.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "info_post")
public class InfoPost extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title")
    private String title;

    @Size(max = 1000)
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "view_count")
    private Integer viewCount;

    @NotNull
    @Column(name = "recruitment_start_date")
    private LocalDateTime recruitmentStartDate;

    @NotNull
    @Column(name = "recruitment_end_date")
    private LocalDateTime recruitmentEndDate;

    @NotNull
    @Column(name = "activity_start_date")
    private LocalDateTime activityStartDate;

    @NotNull
    @Column(name = "activity_end_date")
    private LocalDateTime activityEndDate;

    @Size(max = 500)
    @NotNull
    @Column(name = "detail_url")
    private String detailUrl;

    @Size(max = 50)
    @NotNull
    @Column(name = "organization")
    private String organization;

    @NotNull
    @Column(name = "recommended")
    private Boolean recommended = false;

    @NotNull
    @Column(name = "reported")
    private Boolean reported = false;

    @NotNull
    @Column(name = "recruitment_closed")
    private Boolean recruitmentClosed = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}