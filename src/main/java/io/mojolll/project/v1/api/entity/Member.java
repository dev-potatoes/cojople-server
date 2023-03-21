package io.mojolll.project.v1.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "Member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String email;
    private String password;
    private String name;
//    private LocalDateTime createDate;
//    private LocalDateTime loginDate;
//    @ManyToOne
//    @JoinColumn(name = "University_id")
//    private University university;
}
