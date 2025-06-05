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

import java.awt.DisplayMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Entity
@Table(name = "resume_sections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSection {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resume_id", nullable = false)
  private Resume resume;

  @Enumerated(EnumType.STRING)
  @Column(name = "section_type", nullable = false, length = 50)
  private SectionType sectionType;

  @Column(length = 200)
  private String title;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> content;

  @Column(name = "display_order", nullable = false)
  private Integer displayOrder;

  @Column(name = "is_visible")
  @Builder.Default
  private Boolean isVisible = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false) 
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public enum SectionType {
    PERSONAL_INFO("Informações Pessoais"),
    PROFESSIONAL_SUMMARY("Resumo Profissional"),
    EXPERIENCE("Experiência Profissional"),
    EDUCATION("Educação"),
    SKILLS("Idiomas"),
    LANGUAGES("Idiomas"),
    CARTIFICATIONS("Certificações"),
    PROJECTS("Projetos"),
    AWARDS("Prêmios e Reconhecimentos"),
    VOLUNTEER("Trabalho Voluntário"),
    REFERENCES("Referências"),
    CUSTOM("Seção Personalizada");

    private final String displayName;

    SectionType(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }

  //Businness methods 

  public void moveUp() {
    if (displayOrder > 1) {
      displayOrder--;
    }
  }

  public void moveDown(){
    displayOrder++;
  }

  public void hide() {
    this.isVisible = false;
  }

  public void show() {
    this.isVisible= true;
  }

  public boolean isPersonalInfo() {
    return sectionType == SectionType.PERSONAL_INFO;
  }

  public boolean isRequired() {
    return sectionType == SectionType.PERSONAL_INFO || sectionType == SectionType.EXPERIENCE;
  }


  public void updateContent(Map<String, Object> newContent) {
    this.content= newContent;
  }

  public Object getContentField(String fieldName){
    return content != null ? content.get(fieldName) : null;
  }

  public void setContentField(String fieldName, Object value) {
    if (content == null) {
      content = new java.util.HashMap<>();
    }
    content.put(fieldName, value);
  }

  
  
}
