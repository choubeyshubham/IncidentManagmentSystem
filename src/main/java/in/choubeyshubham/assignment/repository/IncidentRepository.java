package in.choubeyshubham.assignment.repository;

import in.choubeyshubham.assignment.model.Incident;
import in.choubeyshubham.assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Optional<Incident> findByIncidentId(String incidentId);
    List<Incident> findByReporterEmail(String reporterEmail);

}