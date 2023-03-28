package io.mojolll.project.v1.api.repositroy;

import io.mojolll.project.v1.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);

}
