package com.savpoint.savpoint.repositories;

import com.savpoint.savpoint.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    
    List<GameEntity> findTop3ByTitleLikeIgnoreCase(String title);

}
