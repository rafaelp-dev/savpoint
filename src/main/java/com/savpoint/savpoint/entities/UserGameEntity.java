package com.savpoint.savpoint.entities;

import com.savpoint.savpoint.enums.GameStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_game")
@Getter
@Setter
public class UserGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGameId;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfileEntity profile;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(nullable = false)
    private Boolean favorite;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT",  nullable = true)
    private String review;

    private LocalDateTime addedAt;

    private LocalDateTime updatedAt;

    public UserGameEntity () {}

    public UserGameEntity(String review, UserProfileEntity profile, GameEntity game, GameStatus status, Boolean favorite, Integer rating) {
        this.review = review;
        this.profile = profile;
        this.game = game;
        this.status = status;
        this.favorite = favorite;
        this.rating = rating;
        this.addedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
