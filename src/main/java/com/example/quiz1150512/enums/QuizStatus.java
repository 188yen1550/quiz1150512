package com.example.quiz1150512.enums;

public enum QuizStatus {
	DRAFT("草稿"),
	   NOT_STARTED("未開始"),
	   IN_PROGRESS("進行中"),
	   ENDED("已結束");
	   private final String label;
	   QuizStatus(String label) {
	       this.label = label;
	   }
	   public String getLabel() {
	       return label;
	   }
}
