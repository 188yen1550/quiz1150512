package com.example.quiz1150512.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quiz1150512.entity.QuizResponse;

@Repository
public interface QuizResponseDao extends JpaRepository <QuizResponse,Long > {
	
	
	@Modifying
	   @Query(value = "INSERT INTO quiz_response (quiz_id, user_email) " //
	   		+ " VALUES (:quizId, :userEmail)", nativeQuery = true)
	   public void insertQuizResponse(//
	   		@Param("quizId") Long quizId, //
	   		@Param("userEmail") String userEmail);
	   @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
	   public Long getLastInsertedId();

}
