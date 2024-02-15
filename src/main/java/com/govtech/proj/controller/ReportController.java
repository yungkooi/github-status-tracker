package com.govtech.proj.controller;

import com.govtech.proj.service.GitHubStatusTrackingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class ReportController {

    @Autowired
    GitHubStatusTrackingService gitHubStatusTrackingService;

    @GetMapping("/api/csv/download")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        String csvData = gitHubStatusTrackingService.generateCsvReport();
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=github_data_report.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(csvData);
        }
    }
}
