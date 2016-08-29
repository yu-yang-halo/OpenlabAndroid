package cn.elnet.andrmb.bean;

public class DeskInfo {
	private int deskNum;
	private int labId;
	private int type;
	private String desc;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public DeskInfo(int deskNum, int labId, int type, String desc) {
		super();
		this.deskNum = deskNum;
		this.labId = labId;
		this.type = type;
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "DeskInfo [deskNum=" + deskNum + ", labId=" + labId + ", type="
				+ type + ", desc=" + desc + "]";
	}

}
