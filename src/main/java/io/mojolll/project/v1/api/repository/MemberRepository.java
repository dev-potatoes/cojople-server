package io.mojolll.project.v1.api.repository;

import io.mojolll.project.v1.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
}
