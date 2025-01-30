package in.choubeyshubham.assignment.service;

import in.choubeyshubham.assignment.model.Incident;
import in.choubeyshubham.assignment.model.User;
import in.choubeyshubham.assignment.repository.IncidentRepository;
import in.choubeyshubham.assignment.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    public Incident createIncident(Long userId, Incident incident) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        incident.setUser(user);
        incident.setReportedDate(LocalDateTime.now());
        incident.setIncidentId(generateIncidentId());

        return incidentRepository.save(incident);
    }

    public List<Incident> getUserIncidents(Long userId) {
        return incidentRepository.findByUserId(userId);
    }

    public Optional<Incident> getIncidentById(String incidentId) {
        return incidentRepository.findByIncidentId(incidentId);
    }

    public Incident updateIncident(String incidentId, Incident updatedIncident) {
        Incident incident = incidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if (!"Closed".equalsIgnoreCase(incident.getStatus())) {
            incident.setDetails(updatedIncident.getDetails());
            incident.setPriority(updatedIncident.getPriority());
            incident.setStatus(updatedIncident.getStatus());
            return incidentRepository.save(incident);
        } else {
            throw new RuntimeException("Closed incidents cannot be edited");
        }
    }

    private String generateIncidentId() {
        int randomNum = (int) (Math.random() * 90000) + 10000;
        return "RMG" + randomNum + LocalDateTime.now().getYear();
    }
}

