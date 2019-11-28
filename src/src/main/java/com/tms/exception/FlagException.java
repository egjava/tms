package com.tms.exception;


import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Response;



public class FlagException extends Exception {
		
		private StatusType errorCode = (StatusType) Response.status(2).build();
		public StatusType getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(StatusType errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		private String errorMessage = "DB Error";
}
