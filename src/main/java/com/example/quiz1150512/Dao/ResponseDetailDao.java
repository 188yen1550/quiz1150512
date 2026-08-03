package com.example.quiz1150512.Dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResponseDetailDao {

	@Modifying
	@Query(value = "INSERT INTO response_detail (response_id, question_id, option_id, answer_text) "//
			+ " VALUES (:responseId, :questionId, :optionId, :answerText)", nativeQuery = true)
	public void insertDetail(//
			@Param("responseId") Long responseId, //
			@Param("questionId") Long questionId, //
			@Param("optionId") Long optionId, //
			@Param("answerText") String answerText);

}
