package com.marlon.curriculos.api.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


/**
 * Template
 */

@Entity
@Table(name = "templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Template {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private TemplateCategory category;

  @Column(name = "preview_image_url", length = 500)
  private String previewImageUrl; 

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> structure;

  
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "default_theme", columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> defaultTheme;

  @Column(name = "is_premium")
  @Builder.Default
  private Boolean isPremium = false;

  @Column(precision = 3, scale = 2)
  @Builder.Default
  private Boolean isActive = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "update_at")
  private LocalDateTime updatedAt;

  public enum TemplateCategory {
    PROFESSIONAL("Profissional"),
    CREATIVE("Criativo"),
    MODERN("Moderno"),
    CLASSIC("Clássico"),
    MINIMALIST("Minimalista"),
    ACADEMIC("Acadêmico"),
    TECHNICAL("Técnico");

    private final String displayName;

    TemplateCategory(String displayName) {
      this.displayName = displayName;
    }
    public String getDisplayName(){
      return displayName;
    }
  }

  public void incrementUsage() {
    this.usageCount++;
  }

  public boolean isAvailableForUser(User.SubscriptionPlan userPlan) {
    if (!isPremium) {
      return true;
    }
    return userPlan == User.SubscriptionPlan.PREMIUM || userPlan == User.SubscriptionPlan.PROFESSIONAL;
  }

  public void updateRating(BigDecimal newRating) {
    this.rating = newRating;
  }

  public void activate() {
    this.isActive = true;
  }

  public void deactivate() {
    this.isActive = false;
  }


}
