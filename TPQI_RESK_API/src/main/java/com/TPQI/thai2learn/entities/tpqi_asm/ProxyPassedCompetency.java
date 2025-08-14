package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "proxy_passed_competencies")
public class ProxyPassedCompetency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "proxy_log_id", nullable = false)
    private Long proxyLogId;

    @Column(name = "competency_code", nullable = false)
    private String competencyCode;

    public ProxyPassedCompetency() {}

    public ProxyPassedCompetency(Long proxyLogId, String competencyCode) {
        this.proxyLogId = proxyLogId;
        this.competencyCode = competencyCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProxyLogId() { return proxyLogId; }
    public void setProxyLogId(Long proxyLogId) { this.proxyLogId = proxyLogId; }
    public String getCompetencyCode() { return competencyCode; }
    public void setCompetencyCode(String competencyCode) { this.competencyCode = competencyCode; }
}