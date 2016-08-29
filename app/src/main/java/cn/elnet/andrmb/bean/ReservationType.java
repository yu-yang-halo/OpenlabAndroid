package cn.elnet.andrmb.bean;

public class ReservationType {
	private int resvId;
	private String userName;
	private String startTime;
	private String endTime;
	private int deskNum;
	private int labId;
	private  int status;
	private String cancelTime;
	public int getResvId() {
		return resvId;
	}
	public void setResvId(int resvId) {
		this.resvId = resvId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getDeskNum() {
		return deskNum;
	}
	public void setDeskNum(int deskNum) {
		this.deskNum = deskNum;
	}
	public int getLabId() {
		return labId;
	}
	public void setLabId(int labId) {
		this.labId = labId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}
	public ReservationType(int resvId, String userName, String startTime,
			String endTime, int deskNum, int labId, int status,
			String cancelTime) {
		super();
		this.resvId = resvId;
		this.userName = userName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.deskNum = deskNum;
		this.labId = labId;
		this.status = status;
		this.cancelTime = cancelTime;
	}
	@Override
	public String toString() {
		return "ReservationType [resvId=" + resvId + ", userName=" + userName
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", deskNum=" + deskNum + ", labId=" + labId + ", status="
				+ status + ", cancelTime=" + cancelTime + "]";
	}
	
}
