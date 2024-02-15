package com.govtech.proj.repository;

import com.govtech.proj.entity.GitHubStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitHubStatusRepository extends JpaRepository<GitHubStatus, Long> {
}
