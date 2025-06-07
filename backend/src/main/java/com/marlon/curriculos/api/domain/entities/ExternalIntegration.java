package com.marlon.curriculos.api.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "external_integrations", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "provider"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalIntegration {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private IntegrationProvider provider;

  @Column(name = "external_user_id", length = 200)
  private String externalUserId; 

  @Column(name = "access_token", columnDefinition = "TEXT")
  private String accessToken;

  @Column(name = "refresh_token", columnDefinition = "TEXT")
  private String refreshToken; 

  @Column(name = "token_expires_at")
  private LocalDateTime tokenExpiresAt;

  @Column(name = "last_sync_at")
  private LocalDateTime lastSyncAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "sync_status", length = 20)
  @Builder.Default
  private SyncStatus syncStatus = SyncStatus.ACTIVE;

  @CreationTimestamp
  @Column(name = "update_at")
  private LocalDateTime updateAt;

  public enum IntegrationProvider{
    LINKEDIN("Linkedin"),
    GOVBR("gov.br"),
    GOOGLE("Google"),
    FACEBOOK("Facebook");

    private final String displayName;

    IntegrationProvider(String displayName){
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }

  public enum SyncStatus {
    ACTIVE, INACTIVE, ERROR, EXPIRED
  }


  public boolean isTokenExpired() {
    return tokenExpiresAt != null && tokenExpiresAt.isBefore(LocalDateTime.now());
  }

  public boolean needsRefresh() {
    return isTokenExpired() && refreshToken != null;
  }

  public void updateTokens(String accessToken, String refreshToken, LocalDateTime expiresAt) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenExpiresAt = expiresAt; 
    this.syncStatus = SyncStatus.ACTIVE;
  }

  public void markSyncCompleted() {
    this.lastSyncAt = LocalDateTime.now();
    this.syncStatus = SyncStatus.ERROR;
  }

  public void markSyncError() {
    this.syncStatus = SyncStatus.ERROR;
  }

  public void deactive() {
    this.syncStatus = SyncStatus.INACTIVE;
    this.accessToken = null;
    this.refreshToken = null;
    this.tokenExpiresAt = null;
  }

  public boolean isLinkedIn() {
    return provider == IntegrationProvider.LINKEDIN;
  }

  public boolean isGovBr() {
    return provider == IntegrationProvider.GOVBR;
  }

  public boolean canSync() {
    return syncStatus == SyncStatus.ACTIVE && !isTokenExpired();
  }




}
