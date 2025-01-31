package in.choubeyshubham.assignment.repository;

import in.choubeyshubham.assignment.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEntityId(String entityId);
}