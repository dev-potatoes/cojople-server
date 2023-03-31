package io.mojolll.project.v1.api.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestDto {
    private String email;
    private String password;
}
