package com.marlon.curriculos.api.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_themes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * ResumeTheme
 */
public class ResumeTheme {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resume_id", nullable = false)
  private Resume resume;

  @Column(name =  "primary_color", length = 7)
  @Builder.Default
  private String primaryColor = "#000000";

  @Column(name= "secondaryColor", length = 7)
  @Builder.Default
  private String secondaryColor = "#666666";

  @Column(name = "accent_color", length = 7)
  @Builder.Default
  private String accentColor = "#0066cc";

  @Column(name = "background_color", length = 7)
  @Builder.Default
  private String backgroundColor = "#ffffff";

  @Column(name = "font_family", length = 100)
  @Builder.Default
  private String fontFamily = "Arial";

  @Column(name = "font_size")
  @Builder.Default
  private Integer fontSize = 12;

  @Column(name = "line_height", precision = 3, scale = 2)
  @Builder.Default
  private BigDecimal lineHeight = BigDecimal.valueOf(1.5);

  @Column(name = "margin_top")
  @Builder.Default
  private Integer marginTop = 20;

  @Column(name = "margin_bottom")
  @Builder.Default
  private Integer marginBottom = 20;

  @Column(name = "margin_left")
  @Builder.Default
  private Integer marginLeft = 20;

  @Column(name = "margin_right")
  @Builder.Default
  private Integer marginRight = 20;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public void updateColors(String primary, String secondary, String accent, String background) {
    if (isValidHexColor(primary))
    this.primaryColor = primary;
    if (isValidHexColor(secondary))
    this.secondaryColor = secondary;
    if (isValidHexColor(accent))
    this.accentColor = accent;
    if (isValidHexColor(background))
    this.backgroundColor = background;
  }

public void updateTypography(String fontFamily, Integer fontSize, BigDecimal lineHeight) {
        if (fontFamily != null && !fontFamily.trim().isEmpty()) {
            this.fontFamily = fontFamily;
        }
        if (fontSize != null && fontSize >= 8 && fontSize <= 24) {
            this.fontSize = fontSize;
        }
        if (lineHeight != null && lineHeight.compareTo(BigDecimal.valueOf(1.0)) >= 0 && 
            lineHeight.compareTo(BigDecimal.valueOf(3.0)) <= 0) {
            this.lineHeight = lineHeight;
        }
    }

    private boolean isValidHexColor(String color) {
        return color != null && color.matches("^#[0-9A-Fa-f]{6}$");
    }

    public void resetToDefaults() {
        this.primaryColor = "#000000";
        this.secondaryColor = "#666666";
        this.accentColor = "#0066cc";
        this.backgroundColor = "#ffffff";
        this.fontFamily = "Ariel";
        this.fontSize = 12;
        this.lineHeight = BigDecimal.valueOf(1.5);
        this.marginTop = 20;
        this.lineHeight = BigDecimal.valueOf(1.5);
        this.marginBottom = 20;
        this.marginLeft = 20;
        this.marginRight = 20;

    }

    public boolean isDarkTheme() {
        return backgroundColor.equals("#000000") || backgroundColor.equals("#1a1a1a") || backgroundColor.equals("#2d2d2d");
    }
  
}
