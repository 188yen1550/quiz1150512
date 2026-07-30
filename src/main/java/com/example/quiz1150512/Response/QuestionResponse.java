package com.example.quiz1150512.Response;



import java.util.List;

import com.example.quiz1150512.enums.QuestionType;

public class QuestionResponse {
	private Long id;
	private Integer questionNum;
	private String title;
	private QuestionType type;
	private Boolean isRequired;
	private List<OptionResponse> options;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getQuestionNum() {
		return questionNum;
	}
	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public QuestionType getType() {
		return type;
	}
	public void setType(QuestionType type) {
		this.type = type;
	}
	public Boolean getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
	public List<OptionResponse> getOptions() {
		return options;
	}
	public void setOptions(List<OptionResponse> options) {
		this.options = options;
	}
}

