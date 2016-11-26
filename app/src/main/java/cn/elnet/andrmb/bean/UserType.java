package cn.elnet.andrmb.bean;

public class UserType {
	private int userId;
	private String name;
	private String password;
	private String realName;
	private String phone;
	private String email;
	private String lastSecToken;
	private String lastLoginTime;
	private String cardId;

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	private String vcode;

	
	
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	private String userRole;
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLastSecToken() {
		return lastSecToken;
	}
	public void setLastSecToken(String lastSecToken) {
		this.lastSecToken = lastSecToken;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public UserType(int userId, String name, String password, String realName,
			String phone, String email, String lastSecToken,
			String lastLoginTime,String cardId) {
		super();
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.realName = realName;
		this.phone = phone;
		this.email = email;
		this.lastSecToken = lastSecToken;
		this.lastLoginTime = lastLoginTime;
		this.cardId=cardId;
	}
	@Override
	public String toString() {
		return "UserType [userId=" + userId + ", name=" + name + ", password="
				+ password + ", realName=" + realName + ", phone=" + phone
				+ ", email=" + email + ", lastSecToken=" + lastSecToken
				+ ", lastLoginTime=" + lastLoginTime + ",cardId="+cardId+"]";
	}
	
}
