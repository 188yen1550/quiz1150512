package com.example.quiz1150512.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.quiz1150512.Dao.QuestionDao;
import com.example.quiz1150512.Dao.QuestionOptionDao;
import com.example.quiz1150512.Dao.QuizDao;
import com.example.quiz1150512.Dao.QuizResponseDao;
import com.example.quiz1150512.Dao.ResponseDetailDao;
import com.example.quiz1150512.Response.OptionResponse;
import com.example.quiz1150512.entity.Question;
import com.example.quiz1150512.entity.QuestionOption;
import com.example.quiz1150512.entity.QuestionResponse;
import com.example.quiz1150512.entity.Quiz;
import com.example.quiz1150512.entity.dto.FillQuizRequest;
import com.example.quiz1150512.entity.dto.OptionRequest;
import com.example.quiz1150512.entity.dto.QuestionAnswerRequest;
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
	@Autowired
	private QuizResponseDao quizResponseDao;
	@Autowired
	private ResponseDetailDao responseDetailDao;
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
		if (id == null || id <= 0) {
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
		quizDao.findQuizById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
		List<Question> questions = questionDao.findByQuizId(quizId);
		return questions.stream().map(this::convertToQuestionDto).collect(Collectors.toList());
	}
	/**
	 * 5. 批次刪除問卷（包含對應的問題與選項）
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteQuizzes(List<Long> quizIds) {
		// 1. 刪除這些問卷對應的所有選項
		questionOptionDao.deleteByQuizIds(quizIds);
		// 2. 刪除這些問卷對應的所有問題
		questionDao.deleteByQuizIds(quizIds);
		// 3. 刪除問卷本體
		int deletedCount = quizDao.deleteByQuizIds(quizIds);
		if (deletedCount == 0) {
			throw new RuntimeException("No quizzes were found to delete!!");
		}
	}
	/**
	 * 6. 填寫並提交問卷
	 */
	@Transactional(rollbackFor = Exception.class)
	public void fillQuiz(FillQuizRequest request) {
		// 1. 檢查問卷是否存在與發布狀態
		/*
		 * orElseThrow 是 Optional 提供的方法，意思是: 如果容器裡面有資料，就把 Quiz 物件拿出來；
		 * 如果容器裡面是空的(沒找到資料)，就執行括號裡面的程式碼，拋出例外
		 */
		Quiz quiz = quizDao.findQuizById(request.getQuizId())
				.orElseThrow(() -> new RuntimeException("Quiz not found with id: " + request.getQuizId()));
		/*
		 * 安全的布林值比較: 是種防止 NullPointerException 的寫法； 如果傳入 true --> 回傳 true； 如果傳入 false
		 * --> 回傳 false； 如果傳入 null --> 不會報錯，直接回傳 false
		 */
		if (!Boolean.TRUE.equals(quiz.getIsPublished())) {
			throw new IllegalArgumentException("This quiz is not published yet!!");
		}
		// 2. 檢查問卷可填寫時間
		LocalDate now = LocalDate.now();
		if (now.isBefore(quiz.getStartDate())) {
			throw new IllegalArgumentException("This quiz has not started yet!!");
		}
		if (now.isAfter(quiz.getEndDate())) {
			throw new IllegalArgumentException("This quiz has already ended!!");
		}
		// 3. 檢查必填欄位與答案合法性
		List<Question> questions = questionDao.findByQuizId(quiz.getId());
		validateFillAnswers(questions, request.getAnswers());
		// 4. 寫入 quiz_response (主表)
		// 註：若此 Email 在此 Quiz 已填過，會觸發 uk_quiz_user 唯一約束並拋出 Database Exception
		quizResponseDao.insertQuizResponse(request.getQuizId(), request.getUserEmail());
		Long responseId = quizResponseDao.getLastInsertedId();
		// 5. 寫入 response_detail (明細表)
		saveResponseDetails(responseId, request.getAnswers());
	}
	// ==================== 私有輔助方法 ====================
	private void saveQuestionsAndOptions(Long quizId, List<QuestionRequest> questionRequests) {
		for (QuestionRequest qReq : questionRequests) {
			validateQuestionOptions(qReq);
			questionDao.insertQuestion(quizId, qReq.getQuestionNum(), qReq.getTitle(), qReq.getType().name(),
					qReq.getIsRequired());
			/* 取得新增的最新流水號 */
			Long newQuestionId = questionDao.getLastInsertedId();
			/* 新增選項: 前提是 qReq.getOptions() 不能是 null 或是 空集合 */
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
		if (request.getEndDate().isBefore(request.getStartDate()) || request.getStartDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("End date cannot be earlier than start date!!");
		}
	}
	private void validateQuestionOptions(QuestionRequest qReq) {
		// 若不是簡答題型(單、多選)，就必須要有選項 ==> 如果非簡答題 且 無選項 ==> 拋出例外
		if (qReq.getType() != QuestionType.TEXT) {
			if (qReq.getOptions() == null || qReq.getOptions().isEmpty()) {
				throw new IllegalArgumentException(
						"Question #" + qReq.getQuestionNum() + " requires at least one option!!");
			}
		}
	}
	private void validateFillAnswers(List<Question> questions, List<QuestionAnswerRequest> answers) {
		/*
		 * 轉成 Map 的原因: 1. 大幅提升執行效率(降低時間複雜度) 2. 直接用 Key 查詢 3. 自動過濾前端傳遞的「重複答案」: (k1, k2)
		 * -> k1 表示 有重複 key 時，保留舊的 key(k1) 對應的值
		 */
		Map<Long, QuestionAnswerRequest> answerMap = answers.stream()
				.collect(Collectors.toMap(QuestionAnswerRequest::getQuestionId, a -> a, (k1, k2) -> k1));
		// 以資料庫的標準題目為基準，逐一檢查使用者的答案是否合規
		for (Question question : questions) {
			QuestionAnswerRequest userAns = answerMap.get(question.getId());
			// 檢查必填題
			if (Boolean.TRUE.equals(question.getIsRequired())) {
				if (userAns == null || isAnswerEmpty(question.getType(), userAns)) {
					throw new IllegalArgumentException("Question #" + question.getQuestionNum() + " is required!!");
				}
			}
			// 如果使用者有傳遞答案，且為選擇題，進行選項合法性檢查
			if (userAns != null
					&& (question.getType() == QuestionType.SINGLE || question.getType() == QuestionType.MULTI)) {
				List<Long> submittedOptionIds = userAns.getOptionIds();
				if (submittedOptionIds != null && !submittedOptionIds.isEmpty()) {
					// 2. 自動去重複（防止重複傳入相同的 option_id，例如 [10, 10, 11, 12, 13]）
					Set<Long> uniqueSubmittedIds = new HashSet<>(submittedOptionIds);
					// 3. 查出資料庫中該題目「真正擁有」的所有 Option IDs
					List<QuestionOption> validOptions = questionOptionDao.findByQuestionId(question.getId());
					Set<Long> validOptionIds = validOptions.stream().map(QuestionOption::getId)
							.collect(Collectors.toSet());
					// 4. 數量與範圍驗證：
					// A. 檢查傳入的選項數量是否超過資料庫該題的選項總數 (例如選項只有 4 個，答案卻有 5 個)
					if (uniqueSubmittedIds.size() > validOptionIds.size()) {
						throw new IllegalArgumentException("Question #" + question.getQuestionNum()
								+ " has invalid/excessive option selections!!");
					}
					// B. 檢查傳入的每一個 option_id 是否真的屬於該題目 (防止偷渡其他題目的 option_id)
					for (Long optionId : uniqueSubmittedIds) {
						if (!validOptionIds.contains(optionId)) {
							throw new IllegalArgumentException("Question #" + question.getQuestionNum()
									+ " contains an invalid option ID: " + optionId);
						}
					}
					// 5. 單選題規則檢查 (不可選超過 1 個不同選項)
					if (question.getType() == QuestionType.SINGLE && uniqueSubmittedIds.size() > 1) {
						throw new IllegalArgumentException(
								"Question #" + question.getQuestionNum() + " is a single-choice question!!");
					}
				}
			}
		}
	}
	// 必填題的檢查: 根據題目的類型（單選、多選或簡答），判斷使用者傳進來的答案內容是否為「空白／未填寫」
	private boolean isAnswerEmpty(QuestionType type, QuestionAnswerRequest ans) {
		// 選擇題(單/多選): 但 optionIds 沒有東西
		if (type == QuestionType.SINGLE || type == QuestionType.MULTI) {
			return ans.getOptionIds() == null || ans.getOptionIds().isEmpty();
		} else if (type == QuestionType.TEXT) {
			// 簡答題: 但卻沒有內容
			return ans.getAnswerText() == null || ans.getAnswerText().isBlank();
		}
		return true;
	}
	private void saveResponseDetails(Long responseId, List<QuestionAnswerRequest> answers) {
		for (QuestionAnswerRequest ans : answers) {
			if (ans.getOptionIds() != null && !ans.getOptionIds().isEmpty()) {
				for (Long optionId : ans.getOptionIds()) {
					responseDetailDao.insertDetail(responseId, ans.getQuestionId(), optionId, null);
				}
			} else if (ans.getAnswerText() != null && !ans.getAnswerText().isBlank()) {
				responseDetailDao.insertDetail(responseId, ans.getQuestionId(), null, ans.getAnswerText());
			}
		}
	}
}
