package com.savpoint.savpoint.repositories;

import com.savpoint.savpoint.entities.UserGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGameEntity, Long> {

    Optional<UserGameEntity> findByProfile_UserProfileIdAndGame_GameId(Long profileId, Long gameId);

    java.util.List<UserGameEntity> findByProfile_UserProfileId(Long profileId);

    java.util.List<UserGameEntity> findByProfile_UserProfileIdAndFavoriteTrue(Long profileId);
}
