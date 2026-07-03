package com.savpoint.savpoint.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Getter
@Setter
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column(nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = true)
    private String playtime;

    @Column(nullable = false)
    private String plataforms;

    @Column(nullable = false)
    private String genres;

    @Column(nullable = true)
    private String developers;

    @Column(nullable = true)
    private String publisher;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    public GameEntity () {}

    public GameEntity(String externalId, String slug, String title, String description, LocalDate releaseDate, String coverUrl, String playtime, String plataforms, String genres, String developers, String publisher) {
        this.externalId = externalId;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.coverUrl = coverUrl;
        this.playtime = playtime;
        this.plataforms = plataforms;
        this.genres = genres;
        this.developers = developers;
        this.publisher = publisher;
        this.addedAt = LocalDateTime.now();
    }
}
