package com.govtech.proj.repository;

import com.govtech.proj.entity.GitHubComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitHubComponentRepository extends JpaRepository<GitHubComponent, Long> {
}
