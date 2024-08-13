package com.dissonance.itit.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Directory {
    INFORMATION("info-posts"), RECRUITMENT("recruit-posts");

    private final String name;
}
