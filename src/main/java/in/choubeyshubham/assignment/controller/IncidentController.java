package in.choubeyshubham.assignment.controller;

import in.choubeyshubham.assignment.model.Incident;
import in.choubeyshubham.assignment.service.IncidentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping("/create/{userId}")
    public Incident createIncident(@PathVariable Long userId, @RequestBody Incident incident) {
        return incidentService.createIncident(userId, incident);
    }

    @GetMapping("/user/{userId}")
    public List<Incident> getUserIncidents(@PathVariable Long userId) {
        return incidentService.getUserIncidents(userId);
    }

    @GetMapping("/{incidentId}")
    public Optional<Incident> getIncidentById(@PathVariable String incidentId) {
        return incidentService.getIncidentById(incidentId);
    }

    @PutMapping("/update/{incidentId}")
    public Incident updateIncident(@PathVariable String incidentId, @RequestBody Incident updatedIncident) {
        return incidentService.updateIncident(incidentId, updatedIncident);
    }
}
