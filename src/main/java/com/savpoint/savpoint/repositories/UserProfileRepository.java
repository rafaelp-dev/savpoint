package com.savpoint.savpoint.repositories;

import com.savpoint.savpoint.entities.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByDisplayName(String displayName);

    boolean existsByDisplayName(String displayName);

    Optional<UserProfileEntity> findByUser_Email(String email);
}
