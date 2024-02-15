package com.govtech.proj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "github_component")
public class GitHubComponent {
    @Id
    private String name;
    private String status;
}
