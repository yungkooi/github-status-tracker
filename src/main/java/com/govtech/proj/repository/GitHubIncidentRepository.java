package com.govtech.proj.repository;

import com.govtech.proj.entity.GitHubIncident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitHubIncidentRepository extends JpaRepository<GitHubIncident, Long> {
}
