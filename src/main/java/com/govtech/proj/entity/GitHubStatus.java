package com.govtech.proj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "github_status")
public class GitHubStatus {
    @Id
    private Long id = 1L;
    private LocalDateTime timestamp;
    private String description;
    private String indicator;
}
