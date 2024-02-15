package com.govtech.proj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "github_scheduled_maintenance")
public class GitHubScheduledMaintenance {
    @Id
    private String maintenanceId;
    private String impact;
    private String name;
    private String status;
}
