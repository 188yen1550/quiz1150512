package com.example.quiz1150512.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quiz1150512.entity.QuestionOption;

@Repository
public interface QuestionOptionDao extends JpaRepository<QuestionOption, Long> {
	@Query(value = "SELECT * FROM question_option WHERE question_id = ?1 ORDER BY id ASC", //
			nativeQuery = true)
	public List<QuestionOption> findByQuestionId(Long questionId);
	@Modifying
	@Query(value = "INSERT INTO question_option (question_id, option_code, option_text) "//
			+ " VALUES (:questionId, :optionCode, :optionText)", nativeQuery = true)
	public void insertOption(//
			@Param("questionId") Long questionId, //
			@Param("optionCode") String optionCode,//
			@Param("optionText") String optionText);
	@Modifying
	@Query(value = "DELETE FROM question_option WHERE question_id "
			+ " IN (SELECT id FROM question WHERE quiz_id = ?1)", nativeQuery = true)
	public void deleteByQuizId(Long quizId);
	// question_option 綁定 question_id，question_id 則是從 quiz_id 得到
		@Modifying
		@Query(value = "DELETE FROM question_option WHERE question_id IN ("//
				+ " SELECT id FROM question WHERE quiz_id IN (:quizIds))", //
				nativeQuery = true)
		public void deleteByQuizIds(@Param("quizIds") List<Long> quizIds);

}
