package com.example.quiz1150512.entity.dto;


import java.time.LocalDate;
import java.util.List;

import com.example.quiz1150512.entity.QuestionResponse;



public class QuizResponseDto {
	private Long id;
	private String title;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private Boolean isPublished;
	private List<QuestionResponse> questions;
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
	public List<QuestionResponse> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionResponse> questions) {
		this.questions = questions;
	}
}


