package in.choubeyshubham.assignment.service;

import in.choubeyshubham.assignment.model.AuditLog;
import in.choubeyshubham.assignment.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String action, String entity, String entityId, String performedBy, String details) {
        AuditLog log = new AuditLog(action, entity, entityId, performedBy, details);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAuditLogsForIncident(String incidentId) {
        return auditLogRepository.findByEntityId(incidentId);
    }
}
