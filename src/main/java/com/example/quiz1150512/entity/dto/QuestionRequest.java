package com.example.quiz1150512.entity.dto;


import java.util.List;

import com.example.quiz1150512.constants.ValidationMsg;
import com.example.quiz1150512.enums.QuestionType;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class QuestionRequest {
	@NotNull(message = ValidationMsg.QUESTION_NUM_REQUIRED)
	private Integer questionNum; // 問題編號 (如: 1, 2, 3)
	@NotBlank(message = ValidationMsg.QUESTION_TITLE_REQUIRED)
	private String title;
	/* Spring Boot 預設使用 Jackson 解析 JSON。當前端傳送 "SINGLE"、"MULTI" 或 "TEXT" 等字串時，
	 * Jackson 會自動尋找 QuestionType 中名稱完全一致的列舉值並進行反序列化 <br>
	 * 注意: 原本是有區分大小寫，但在 QuestionType 中有寫了一個不區分大小寫自動反序列化的方法 */
	@NotNull(message = ValidationMsg.QUESTION_TYPE_REQUIRED)
	private QuestionType type;
	private Boolean isRequired = true;
	@Valid
	private List<OptionRequest> options;
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
	public List<OptionRequest> getOptions() {
		return options;
	}
	public void setOptions(List<OptionRequest> options) {
		this.options = options;
	}
}
