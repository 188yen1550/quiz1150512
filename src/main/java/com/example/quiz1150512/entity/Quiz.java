package com.example.quiz1150512.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quiz")
public class Quiz {
	@Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
   private Long id;
   @Column(name = "title", nullable = false, length = 100)
   private String title;
   /*columnDefinition = "TEXT": 指定該欄位的精確資料型態與約束為 TEXT */
   @Column(name = "description", columnDefinition = "TEXT")
   private String description;
   @Column(name = "start_date")
   private LocalDate startDate;
   @Column(name = "end_date")
   private LocalDate endDate;
   @Column(name = "is_published", nullable = false)
   private Boolean isPublished = false;
   @Column(name = "created_at", insertable = false, updatable = false)
   private LocalDateTime createdAt;
   @Column(name = "updated_at", insertable = false, updatable = false)
   private LocalDateTime updatedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Boolean getIsPublished() {
		return isPublished;
	}
	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
  
}
