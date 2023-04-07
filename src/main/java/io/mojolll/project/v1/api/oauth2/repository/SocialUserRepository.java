package io.mojolll.project.v1.api.oauth2.repository;

import io.mojolll.project.v1.api.oauth2.model.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser,Long> {
    Optional<SocialUser> findByEmail(String email);
    Optional<SocialUser> findByUsername(String username);
    boolean existsByEmail(String email);
}
