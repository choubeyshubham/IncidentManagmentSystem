package in.choubeyshubham.assignment.controller;

import in.choubeyshubham.assignment.model.Incident;
import in.choubeyshubham.assignment.service.IncidentService;
import in.choubeyshubham.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;
    private final UserService userService;
    public IncidentController(IncidentService incidentService, UserService userService) {
        this.incidentService = incidentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createIncident(@RequestBody Incident incident, HttpServletRequest request) {
        String reporterEmail = (String) request.getAttribute("email");
        incident.setReporterEmail(reporterEmail);
        Incident savedIncident = incidentService.createIncident(incident);
        return ResponseEntity.ok(savedIncident);
    }

    @GetMapping
    public ResponseEntity<?> getUserIncidents(HttpServletRequest request) {
        String reporterEmail = (String) request.getAttribute("email");
        List<Incident> incidents = incidentService.getUserIncidents(reporterEmail);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{incidentId}")
    public ResponseEntity<?> getIncidentById(@PathVariable String incidentId, HttpServletRequest request) {
        String reporterEmail = (String) request.getAttribute("email");
        return incidentService.getIncidentById(incidentId)
                .map(incident -> incident.getReporterEmail().equals(reporterEmail) ? ResponseEntity.ok(incident) : ResponseEntity.status(403).body("Unauthorized"))
                .orElse(ResponseEntity.status(404).body("Incident not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIncident(@PathVariable Long id, @RequestBody Incident updatedIncident, HttpServletRequest request) {
        try {
            String reporterEmail = (String) request.getAttribute("email");
            boolean isAdmin = "admin@example.com".equals(reporterEmail); // Example admin check
            Incident incident = incidentService.updateIncident(id, updatedIncident, reporterEmail, isAdmin);
            return ResponseEntity.ok(incident);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncident(@PathVariable Long id, HttpServletRequest request) {
        try {
            String reporterEmail = (String) request.getAttribute("email");
            boolean isAdmin = userService.isAdmin(reporterEmail);
            incidentService.deleteIncident(id, reporterEmail, isAdmin);
            return ResponseEntity.ok("Incident deleted successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}