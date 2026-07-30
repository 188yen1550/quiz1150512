package com.example.quiz1150512.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
* @UniqueConstraint: 是用來定義資料庫的「聯合唯一索引／約束」 name: 這組唯一約束在資料庫中的「索引／約束名稱」，名稱自定義，uk
*                    通常是 Unique Key 的簡寫<br>
*                    columnNames: 參與這組唯一約束的「資料庫欄位名稱 而非 類別中的屬性名稱」
*/
@Entity
@Table(name = "quiz_response", uniqueConstraints = {
		@UniqueConstraint(name = "uk_quiz_user", columnNames = { "quiz_id", "user_email" }) })
public class QuizResponse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "quiz_id", nullable = false)
	private Long quizId;
	@Column(name = "user_email", nullable = false, length = 100)
	private String userEmail;
	@Column(name = "submitted_at", insertable = false, updatable = false)
	private LocalDateTime submittedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}
	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}
}


