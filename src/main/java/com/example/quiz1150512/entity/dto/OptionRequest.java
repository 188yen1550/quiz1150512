package com.example.quiz1150512.entity.dto;

import com.example.quiz1150512.constants.ValidationMsg;
import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;

public class OptionRequest {
	@JsonAlias({"code"})// 用來匹配前端的變數名稱
	@NotBlank(message = ValidationMsg.OPTION_CODE_REQUIRED)
	private String optionCode; // 選項編號 (如: A, B, C 或 數字 1, 2, 3 的字串)
	@JsonAlias({"optionName"})
	@NotBlank(message = ValidationMsg.OPTION_TEXT_REQUIRED)
	private String optionText; // 選項內容
	public String getOptionCode() {
		return optionCode;
	}
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
}
