package in.choubeyshubham.assignment.service;

import in.choubeyshubham.assignment.model.Incident;
import in.choubeyshubham.assignment.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Incident createIncident(Incident incident) {
        return incidentRepository.save(incident);
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
                incident.setDescription(updatedIncident.getDescription());
                incident.setPriority(updatedIncident.getPriority());
                incident.setStatus(updatedIncident.getStatus());
                return incidentRepository.save(incident);
            }
            throw new IllegalStateException("Unauthorized or Closed incidents cannot be edited");
        }).orElseThrow(() -> new IllegalArgumentException("Incident not found"));
    }

//    public void deleteIncident(Long id, boolean isAdmin) {
//        if (!isAdmin) {
//            throw new IllegalStateException("Only admins can delete incidents");
//        }
//        incidentRepository.deleteById(id);
//    }

    public void deleteIncident(Long id, String reporterEmail, boolean isAdmin) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found"));

        if (!isAdmin && !incident.getReporterEmail().equals(reporterEmail)) {
            throw new IllegalStateException("Unauthorized: You can only delete your own incidents");
        }

        incidentRepository.deleteById(id);
    }

}
