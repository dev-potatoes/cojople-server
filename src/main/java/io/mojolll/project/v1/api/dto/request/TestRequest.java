package io.mojolll.project.v1.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TestRequest {
    private String title;
    private String content;
    private LocalDateTime createDate;
}