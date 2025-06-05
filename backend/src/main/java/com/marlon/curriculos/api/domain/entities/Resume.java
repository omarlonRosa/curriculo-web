public package com.marlon.curriculos.api.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.sql.Template;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "resumes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Resume {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(fetch = FectchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, length = 200)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id")
  private Template template;

  @Column(name = "is_public")
  @Builder.Default
  private Boolean isPublic = false;

  @Column(name = "public_url_token", unique = true, length = 100)
  private String publicUrlToken;

  @Column(name = "view_count")
  @Builder.Default
  private Integer viewCount = 0;

  @Column(name = "download_count")
  @Builder.Default
  private Integer downloadCount = 0;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ResumeStatus status = ResumeStatus.DRAFT;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "update_at")
  private LocalTime updatedAt;

  @Column(name = "last_exported_at")
  private LocalDateTime lastExportedAt;

  @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("displayOrder ASC")
  @Builder.Default
  private List<ResumeSection> sections = new ArrayList<>();

  @OneToOne(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private ResumeTheme theme;

  @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<JobApplication> jobApplications = new ArrayList<>();

  public enum ResumeStatus {
    DRAFT, PUBLISHED, ARCHIVED
  }

  //BUSINESS METHODS 
  public void incrementViewCount() {
    this.viewCount++;
  }

  public void incrementDownloadCount() {
    this.downloadCount++;
    this.lastExportedAt = LocalDateTime.now();
  }

  public void publish() {
    this.status = ResumeStatus.PUBLISHED;
    if (this.publicUrlToken == null) {
      this.publicUrlToken = generatePublicToken();
    }
  }

  public void archive() {
    this.status = ResumeStatus.ARCHIVED;
    this.isPublic = false;
    this.publicUrlToken = null;
  }

  public boolean isOwnedBy(UUID userId) {
    return this.user.getId().equals(userId);
  }

  public boolean canBeViewedBy(UUID userId)
{
    return isOwnedBy(userId) || (isPublic && status == ResumeStatus.PUBLISHED);
  }

  private String generatePublicToken() {
    return UUID.randomUUID().toString().replace("_","").substring(0, 16);
  }

  public void addSection(ResumeSection section) {
    section.setResume(this);

    section.setDisplayOrder(this.sections.size() + 1);
    this.sections.add(section);
  }

  public void removeSection(ResumeSection section) {
    this.sections.remove(section);
    reorderSection();
  }

  private void reorderSections() {
    for (int i = 0; i < sections.size(); i++) {
      sections.get(i).setDisplayOrder(i + 1)
    }
  }






  
}
