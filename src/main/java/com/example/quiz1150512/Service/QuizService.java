package com.example.quiz1150512.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.quiz1150512.Dao.QuestionDao;
import com.example.quiz1150512.Dao.QuestionOptionDao;
import com.example.quiz1150512.Dao.QuizDao;
import com.example.quiz1150512.Response.OptionResponse;
import com.example.quiz1150512.entity.Question;
import com.example.quiz1150512.entity.QuestionOption;
import com.example.quiz1150512.entity.QuestionResponse;
import com.example.quiz1150512.entity.Quiz;
import com.example.quiz1150512.entity.dto.OptionRequest;
import com.example.quiz1150512.entity.dto.QuestionRequest;
import com.example.quiz1150512.entity.dto.QuizRequest;
import com.example.quiz1150512.entity.dto.QuizResponseDto;
import com.example.quiz1150512.enums.QuestionType;

@Service
public class QuizService {
	@Autowired
	private QuizDao quizDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private QuestionOptionDao questionOptionDao;
	/**
	 * 1. 新增問卷 (不需回傳完整問卷，成功即結束)
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createQuiz(QuizRequest request) {
		validateQuizTime(request);
		// 1. 新增 Quiz
		quizDao.insertQuiz(request.getTitle(), request.getDescription(), request.getStartDate(), request.getEndDate(),
				request.getIsPublished());
		Long newQuizId = quizDao.getLastInsertedId();
		// 2. 寫入所有問題與選項
		saveQuestionsAndOptions(newQuizId, request.getQuestions());
	}
	
	/**
	 * 2. 更新問卷 (Request 包含問卷以及對應的所有問題與選項)
	 */
	@Transactional
	public void updateQuiz(QuizRequest request) {
		validateQuizTime(request);
		// 更新一定要有 quiz_id
		Long id = request.getId();
		if(id == null || id <= 0) {
			throw new RuntimeException("Quiz not found with id: " + id);
		}
		// 1. 更新 Quiz 本體
		int updatedRows = quizDao.updateQuiz(id, request.getTitle(), request.getDescription(), request.getStartDate(),
				request.getEndDate(), request.getIsPublished());
		if (updatedRows == 0) {
			throw new RuntimeException("Quiz not found with id: " + id);
		}
		/* 先刪除舊的，再新增更新的 */
		// 2. 刪除舊有選項與問題
		questionOptionDao.deleteByQuizId(id);
		questionDao.deleteByQuizId(id);
		// 3. 重新新增問題與選項
		saveQuestionsAndOptions(id, request.getQuestions());
	}
	
	/**
    * 3. 取得所有的問卷列表 (僅問卷，沒有問題內容以及選項)
    */
   @Transactional(readOnly = true)
   public List<QuizResponseDto> getAllQuizzesWithoutQuestions() {
       List<Quiz> quizzes = quizDao.findAllQuizzes();
       return quizzes.stream().map(quiz -> {
           QuizResponseDto dto = new QuizResponseDto();
           dto.setId(quiz.getId());
           dto.setTitle(quiz.getTitle());
           dto.setDescription(quiz.getDescription());
           dto.setStartDate(quiz.getStartDate());
           dto.setEndDate(quiz.getEndDate());
           dto.setIsPublished(quiz.getIsPublished());
           dto.setQuestions(null); // 不回傳問題與選項
           return dto;
       }).collect(Collectors.toList());
   }
   /**
    * 4. 根據問卷 id 取得對應的問題內容以及選項
    */
   @Transactional(readOnly = true)
   public List<QuestionResponse> getQuestionsByQuizId(Long quizId) {
       quizDao.findQuizById(quizId)
               .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
       List<Question> questions = questionDao.findByQuizId(quizId);
       return questions.stream()
               .map(this::convertToQuestionDto)
               .collect(Collectors.toList());
   }
	// ==================== 私有輔助方法 ====================
	private void saveQuestionsAndOptions(Long quizId, List<QuestionRequest> questionRequests) {
		for (QuestionRequest qReq : questionRequests) {
			validateQuestionOptions(qReq);
			questionDao.insertQuestion(quizId, qReq.getQuestionNum(), qReq.getTitle(), qReq.getType().name(),
					qReq.getIsRequired());
			/* 取得新增的最新流水號 */
			Long newQuestionId = questionDao.getLastInsertedId();
			/* 新增選項: 前提是 qReq.getOptions() 不能是 null 或是 空集合*/
			if (!CollectionUtils.isEmpty(qReq.getOptions())) {
				for (OptionRequest oReq : qReq.getOptions()) {
					questionOptionDao.insertOption(newQuestionId, oReq.getOptionCode(), oReq.getOptionText());
				}
			}
		}
	}
	private QuestionResponse convertToQuestionDto(Question question) {
		QuestionResponse qDto = new QuestionResponse();
		qDto.setId(question.getId());
		qDto.setQuestionNum(question.getQuestionNum());
		qDto.setTitle(question.getTitle());
		qDto.setType(question.getType());
		qDto.setIsRequired(question.getIsRequired());
		List<QuestionOption> options = questionOptionDao.findByQuestionId(question.getId());
		List<OptionResponse> oDtos = options.stream().map(o -> {
			OptionResponse oDto = new OptionResponse();
			oDto.setId(o.getId());
			oDto.setOptionCode(o.getOptionCode());
			oDto.setOptionText(o.getOptionText());
			return oDto;
		}).collect(Collectors.toList());
		qDto.setOptions(oDtos);
		return qDto;
	}
	private void validateQuizTime(QuizRequest request) {
		// QuizRequest 已經有檢查 startDate 和 endDate 不能是 null
		// 檢查: 1. 結束日期不能比開始日期早 2. 開始日期不能比今天早(開始日期最早只能是當天)
		// 因為是使用排除法，所以 1. 結束日期比開始日期早 或 2. 開始日期比今天早，就拋出錯誤
		if (request.getEndDate().isBefore(request.getStartDate()) ||
				request.getStartDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("End date cannot be earlier than start date!!");
		}
	}
	private void validateQuestionOptions(QuestionRequest qReq) {
		// 若不是簡答題型(單、多選)，就必須要有選項 ==> 如果非簡答題 且 無選項  ==> 拋出例外
		if (qReq.getType() != QuestionType.TEXT) {
			if (qReq.getOptions() == null || qReq.getOptions().isEmpty()) {
				throw new IllegalArgumentException(
						"Question #" + qReq.getQuestionNum() + " requires at least one option!!");			
			}
		}
	}
}
