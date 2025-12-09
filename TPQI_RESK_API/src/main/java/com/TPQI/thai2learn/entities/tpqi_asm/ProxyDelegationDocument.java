package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "proxy_delegation_documents")
public class ProxyDelegationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "proxy_log_id", nullable = false)
    private Long proxyLogId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    public ProxyDelegationDocument() {}
    public ProxyDelegationDocument(Long proxyLogId, String filePath) {
        this.proxyLogId = proxyLogId;
        this.filePath = filePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProxyLogId() { return proxyLogId; }
    public void setProxyLogId(Long proxyLogId) { this.proxyLogId = proxyLogId; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}