package com.example.quiz1150512.entity.dto;

import java.util.List;

import com.example.quiz1150512.constants.ValidationMsg;

import jakarta.validation.constraints.NotEmpty;

public class QuizDeleteRequest {
	@NotEmpty(message = ValidationMsg.QUIZ_IDS_REQUIRED)
   private List<Long> quizIds;
   public List<Long> getQuizIds() {
       return quizIds;
   }
   public void setQuizIds(List<Long> quizIds) {
       this.quizIds = quizIds;
   }
}
