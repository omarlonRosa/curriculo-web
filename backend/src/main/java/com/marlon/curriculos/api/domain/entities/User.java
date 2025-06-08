package com.marlon.curriculos.api.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @Column(length = 20)
  private String phone;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "profile_picture_url", length = 500)
  private String profilePictureUrl;

@Enumerated(EnumType.STRING)
@Column(name = "subscription_plan")
@Builder.Default
private SubscriptionPlan subscriptionPlan = SubscriptionPlan.FREE; 


  @Enumerated(EnumType.STRING)
  @Column(name = "subscription_status")
  @Builder.Default
  private SubscriptionStatus subscriptionStatus = SubscriptionStatus.ACTIVE;

  @Column(name = "email_verified")
  @Builder.Default
  private Boolean emailVerified = false;

  @Column(name = "is_active")
  @Builder.Default 
  private Boolean isActive = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<Resume> resumes = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<ExternalIntegration> externalIntegrations = new ArrayList<>();

  public enum SubscriptionPlan {
    FREE, PREMIUM, PROFESSIONAL
  }

  public enum SubscriptionStatus {
    ACTIVE, INACTIVE, CANCELLED, EXPIRED
  }

  //BUSINESS METHODS 
  public boolean isPremiumUser() {
    return subscriptionPlan == SubscriptionPlan.PREMIUM || 
    subscriptionPlan == subscriptionPlan.PROFESSIONAL;
  }

  public boolean canCreateMoreResumes(int currentResumeCount){
    return switch (subscriptionPlan) {
      case FREE -> currentResumeCount < 2;
      case PREMIUM, PROFESSIONAL -> true;
    };
  }

  public void updateLastLogin() {
    this.lastLoginAt = LocalDateTime.now();
  }

  public String getFullname() {
    return firstName + " " + lastName;
  }
}
