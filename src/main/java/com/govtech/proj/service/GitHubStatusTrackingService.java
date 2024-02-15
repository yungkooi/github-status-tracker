package com.govtech.proj.service;

public interface GitHubStatusTrackingService {
    void fetchAndSaveStatus();

    void fetchAndSaveComponentStatus();

    void fetchAndSaveRecentIncidents();

    void fetchAndSaveRecentScheduledMaintenances();

    String generateCsvReport();
}
