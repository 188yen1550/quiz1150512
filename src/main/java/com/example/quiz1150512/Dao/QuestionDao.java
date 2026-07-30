package com.example.quiz1150512.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quiz1150512.entity.Question;

@Repository
public interface QuestionDao extends JpaRepository<Question, Long> {
	@Query(value = "SELECT * FROM question WHERE quiz_id = ?1 ORDER BY question_num ASC", //
			nativeQuery = true)
	public List<Question> findByQuizId(Long quizId);
	@Modifying
	@Query(value = "INSERT INTO question (quiz_id, question_num, title, type, is_required) "//
			+ " VALUES (:quizId, :questionNum, :title, :type, :isRequired)", nativeQuery = true)
	public void insertQuestion(//
			@Param("quizId") Long quizId, //
			@Param("questionNum") Integer questionNum,//
			@Param("title") String title, //
			@Param("type") String type, //
			@Param("isRequired") Boolean isRequired);
	@Modifying
	@Query(value = "DELETE FROM question WHERE quiz_id = ?1", nativeQuery = true)
	public void deleteByQuizId(Long quizId);
	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
	public Long getLastInsertedId();
}