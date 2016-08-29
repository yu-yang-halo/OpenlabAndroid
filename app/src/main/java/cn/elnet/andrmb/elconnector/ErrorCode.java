/**
 * <p>Package: cn.lztech.elcomm.message</p>
 * <p>File: ErrorCode.java</p>
  *
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>Created at 2010-11-4</p>
 *
 * @author zjiang
 * @version 1.0
 */
package cn.elnet.andrmb.elconnector;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Administrator
 * ErrorCode 错误编码及信息
 */
public enum ErrorCode {
	ACCEPT(0, "Succeed"),
	REJECT(1, "Rejected"),
	CONN_TO_WS_ERR(2,"CONN_TO_WS_ERR"),
	NET_WORK_TIME_OUT(3,"net work time out"),
	LOGIN_FAILED(4,"login failed"),
	INVALID_LOGIN_NAME_PWD(1001, "Invalid login name or password"),
	LOGIN_NAME_NOT_EXIST(1002, "The login name does not exist"),
	USER_ACCT_LOCKED(1003, "The user account is locked"),
	DUP_NAME(1004, "Name has already been used in database"),	
	PERMISSION_DENY(1005, "The permission denied."),
	SEC_TOKEN_EXPRIED(1006, "The security token was expired "),
	INVALID_SEC_TOKEN(1007, "Invalid security token"),
	USER_ALREADY_LOGOUT(1008, "The user was logout"),
	USER_ACCT_NOT_EXIST(1009, "The user account does not exist"),
	DOMAIN_NOT_FOUND(1010, "The domain was not found"),
	ACTION_NOT_FOUND(1011, "The action was not found"),
	TARGET_NOT_FOUND(1012, "The target was not found"),
	
	INTERNAL_ERROR(1100, "Internal error"),
	REQ_TIME_OUT(1101, "Request time out"),	
	WS_CONN_ERROR(1102, "Fail to connect to the web services"),
	INTERNAL_CONN_ERROR(1103, "The web services internal connection error"),
	INTERNAL_DB_ERR(1104, "Internal database error"),
	DATA_CONVERSION_ERR(1105, "Data conversion error"),
	
	LAB_NOT_FOUND(2000, "No lab info was added into the system"),
	RESV_NOT_FOUND(2001, "No reservation was found "),
	DEVICE_NOT_FOUND(2002, "The device was not found "),
	AS_NOT_FOUND(2003, "The assignment was not found "),
	RESV_ALREADY_EXIST(2004, "reservation already exist "),
	RESV_CAN_NOT_CANCEL(2005, "reservation can't cancel "),
	RESV_FULL(2006, "reservation full "),
	UNKNOWN_ERR(2020, "Unknown error");

	
	private static final Map<Integer, ErrorCode> lookup = new HashMap<Integer, ErrorCode>();

	static {
		for (ErrorCode s : EnumSet.allOf(ErrorCode.class))
			lookup.put(s.getCode(), s);
	}

	private int code;
	private String desc;

	private ErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public boolean equals(ErrorCode value) {
		if (this.code == value.getCode())
			return true;
		else
			return false;
	}
	
	public String toString() {
		return this.desc;
	}

	public static ErrorCode get(int code) {
		ErrorCode errCode=lookup.get(code);
		if(errCode==null){
			errCode=ErrorCode.UNKNOWN_ERR;
		}
		return errCode;
	}
}
