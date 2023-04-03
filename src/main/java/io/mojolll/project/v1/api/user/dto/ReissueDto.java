package io.mojolll.project.v1.api.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReissueDto {
    @NotEmpty(message = "accessToken 을 입력해주세요.")
    private String accessToken;

//    @NotEmpty(message = "refreshToken 을 입력해주세요.")
//    private String refreshToken;
}