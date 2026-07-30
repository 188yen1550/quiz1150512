package com.example.quiz1150512.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz1150512.Response.ApiResponse;
import com.example.quiz1150512.Service.QuizService;
import com.example.quiz1150512.entity.QuestionResponse;
import com.example.quiz1150512.entity.dto.QuizRequest;
import com.example.quiz1150512.entity.dto.QuizResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
	@Autowired
	private QuizService quizService;
	
	/**
    * 1. 新增問卷 (僅回傳成功/失敗訊息)
    * POST /api/quiz/create
    */
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createQuiz(@Valid @RequestBody QuizRequest request) {
		quizService.createQuiz(request);
		return ResponseEntity.status(HttpStatus.CREATED)
               .body(ApiResponse.success("Quiz created successfully!!"));
	}
	
	/**
    * 2. 更新問卷 (僅回傳成功/失敗訊息)
    * POST /api/quiz/update
    */
	@PostMapping("/update")
   public ResponseEntity<ApiResponse> updateQuiz(
           @Valid @RequestBody QuizRequest request) {
       quizService.updateQuiz(request);
       return ResponseEntity.ok(ApiResponse.success("Quiz updated successfully!!"));
   }
	
	/**
    * 3. 取得所有的問卷列表 (僅問卷，不含問題與選項)
    * GET /api/quiz/get-all
    */
   @GetMapping("/get-all")
   public ResponseEntity<List<QuizResponseDto>> getAllQuizzes() {
       List<QuizResponseDto> quizzes = quizService.getAllQuizzesWithoutQuestions();
       return ResponseEntity.ok(quizzes);
   }
  
   /**
    * 4. 根據問卷 id 取得對應的問題內容以及選項
    * GET /api/quiz/{id}/questions
    */
   @GetMapping("/{id}/questions")
   public ResponseEntity<List<QuestionResponse>> getQuestionsByQuizId(@PathVariable("id") Long id) {
       List<QuestionResponse> questions = quizService.getQuestionsByQuizId(id);
       return ResponseEntity.ok(questions);
   }
}

