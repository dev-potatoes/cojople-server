package io.mojolll.project.v1.api.dto.request;

//import io.mojolll.project.v1.api.entity.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class MemberJoinRequest {
//    private String email;
    private String password;
    private String name;
//    private LocalDateTime createDate;
//    private LocalDateTime loginDate;
//    private University university;
}
