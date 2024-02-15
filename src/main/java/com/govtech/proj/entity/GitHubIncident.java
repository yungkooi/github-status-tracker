package com.govtech.proj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "github_incident")
public class GitHubIncident {
    @Id
    private String incidentId;
    private String impact;
    private String name;
    private String status;
}
