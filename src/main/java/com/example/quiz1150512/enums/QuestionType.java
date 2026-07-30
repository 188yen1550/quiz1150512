package com.example.quiz1150512.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum QuestionType {
	SINGLE("單選題"),//
   MULTI("多選題"),//
   TEXT("簡答題");
	
	private String type;
	private QuestionType(String type) {
		this.type = type;
	}
	/*@JsonValue: 預設是不加，會回傳 Enum 的變數名稱(SINGLE、MULTI...)；
	 * 有加，回傳 Enum 變數小括號後面的字串值*/
	//@JsonValue
	public String getType() {
		return type;
	}
	
	/**
    * 前端傳 JSON 進來時呼叫此方法進行轉換
    * 支援比對：Enum 名稱 ("SINGLE" / "single") 或 中文標籤 ("單選題")
    * @JsonCreator 就是 Spring Boot / Jackson 解析 JSON 時的「觸發開關」，
    * 所以有加上 @JsonCreator 所以會自動呼叫 fromString 這個方法
    */




   @JsonCreator
   public static QuestionType fromString(String input) {
       if (input == null || input.isBlank()) {
           return null;
       }
       for (QuestionType qType : QuestionType.values()) {
           // 1. 比對 Enum 變數名稱 (忽略大小寫，例如 single, MULTI)
           if (qType.name().equalsIgnoreCase(input)) {
               return qType;
           }
           // 2. 比對中文 description (例如 單選題, 多選題)
           if (qType.getType().equalsIgnoreCase(input)) {
               return qType;
           }
       }
       throw new IllegalArgumentException("Invalid question type: " + input);
   }
}
