package com.example.quiz1150512.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	/* 如果有使用到 spring-data-jpa 定義的 save() 或是 xxxById() 等方法時，
	 * @GeneratedValue(strategy = GenerationType.IDENTITY) 要加上；若是使用 @Query 自定義語法，
	 * 則不需要
	 * */


	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	@Column(name = "phone", nullable = false, unique = true, length = 20)
	private String phone;
	@Column(name = "password", length = 60)
	private String password; // 密碼 (長度 60，用於 BCrypt 雜湊值)
	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;
	@Column(name = "age")
	private Integer age;
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

