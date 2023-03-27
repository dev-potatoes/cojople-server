package io.mojolll.project.v1.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
//    private String name;


    @Enumerated(EnumType.STRING)
    private UserRole role;
////    private LocalDateTime createDate;
//    @ManyToOne
//    @JoinColumn(name = "University_id")
//    private University university;
//    private LocalDateTime loginDate;
}
