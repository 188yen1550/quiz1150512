package com.example.quiz1150512.entity.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz1150512.constants.ValidationMsg;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuizRequest {
	@NotBlank(message = ValidationMsg.QUIZ_TITLE_REQUIRED)
	private String title;
	@NotBlank(message = ValidationMsg.QUIZ_DESCRIPTION_REQUIRED)
	private String description;
	@NotNull(message = ValidationMsg.QUIZ_START_DATE_REQUIRED)
	private LocalDate startDate;
	@NotNull(message = ValidationMsg.QUIZ_END_DATE_REQUIRED)
	private LocalDate endDate;
	@NotNull(message = ValidationMsg.QUIZ_PUBLISHED_REQUIRED)
	private Boolean isPublished = false;
	@Valid
	private List<QuestionRequest> questions;
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
	public List<QuestionRequest> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionRequest> questions) {
		this.questions = questions;
	}
	
	// create 和 update 公用此 QuizRequest，但只有 update 時此屬性才會有值
		private Long id;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

}
