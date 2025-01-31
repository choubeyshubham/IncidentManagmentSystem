package in.choubeyshubham.assignment.service;

import in.choubeyshubham.assignment.model.Incident;
import in.choubeyshubham.assignment.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private AuditLogService auditLogService;

    public IncidentService(IncidentRepository incidentRepository, AuditLogService auditLogService) {
        this.incidentRepository = incidentRepository;
        this.auditLogService = auditLogService;
    }
    //-----------------------------



    public Incident createIncident(Incident incident, String reporterEmail) {
        incident.setReporterEmail(reporterEmail);
        incident.setIncidentId("RMG" + (10000 + new Random().nextInt(90000)) + "2024");

        Incident savedIncident = incidentRepository.save(incident);
        auditLogService.logAction("CREATE", "INCIDENT", savedIncident.getIncidentId(), reporterEmail,
                "Created new incident.");

        return savedIncident;
    }


    public List<Incident> getUserIncidents(String reporterEmail) {
        return incidentRepository.findByReporterEmail(reporterEmail);
    }

    public Optional<Incident> getIncidentById(String incidentId) {
        return incidentRepository.findByIncidentId(incidentId);
    }

    public Incident updateIncident(Long id, Incident updatedIncident, String reporterEmail, boolean isAdmin) {
        return incidentRepository.findById(id).map(incident -> {
            if (!incident.getStatus().equals("Closed") && (incident.getReporterEmail().equals(reporterEmail) || isAdmin)) {
                String changes = "Updated: ";
                if (!incident.getDescription().equals(updatedIncident.getDescription())) {
                    changes += "Description changed. ";
                }
                if (!incident.getPriority().equals(updatedIncident.getPriority())) {
                    changes += "Priority changed to " + updatedIncident.getPriority() + ". ";
                }
                if (!incident.getStatus().equals(updatedIncident.getStatus())) {
                    changes += "Status changed to " + updatedIncident.getStatus() + ". ";
                }

                incident.setDescription(updatedIncident.getDescription());
                incident.setPriority(updatedIncident.getPriority());
                incident.setStatus(updatedIncident.getStatus());

                Incident updated = incidentRepository.save(incident);
                auditLogService.logAction("UPDATE", "INCIDENT", updated.getIncidentId(), reporterEmail, changes);
                return updated;
            }
            throw new IllegalStateException("Unauthorized or Closed incidents cannot be edited");
        }).orElseThrow(() -> new IllegalArgumentException("Incident not found"));
    }

    public void deleteIncident(Long id, String reporterEmail, boolean isAdmin) {
        incidentRepository.findById(id).ifPresent(incident -> {
            if (isAdmin || incident.getReporterEmail().equals(reporterEmail)) {
                incidentRepository.deleteById(id);
                auditLogService.logAction("DELETE", "INCIDENT", incident.getIncidentId(), reporterEmail, "Incident deleted.");
            } else {
                throw new IllegalStateException("Unauthorized to delete incident");
            }
        });
    }

}
