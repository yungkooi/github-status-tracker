package com.govtech.proj.repository;

import com.govtech.proj.entity.GitHubScheduledMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitHubScheduledMaintenanceRepository extends JpaRepository<GitHubScheduledMaintenance, Long> {
}
