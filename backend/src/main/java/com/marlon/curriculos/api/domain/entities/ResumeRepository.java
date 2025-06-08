
package com.marlon.curriculos.api.domain.entities;

import com.marlon.curriculos.api.domain.entities.Resume;
import com.marlon.curriculos.api.domain.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    
    List<Resume> findByUserOrderByUpdatedAtDesc(User user);
    
    Page<Resume> findByUser(User user, Pageable pageable);
    
    Optional<Resume> findByIdAndUser(UUID id, User user);
    
    Optional<Resume> findByPublicUrlToken(String publicUrlToken);
    
    @Query("SELECT r FROM Resume r WHERE r.isPublic = true AND r.status = 'PUBLISHED'")
    Page<Resume> findPublicResumes(Pageable pageable);
    
    @Query("SELECT COUNT(r) FROM Resume r WHERE r.user = :user")
    long countByUser(@Param("user") User user);
    
    @Query("SELECT r FROM Resume r WHERE r.user = :user AND r.status = :status")
    List<Resume> findByUserAndStatus(@Param("user") User user, 
                                   @Param("status") Resume.ResumeStatus status);
    
    @Query("SELECT r FROM Resume r WHERE r.template.id = :templateId")
    List<Resume> findByTemplateId(@Param("templateId") UUID templateId);
    
    @Query("SELECT r FROM Resume r WHERE r.updatedAt < :cutoffDate AND r.status = 'DRAFT'")
    List<Resume> findOldDraftResumes(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    @Query("SELECT r FROM Resume r WHERE r.viewCount > :minViews ORDER BY r.viewCount DESC")
    List<Resume> findPopularResumes(@Param("minViews") Integer minViews);
    
    @Query("SELECT r FROM Resume r WHERE r.downloadCount > :minDownloads ORDER BY r.downloadCount DESC")
    List<Resume> findMostDownloadedResumes(@Param("minDownloads") Integer minDownloads);
    
    @Query("SELECT r FROM Resume r WHERE r.title LIKE %:title% AND r.user = :user")
    List<Resume> findByUserAndTitleContaining(@Param("user") User user, 
                                            @Param("title") String title);
    
    @Query("SELECT COUNT(r) FROM Resume r WHERE r.createdAt >= :startDate AND r.createdAt <= :endDate")
    long countResumesCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT r FROM Resume r JOIN FETCH r.sections WHERE r.id = :id")
    Optional<Resume> findByIdWithSections(@Param("id") UUID id);
    
    @Query("SELECT r FROM Resume r JOIN FETCH r.theme WHERE r.id = :id")
    Optional<Resume> findByIdWithTheme(@Param("id") UUID id);
}
