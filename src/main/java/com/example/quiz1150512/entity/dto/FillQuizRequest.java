package com.example.quiz1150512.entity.dto;

import java.util.List;

import com.example.quiz1150512.constants.ValidationMsg;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class FillQuizRequest {
	@NotNull(message = ValidationMsg.QUIZ_ID_REQUIRED)
   private Long quizId;
   @NotBlank(message = ValidationMsg.EMAIL_REQUIRED)
   @Email(message = ValidationMsg.EMAIL_FORMAT_INVALID)
   private String userEmail;
   @Valid
   @NotEmpty(message = ValidationMsg.QUESTION_ANSWERS_REQUIRED)
   private List<QuestionAnswerRequest> answers;
   // Getters and Setters
   public Long getQuizId() {
       return quizId;
   }
   public void setQuizId(Long quizId) {
       this.quizId = quizId;
   }
   public String getUserEmail() {
       return userEmail;
   }
   public void setUserEmail(String userEmail) {
       this.userEmail = userEmail;
   }
   public List<QuestionAnswerRequest> getAnswers() {
       return answers;
   }
   public void setAnswers(List<QuestionAnswerRequest> answers) {
       this.answers = answers;
   }
}

