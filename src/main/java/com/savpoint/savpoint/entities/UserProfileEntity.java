package com.savpoint.savpoint.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "userProfile")
@Getter
@Setter
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private BigDecimal credits;

    public UserProfileEntity() {}

    public UserProfileEntity(UserEntity user, String displayName) {
        this.user = user;
        this.displayName = displayName;
        this.createdAt = LocalDateTime.now();
        this.credits = new BigDecimal(0);
    }
}
