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
	ACCEPT(0, "成功"),
	REJECT(1, "服务器错误，请稍后重试"),
	CONN_TO_WS_ERR(2,"登录失败,请重试"),
	NET_WORK_TIME_OUT(3,"不能连接到服务器,请检查手机网络状态"),
	LOGIN_FAILED(4,"login failed"),
	INVALID_LOGIN_NAME_PWD(1001, "用户名或密码输入错误"),
	LOGIN_NAME_NOT_EXIST(1002, "用户名或密码输入错误"),
	USER_ACCT_LOCKED(1003, "账户被锁定"),
	DUP_NAME(1008, "用户名已经被使用"),
	PERMISSION_DENY(1005, "权限拒绝"),
	SEC_TOKEN_EXPRIED(1006, "token失效,请重新登录"),
	INVALID_SEC_TOKEN(1007, "token失效,请重新登录"),
	USER_ALREADY_LOGOUT(2000, "实验室信息无法添加到该系统"),
	USER_ACCT_NOT_EXIST(2001, "预约无法找到"),
	DOMAIN_NOT_FOUND(2003, "作业无法找到"),
	ACTION_NOT_FOUND(2004, "预约已经存在"),
	TARGET_NOT_FOUND(2005, "预约无法取消"),
	
	INTERNAL_ERROR(2006, "预约已满，无法预约"),
	REQ_TIME_OUT(2012, "验证不存在"),
	WS_CONN_ERROR(2013, "验证码不匹配"),
	INTERNAL_CONN_ERROR(9999, "网络超时，无法连接"),


	UNKNOWN_ERR(10000, "Unknown error");

	
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
