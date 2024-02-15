package com.govtech.proj.service.impl;

import com.govtech.proj.entity.GitHubComponent;
import com.govtech.proj.entity.GitHubIncident;
import com.govtech.proj.entity.GitHubStatus;
import com.govtech.proj.entity.GitHubScheduledMaintenance;
import com.govtech.proj.repository.GitHubComponentRepository;
import com.govtech.proj.repository.GitHubIncidentRepository;
import com.govtech.proj.repository.GitHubScheduledMaintenanceRepository;
import com.govtech.proj.repository.GitHubStatusRepository;
import com.govtech.proj.service.GitHubStatusTrackingService;
import com.govtech.proj.utilities.DateTimeConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubStatusTrackingServiceImpl implements GitHubStatusTrackingService {

    private final GitHubStatusRepository statusRepository;
    private final GitHubComponentRepository componentRepository;
    private final GitHubIncidentRepository incidentRepository;
    private final GitHubScheduledMaintenanceRepository maintenanceRepository;

    @Value("${github.status.url}")
    private String gitHubStatusUrl;

    @Value("${github.components.url}")
    private String gitHubComponentsUrl;

    @Value("${github.incidents.url}")
    private String gitHubIncidentsUrl;

    @Value("${github.scheduled.maintenances.url}")
    private String gitHubScheduledMaintenancesUrl;

    @Autowired
    public GitHubStatusTrackingServiceImpl(
            GitHubStatusRepository statusRepository,
            GitHubComponentRepository componentRepository,
            GitHubIncidentRepository incidentRepository,
            GitHubScheduledMaintenanceRepository maintenanceRepository) {
        this.statusRepository = statusRepository;
        this.componentRepository = componentRepository;
        this.incidentRepository = incidentRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    @Scheduled(fixedRateString = "${request.interval}")
    public void fetchAndSaveStatus() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(gitHubStatusUrl, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONObject status = jsonResponse.getJSONObject("status");
        GitHubStatus gitHubStatus = statusRepository.findById(1L).orElse(new GitHubStatus());
        gitHubStatus.setTimestamp(DateTimeConverter.getCurrentTime());
        gitHubStatus.setDescription(status.getString("description"));
        gitHubStatus.setIndicator(status.getString("indicator"));

        statusRepository.save(gitHubStatus);
    }

    @Override
    @Scheduled(fixedRateString = "${request.interval}")
    public void fetchAndSaveComponentStatus() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(gitHubComponentsUrl, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray components = jsonResponse.getJSONArray("components");

        for (int i = 0; i < components.length(); i++) {
            JSONObject component = components.getJSONObject(i);
            GitHubComponent gitHubComponent = new GitHubComponent();
            gitHubComponent.setName(component.getString("name"));
            gitHubComponent.setStatus(component.getString("status"));

            componentRepository.save(gitHubComponent);
        }
    }

    @Override
    @Scheduled(fixedRateString = "${request.interval}")
    @Transactional
    public void fetchAndSaveRecentIncidents() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(gitHubIncidentsUrl, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray incidents = jsonResponse.getJSONArray("incidents");

        List<GitHubIncident> incidentList = new ArrayList<>();

        int incidentCountLimit = Math.min(incidents.length(), 10);

        for (int i = 0; i < incidentCountLimit; i++) {
            JSONObject incidentJson = incidents.getJSONObject(i);
            GitHubIncident gitHubIncident = new GitHubIncident();
            gitHubIncident.setIncidentId(incidentJson.getString("id"));
            gitHubIncident.setImpact(incidentJson.getString("impact"));
            gitHubIncident.setName(incidentJson.getString("name"));
            gitHubIncident.setStatus(incidentJson.getString("status"));
            incidentList.add(gitHubIncident);
        }

        List<GitHubIncident> mostRecentIncidents = incidentList.stream().toList();

        incidentRepository.deleteAllInBatch();
        incidentRepository.saveAll(mostRecentIncidents);
    }

    @Override
    @Scheduled(fixedRateString = "${request.interval}")
    @Transactional
    public void fetchAndSaveRecentScheduledMaintenances() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(gitHubScheduledMaintenancesUrl, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray scheduledMaintenances = jsonResponse.getJSONArray("scheduled_maintenances");

        List<GitHubScheduledMaintenance> maintenanceList = new ArrayList<>();

        int maintenanceCountLimit = Math.min(scheduledMaintenances.length(), 10);

        for (int i = 0; i < maintenanceCountLimit; i++) {
            JSONObject maintenanceJson = scheduledMaintenances.getJSONObject(i);
            GitHubScheduledMaintenance gitHubScheduledMaintenance = new GitHubScheduledMaintenance();
            gitHubScheduledMaintenance.setMaintenanceId(maintenanceJson.getString("id"));
            gitHubScheduledMaintenance.setImpact(maintenanceJson.getString("impact"));
            gitHubScheduledMaintenance.setName(maintenanceJson.getString("name"));
            gitHubScheduledMaintenance.setStatus(maintenanceJson.getString("status"));
            maintenanceList.add(gitHubScheduledMaintenance);
        }

        List<GitHubScheduledMaintenance> mostRecentScheduledMaintenances = maintenanceList.stream().toList();

        maintenanceRepository.deleteAllInBatch();
        maintenanceRepository.saveAll(mostRecentScheduledMaintenances);
    }

    @Override
    @Transactional(readOnly = true)
    public String generateCsvReport() {
        StringWriter csvWriter = new StringWriter();

        csvWriter.append("GitHub Overall Status\n");
        csvWriter.append("Timestamp,Description,Indicator\n");
        List<GitHubStatus> statuses = statusRepository.findAll();
        for (GitHubStatus status : statuses) {
            csvWriter.append(String.format("\"%s\",\"%s\",\"%s\"\n",
                    DateTimeConverter.formatDateTime(status.getTimestamp()),
                    status.getDescription(),
                    status.getIndicator()));
        }
        csvWriter.append("\n");

        csvWriter.append("GitHub Components\n");
        csvWriter.append("Name,Status\n");
        List<GitHubComponent> components = componentRepository.findAll();
        for (GitHubComponent component : components) {
            csvWriter.append(String.format("\"%s\",\"%s\"\n", component.getName(), component.getStatus()));
        }
        csvWriter.append("\n");

        csvWriter.append("GitHub Incidents\n");
        csvWriter.append("Incident ID,Impact,Name,Status\n");
        List<GitHubIncident> incidents = incidentRepository.findAll();
        for (GitHubIncident incident : incidents) {
            csvWriter.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    incident.getIncidentId(),
                    incident.getImpact(),
                    incident.getName(),
                    incident.getStatus()));
        }
        csvWriter.append("\n");

        csvWriter.append("GitHub Scheduled Maintenance\n");
        csvWriter.append("Maintenance ID,Impact,Name,Status\n");
        List<GitHubScheduledMaintenance> maintenances = maintenanceRepository.findAll();
        for (GitHubScheduledMaintenance maintenance : maintenances) {
            csvWriter.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    maintenance.getMaintenanceId(),
                    maintenance.getImpact(),
                    maintenance.getName(),
                    maintenance.getStatus()));
        }

        return csvWriter.toString();
    }
}
