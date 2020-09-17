package com.project.assessment.type;

public enum ErrorType {

	GENERAL_SYSTEM_ERROR(StatusType.FAIL, 99), //
	SUCCESS(StatusType.SUCCESS, 0), //
	SUCCESS_LOGIN(StatusType.SUCCESS, 0), //
	SUCCESS_LOGIN_FIRST(StatusType.SUCCESS, 1), //
	SUCCESS_LOGIN_EXPIRY(StatusType.SUCCESS, 2), //
	UNAUTHORIZED(StatusType.FAIL, 92), //
	INVALID_PARAMETER(StatusType.FAIL, 97);

	private final StatusType status;
	private final int resultCode;

	ErrorType(final StatusType status, final int resCode) {
		this.status = status;
		this.resultCode = resCode;
	}

	public StatusType getStatus() {
		return status;
	}

	public int getResultCode() {
		return this.resultCode;
	}

}
