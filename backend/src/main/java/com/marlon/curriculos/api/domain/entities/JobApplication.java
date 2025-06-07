
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
import java.util.UUID;


@Entity
@Table(name = "job_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resume_id", nullable = false)
  private Resume resume;

  @Column(name = "job_title", nullable = false, length = 200)
  private String jobTitle;

  @Column(name = "company_name", nullable = false, length = 200)
  private String company_name;

  @Column(name = "job_url", length = 500)
  private String jobUrl;

  @Column(name = "application_date")
  @Builder.Default
  private LocalDateTime applicationDate = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  @Builder.Default
  private ApplicationStatus status = ApplicationStatus.APPLIED;

  @Column(columnDefinition = "TEXT")
  private LocalDate followUpDate;

  @Column(name = "interview_date")
  private LocalDateTime InterviewDate;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public enum ApplicationStatus {
    APPLIED("Candidatura Enviada"),
    UNDER_REVIEW("Em Análise"),
    INTERVIEW_SCHEDULED("Entrevista Agendada"),
    INTERVIEWED("Entrevistado"),
    SECOND_ROUND("Segunda Fase"),
    OFFER_RECEIVED("Proposta Recebida"),
    ACCEPTED("Aceito"),
    REJECTED("Rejeitado"),
    WHITHDRAWN("Retirado");

    private final String displayName;

    ApplicationStatus(String displayName){
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }


  public void scheduleInterview(LocalDateTime interviewDateTime) {
    this.InterviewDate = interviewDateTime;
    this.status = ApplicationStatus.INTERVIEW_SCHEDULED;
  }

  public void markInterviewed() {
    this.status = ApplicationStatus.INTERVIEW_SCHEDULED;
  }

  public void receiveOffer(String salaryOffer) {
    this.salaryOfferred = salaryOffer;
    this.status = ApplicationStatus.OFFER_RECEIVED;
  }

  public void accept() {
    this.status = ApplicationStatus.ACCEPTED;
  }

  public void withdraw() {
    this.status = ApplicationStatus.REJECTED;
  }

  public void addNotes(String additionalNotes) {
        if (this.notes == null) {
            this.notes = additionalNotes;
        } else {
            this.notes += "\n\n" + LocalDateTime.now() + ": " + additionalNotes;
        }
    }

  public void setFollowUpDate(LocalDate date) {
    this.followUpDate = date;
  }

  public boolean needsFollowUp() {
    return followUpDate != null && followUpDate.isBefore(LocalDate.now()) && isActiveApplication();
  }

  public boolean isActiveApplication() {
    return status != ApplicationStatus.ACCEPTED && status != ApplicationStatus.REJECTED && status != ApplicationStatus.WHITHDRAWN;
  }

  public boolean isSuccessful() {
    return status == ApplicationStatus.ACCEPTED;
  }

  public boolean isPending() {
    return status == ApplicationStatus.APPLIED || 
           status == ApplicationStatus.UNDER_REVIEW ||
           status == ApplicationStatus.INTERVIEW_SCHEDULED || 
           status == ApplicationStatus.SECOND_ROUND;
  }
}
