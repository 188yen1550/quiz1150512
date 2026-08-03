package com.example.quiz1150512.entity.dto;

import java.util.List;

import com.example.quiz1150512.constants.ValidationMsg;

import jakarta.validation.constraints.NotNull;

public class QuestionAnswerRequest {
	@NotNull(message = ValidationMsg.QUESTION_ID_REQUIRED)
   private Long questionId;
   // 單選或多選題對應的選項 ID 列表
   private List<Long> optionIds;
   // 簡答題內容
   private String answerText;
   // Getters and Setters
   public Long getQuestionId() {
       return questionId;
   }
   public void setQuestionId(Long questionId) {
       this.questionId = questionId;
   }
   public List<Long> getOptionIds() {
       return optionIds;
   }
   public void setOptionIds(List<Long> optionIds) {
       this.optionIds = optionIds;
   }
   public String getAnswerText() {
       return answerText;
   }
   public void setAnswerText(String answerText) {
       this.answerText = answerText;
   }
}
