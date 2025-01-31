package in.choubeyshubham.assignment.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action; // CREATE, UPDATE, DELETE

    @Column(nullable = false)
    private String entity; // INCIDENT

    @Column(nullable = false)
    private String entityId; // Incident ID

    @Column(nullable = false)
    private String performedBy; // User who made the change

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @Column(columnDefinition = "TEXT")
    private String details; // Change details

    // ✅ Constructors, Getters, Setters
    public AuditLog() {}

    public AuditLog(String action, String entity, String entityId, String performedBy, String details) {
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.performedBy = performedBy;
        this.details = details;
    }

    public Long getId() { return id; }
    public String getAction() { return action; }
    public String getEntity() { return entity; }
    public String getEntityId() { return entityId; }
    public String getPerformedBy() { return performedBy; }
    public Date getTimestamp() { return timestamp; }
    public String getDetails() { return details; }
}