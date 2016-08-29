/**
 * @author Administrator
 * <p>Copyright: Copyright (c) 2015 Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>Developed By: Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>最新版本的SDK</p>
 */
package cn.elnet.andrmb.elconnector;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketImpl;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import cn.elnet.andrmb.bean.AssignmentReportTurple;
import cn.elnet.andrmb.bean.AssignmentType;
import cn.elnet.andrmb.bean.CourseType;
import cn.elnet.andrmb.bean.DeskInfo;
import cn.elnet.andrmb.bean.LabInfoType;
import cn.elnet.andrmb.bean.ReportInfo;
import cn.elnet.andrmb.bean.ReservationType;
import cn.elnet.andrmb.bean.UserType;
import cn.elnet.andrmb.elconnector.util.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.elnet.andrmb.elconnector.util.IWSErrorCodeListener;
import cn.elnet.andrmb.elconnector.util.MD5Generator;
import cn.elnet.andrmb.elconnector.util.Util;

import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WSConnector {
	private static String openlabUrl = "";
	private static String authapiUrl = "";
	private static String IP1 = "202.38.78.70";//202.38.78.70
	private static String portStr = "8080";
	private static final String REQUEST_HEAD = "http://";
	private static WSConnector instance = new WSConnector();
	private Map<String, String> userMap;
	private IWSErrorCodeListener listener;

	private WSConnector() {
		this.userMap = new LinkedHashMap<String, String>();
		openlabUrl = REQUEST_HEAD + IP1 + ":" + portStr
				+ "/elws/services/openlab/";
		authapiUrl = REQUEST_HEAD + IP1 + ":" + portStr
				+ "/elws/services/authapi/";
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public String getIP1() {
		return IP1;
	}

	public static WSConnector getInstance() {
		return instance;
	}

	public static WSConnector getInstance(String ip, String port,
			boolean isHttps) {
		instance.IP1 = ip;
		instance.portStr = port;
		openlabUrl = REQUEST_HEAD + IP1 + ":" + portStr
				+ "/elws/services/openlab/";
		authapiUrl = REQUEST_HEAD + IP1 + ":" + portStr
				+ "/elws/services/authapi/";
		return instance;
	}
	public String getWebImageURL(String imageName){
		return "http://"+IP1+"/openlabweb/upload/"+imageName;
	}

	private InputStream request(String service) throws WSException {
		String path = service;
		InputStream is = null;
		HttpURLConnection uc = null;
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("GET");
			// uc.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
			// uc.setRequestProperty("Accept-Language",
			// "en,zh-cn;q=0.8,zh;q=0.5,en-us;q=0.3");
			// uc.setRequestProperty("Cache-Control", "max-age=0");
			// uc.setRequestProperty("Connection", "keep-alive");
			// uc.setRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; rv:28.0) Gecko/20100101 Firefox/28.0");
		} catch (IOException e) {
			e.printStackTrace();
		}
		uc.setConnectTimeout(Util.REQ_TIME_OUT);
		uc.setReadTimeout(Util.READ_TIME_OUT);
		uc.setDoOutput(true);
		uc.setDoInput(true);
		try {
			uc.connect();
		} catch (Exception e1) {
			throw new WSException(ErrorCode.NET_WORK_TIME_OUT);
		}
		try {
			System.out.println("rspCode=========================:"
					+ uc.getResponseCode());
			is = uc.getInputStream();
		} catch (Exception e) {
			throw new WSException(ErrorCode.NET_WORK_TIME_OUT);
		}
		return is;
	}

	private Element parseInputStreamToDom(InputStream is) {
		Logger.getLogger(this.getClass()).info("inputStream :" + is);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			is = null;
			return null;
		}
		Logger.getLogger(this.getClass()).info("document :" + document);
		Element rootElement = document.getDocumentElement();
		Logger.getLogger(this.getClass()).info(rootElement.toString());
		try {
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootElement;
	}

	public void setWSErrorCodeListener(IWSErrorCodeListener listener) {
		this.listener = listener;
	}

	private Element getPostXMLNode(String service,String params) throws WSException{
		Logger.getLogger(this.getClass()).info(service);

		InputStream is = requestPost(service,params);

		Logger.getLogger(this.getClass()).info("post inputStream :" + is);
		if (is == null) {
			throw new WSException(ErrorCode.CONN_TO_WS_ERR);
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			is = null;
			return null;
		}
		Logger.getLogger(this.getClass()).info("document :" + document);
		Element rootElement = document.getDocumentElement();

		Logger.getLogger(this.getClass()).info(rootElement.toString());
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(listener!=null){
			listener.handleErrorCode(getErrorCodeInElement(rootElement));
		}

		return rootElement;
	}


	private Element getXMLNode(String service) throws WSException {
		Logger.getLogger(this.getClass()).info(service);

		InputStream is = request(service);

		Logger.getLogger(this.getClass()).info("inputStream :" + is);
		if (is == null) {
			throw new WSException(ErrorCode.CONN_TO_WS_ERR);
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			is = null;
			return null;
		}
		Logger.getLogger(this.getClass()).info("document :" + document);
		Element rootElement = document.getDocumentElement();

		Logger.getLogger(this.getClass()).info(rootElement.toString());
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (listener != null) {
			listener.handleErrorCode(getErrorCodeInElement(rootElement));
		}

		return rootElement;
	}

	private ErrorCode getErrorCodeInElement(Element element) {

		Element errorCodeNode = element.getElementsByTagName("errorCode") != null ? (Element) element
				.getElementsByTagName("errorCode").item(0) : null;
		if (errorCodeNode != null) {
			int errorCode = Integer.parseInt(errorCodeNode.getFirstChild()
					.getNodeValue());
			return ErrorCode.get(errorCode);
		}
		return null;
	}

	/*
	 * ***********************************************************
	 * Openlab webservice 最新版本的接口 * * * authapi openlab
	 * ***********************************************************
	 */

	// openlab 接口部分

	public List<LabInfoType> getLabListByIncDesk(boolean incDesk)
			throws WSException {
		String service = WSConnector.openlabUrl + "getLabList?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken") + "&incDesk=" + incDesk;

		Logger.getLogger(this.getClass()).info("getLabList URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;

		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				List<LabInfoType> labInfoTypes = new ArrayList<LabInfoType>();

				NodeList lablistNodes = root.getElementsByTagName("lablist");
				if (lablistNodes != null && lablistNodes.getLength() > 0) {
					for (int i = 0; i < lablistNodes.getLength(); i++) {
						Element child = (Element) lablistNodes.item(i);
						Element labIdNode = (Element) child
								.getElementsByTagName("labId").item(0);
						Element nameNode = (Element) child
								.getElementsByTagName("name").item(0);
						Element descNode = (Element) child
								.getElementsByTagName("desc").item(0);
						Element numOfDeskNode = (Element) child
								.getElementsByTagName("numOfDesk").item(0);
						Element buildingNode = (Element) child
								.getElementsByTagName("building").item(0);
						Element floorNode = (Element) child
								.getElementsByTagName("floor").item(0);
						Element roomNode = (Element) child
								.getElementsByTagName("room").item(0);

						int labId = Integer.parseInt(labIdNode.getFirstChild()
								.getNodeValue());
						String name = nameNode.getFirstChild() == null ? ""
								: nameNode.getFirstChild().getNodeValue();
						String desc = descNode.getFirstChild() == null ? ""
								: descNode.getFirstChild().getNodeValue();
						int numOfDesk = Integer.parseInt(numOfDeskNode
								.getFirstChild().getNodeValue());
						String building = buildingNode.getFirstChild() == null ? ""
								: buildingNode.getFirstChild().getNodeValue();
						short floor = Short.parseShort(floorNode
								.getFirstChild().getNodeValue());
						short room = Short.parseShort(roomNode.getFirstChild()
								.getNodeValue());
						LabInfoType labInfo = new LabInfoType(labId, name,
								desc, numOfDesk, building, floor, room);
						if (incDesk) {
							NodeList DeskInfoNodes = root
									.getElementsByTagName("DeskInfo");
							if (DeskInfoNodes != null
									&& DeskInfoNodes.getLength() > 0) {
								List<DeskInfo> deskInfos = new ArrayList<DeskInfo>();

								for (int j = 0; j < DeskInfoNodes.getLength(); j++) {
									Element child2 = (Element) DeskInfoNodes
											.item(j);
									Element deskNumNode = (Element) child2
											.getElementsByTagName("deskNum")
											.item(0);
									Element labIdNode2 = (Element) child2
											.getElementsByTagName("labId")
											.item(0);
									Element typeNode = (Element) child2
											.getElementsByTagName("type").item(
													0);
									Element descNode2 = (Element) child2
											.getElementsByTagName("desc").item(
													0);
									int deskNum = Integer.parseInt(deskNumNode
											.getFirstChild().getNodeValue());
									int labId2 = Integer.parseInt(labIdNode2
											.getFirstChild().getNodeValue());
									int type = Integer.parseInt(typeNode
											.getFirstChild().getNodeValue());
									String desc2 = descNode2.getFirstChild() == null ? ""
											: descNode2.getFirstChild()
													.getNodeValue();

									DeskInfo deskInfo = new DeskInfo(deskNum,
											labId2, type, desc2);
									deskInfos.add(deskInfo);
								}
								/*
								 * 排序 升序排
								 */
								Collections.sort(deskInfos,new Comparator<DeskInfo>() {
									@Override
									public int compare(DeskInfo arg0, DeskInfo arg1) {
										return arg0.getDeskNum()-arg1.getDeskNum();
									}
								});
								
								labInfo.setDeskInfos(deskInfos);
							}
						}
						labInfoTypes.add(labInfo);

					}

					return labInfoTypes;

				}

			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		}
		return null;

	}
	/**
	 *  获取当前用户课程列表
	 */
	public List<CourseType> getLabCourseList() throws WSException {
		String service = WSConnector.openlabUrl
				+ "getLabCourseList?senderId=" + this.userMap.get("userId")
				+ "&secToken=" + this.userMap.get("secToken")+"&year=1&semester=1";


		Logger.getLogger(this.getClass()).info(
				"getLabCourseList URL" + service);
		Element root = getXMLNode(service);

		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}

		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;


		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if(errorCode==ErrorCode.ACCEPT.getCode()){
				List<CourseType> courseTypes = new ArrayList<CourseType>();

				NodeList courselistNodes = root.getElementsByTagName("courselist");
				if (courselistNodes != null && courselistNodes.getLength() > 0) {
					for (int i = 0; i < courselistNodes.getLength(); i++) {
						Element child = (Element) courselistNodes.item(i);
						Element courseCodeNode = (Element) child
								.getElementsByTagName("courseCode").item(0);
						Element nameNode = (Element) child
								.getElementsByTagName("name").item(0);
						Element semesterNode = (Element) child
								.getElementsByTagName("semester").item(0);
						Element yearNode = (Element) child
								.getElementsByTagName("year").item(0);

						String name = nameNode.getFirstChild() == null ? ""
								: nameNode.getFirstChild().getNodeValue();
						String courseCode = courseCodeNode.getFirstChild() == null ? ""
								: courseCodeNode.getFirstChild().getNodeValue();
						int  semester = Integer.parseInt(semesterNode.getFirstChild().getNodeValue());
						int  year = Integer.parseInt(yearNode.getFirstChild().getNodeValue());

						CourseType courseType=new CourseType(courseCode,
								             name,"",year,
								             CourseType.Semester.value(semester),
								             null);

						courseTypes.add(courseType);
					}

					return courseTypes;
				}

			}

		}


        return null;
	}

	public int AddOrUpdAssignment(int asId, String courseCode, String desc,
			String dueDate) throws WSException {
		String service = WSConnector.openlabUrl
				+ "AddOrUpdAssignment?senderId=" + this.userMap.get("userId")
				+ "&secToken=" + this.userMap.get("secToken") + "&asId=" + asId
				+ "&courseCode=" + courseCode + "&desc=" + desc;
		if (dueDate != null) {
			service += "&dueDate=" + dueDate;
		}

		Logger.getLogger(this.getClass()).info(
				"AddOrUpdAssignment URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}

		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;
		Element asIdNode = root.getElementsByTagName("asId") != null ? (Element) root
				.getElementsByTagName("asId").item(0) : null;

		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				asId = Integer
						.parseInt(asIdNode.getFirstChild().getNodeValue());

				return asId;
			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		}
		return 0;

	}

	private String parseGetUrlString(String service){
	   int startIndex=service.indexOf("?");
	   String params=service.substring(startIndex);

	   return params;
	}

	private InputStream requestPost(String service,String params) throws WSException{
		Log.v("Post Params","post pramas "+params);
		InputStream is = null;
		HttpURLConnection uc = null;
		URL url = null;
		try {
			url = new URL(service);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}


		try {
			uc = (HttpURLConnection) url.openConnection();
			uc.setRequestProperty("accept", "*/*");
			uc.setRequestProperty("connection", "Keep-Alive");
			uc.setRequestProperty("Content-Length",
					String.valueOf(params.length()));
			uc.setRequestMethod("POST");
			// uc.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
			// uc.setRequestProperty("Accept-Language",
			// "en,zh-cn;q=0.8,zh;q=0.5,en-us;q=0.3");
			// uc.setRequestProperty("Cache-Control", "max-age=0");
			// uc.setRequestProperty("Connection", "keep-alive");
			// uc.setRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; rv:28.0) Gecko/20100101 Firefox/28.0");

		} catch (IOException e) {
			e.printStackTrace();
		}

		uc.setDoOutput(true);
		uc.setDoInput(true);
		try {
			uc.connect();
			// 建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
			dos.writeBytes(params);
			dos.flush();
			dos.close();

		} catch (Exception e1) {
			throw new WSException(ErrorCode.NET_WORK_TIME_OUT);
		}
		try {
			System.out.println("rspCode=========================:"
					+ uc.getResponseCode());
			is = uc.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WSException(ErrorCode.NET_WORK_TIME_OUT);
		}
		return is;
	}




	public int submitReport(String courseCode, String file, String desc,
			int assignmentId) throws WSException, UnsupportedEncodingException {
		String service = WSConnector.openlabUrl + "submitReport";
		StringBuffer params=new StringBuffer();


		params.append("desc=" + URLEncoder.encode(desc, "UTF-8"));
		params.append("&assignmentId=" + assignmentId);
		params.append("&senderId=" + this.userMap.get("userId"));
		params.append("&secToken="+ this.userMap.get("secToken"));
		params.append("&courseCode=" + courseCode);
		params.append("&file=" + URLEncoder.encode(file,"utf-8"));


		Logger.getLogger(this.getClass()).info("submitReport URL"
				+ service+"?"+params.toString());
		Element root = getPostXMLNode(service,params.toString());
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}

		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;
		Element reportIdNode = root.getElementsByTagName("reportId") != null ? (Element) root
				.getElementsByTagName("reportId").item(0) : null;

		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				if(reportIdNode
						!=null){
					int reportId = Integer.parseInt(reportIdNode.getFirstChild()
							.getNodeValue());

					return reportId;
				}else{
					return -1;
				}

			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		}
		return 0;

	}

	public List<ReservationType> getReservationList(String name)
			throws WSException {
		String service = WSConnector.openlabUrl
				+ "getReservationList?senderId=" + this.userMap.get("userId")
				+ "&secToken=" + this.userMap.get("secToken") + "&name=" + name;

		Logger.getLogger(this.getClass()).info(
				"getReservationList URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}

		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;

		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				List<ReservationType> reservationTypes = new ArrayList<ReservationType>();

				NodeList reservationNodes = root
						.getElementsByTagName("reservation");
				if (reservationNodes != null
						&& reservationNodes.getLength() > 0) {
					for (int i = 0; i < reservationNodes.getLength(); i++) {
						Element child = (Element) reservationNodes.item(i);
						Element resvIdNode = (Element) child
								.getElementsByTagName("resvId").item(0);
						Element userNameNode = (Element) child
								.getElementsByTagName("userName").item(0);
						Element startTimeNode = (Element) child
								.getElementsByTagName("startTime").item(0);
						Element endTimeNode = (Element) child
								.getElementsByTagName("endTime").item(0);
						Element deskNumNode = (Element) child
								.getElementsByTagName("deskNum").item(0);
						Element labIdNode = (Element) child
								.getElementsByTagName("labId").item(0);
						Element statusNode = (Element) child
								.getElementsByTagName("status").item(0);
						Element cancelTimeNode = (Element) child
								.getElementsByTagName("cancelTime").item(0);

						int resvId = Integer.parseInt(resvIdNode
								.getFirstChild().getNodeValue());
						int labId = Integer.parseInt(labIdNode.getFirstChild()
								.getNodeValue());
						int status = Integer.parseInt(statusNode
								.getFirstChild().getNodeValue());
						int deskNum = Integer.parseInt(deskNumNode
								.getFirstChild().getNodeValue());

						String userName = userNameNode.getFirstChild()
								.getNodeValue();
						String startTime = startTimeNode.getFirstChild()
								.getNodeValue();
						String endTime = endTimeNode.getFirstChild()
								.getNodeValue();
						String cancelTime = cancelTimeNode == null ? ""
								: cancelTimeNode.getFirstChild().getNodeValue();

						ReservationType reservationType = new ReservationType(
								resvId, userName, startTime, endTime, deskNum,
								labId, status, cancelTime);
						reservationTypes.add(reservationType);

					}
					return reservationTypes;
				}

			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		}
		return null;
	}

	public AssignmentReportTurple getAassignmentList(String courseCode)
			throws WSException {
		AssignmentReportTurple turple=new AssignmentReportTurple();
		String service = WSConnector.openlabUrl
				+ "getAassignmentList?senderId=" + this.userMap.get("userId")
				+ "&secToken=" + this.userMap.get("secToken")+"&userId="+this.userMap.get("userId");
		if (courseCode != null) {
			service += "&courseCode=" + courseCode;
		}

		Logger.getLogger(this.getClass()).info(
				"getAassignmentList URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}

		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;

		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				List<AssignmentType> assignmentTypes = new ArrayList<AssignmentType>();
				List<ReportInfo> reportInfos = new ArrayList<ReportInfo>();
				NodeList assignmentListNodes = root
						.getElementsByTagName("assignmentList");

				NodeList reportListNodes = root
						.getElementsByTagName("reportList");

				if (assignmentListNodes != null
						&& assignmentListNodes.getLength() > 0) {
					for (int i = 0; i < assignmentListNodes.getLength(); i++) {
						Element child = (Element) assignmentListNodes.item(i);
						Element idNode = (Element) child.getElementsByTagName(
								"id").item(0);
						Element courseCodeNode = (Element) child
								.getElementsByTagName("courseCode").item(0);
						Element descNode = (Element) child
								.getElementsByTagName("desc").item(0);
						Element dueDateNode = (Element) child
								.getElementsByTagName("dueDate").item(0);
						Element createdTimeNode = (Element) child
								.getElementsByTagName("createdTime").item(0);
						Element createdByNode = (Element) child
								.getElementsByTagName("createdBy").item(0);

						int id = Integer.parseInt(idNode.getFirstChild()
								.getNodeValue());
						int createdBy = Integer.parseInt(createdByNode
								.getFirstChild().getNodeValue());

						courseCode = courseCodeNode.getFirstChild()
								.getNodeValue();
						String desc = descNode.getFirstChild().getNodeValue();
						String dueDate = dueDateNode.getFirstChild()
								.getNodeValue();
						String createdTime = createdTimeNode.getFirstChild()
								.getNodeValue();
						AssignmentType assignmentType = new AssignmentType(id,
								courseCode, desc, dueDate, createdTime,
								createdBy);
						assignmentTypes.add(assignmentType);
					}
					turple.setAssignmentTypeList(assignmentTypes);
				}

				if (reportListNodes != null
						&& reportListNodes.getLength() > 0) {
					for (int i = 0; i < reportListNodes.getLength(); i++) {
						Element child = (Element) reportListNodes.item(i);
						Element reportIdNode = (Element) child.getElementsByTagName(
								"reportId").item(0);
						Element courseCodeNode = (Element) child
								.getElementsByTagName("courseCode").item(0);
						Element descriptionNode = (Element) child
								.getElementsByTagName("description").item(0);
						Element userIdNode = (Element) child
								.getElementsByTagName("userId").item(0);
						Element assignmentIdNode = (Element) child
								.getElementsByTagName("assignmentId").item(0);
						Element fileNameNode = (Element) child
								.getElementsByTagName("attachFileName").item(0);
						Element submitTimeNode = (Element) child
								.getElementsByTagName("submitTime").item(0);

						int reportId = Integer.parseInt(reportIdNode.getFirstChild()
								.getNodeValue());
						int userId = Integer.parseInt(userIdNode
								.getFirstChild().getNodeValue());
						int assignmentId = Integer.parseInt(assignmentIdNode
								.getFirstChild().getNodeValue());
						courseCode = courseCodeNode.getFirstChild()
								.getNodeValue();

						String description = descriptionNode.getFirstChild()!=null?descriptionNode.getFirstChild()
								.getNodeValue():"";
						String fileName = fileNameNode.getFirstChild()
								.getNodeValue();
						String submitTime = submitTimeNode.getFirstChild()
								.getNodeValue();
						ReportInfo reportInfo=new ReportInfo(reportId,userId,assignmentId,courseCode,description,fileName,submitTime);

						reportInfos.add(reportInfo);
					}
					turple.setAssignmentTypeList(assignmentTypes);
					turple.setReportInfoList(reportInfos);
				}

			}
		}
		return turple;

	}

	public boolean addOrUpdReservation(String userName, String startTime,
			String endTime, int deskNum, int labId, int status, int resvId)
			throws WSException, UnsupportedEncodingException {
		String service = WSConnector.openlabUrl
				+ "addOrUpdReservation?senderId=" + this.userMap.get("userId")
				+ "&secToken=" + this.userMap.get("secToken") +"&userId=" + this.userMap.get("userId")+ "&userName="
				+ userName + "&startTime="
				+ URLEncoder.encode(startTime, "UTF-8") + "&endTime="
				+ URLEncoder.encode(endTime, "UTF-8") + "&deskNum=" + deskNum
				+ "&labId=" + labId + "&status=" + status + "&resvId=" + resvId;

		Logger.getLogger(this.getClass()).info(
				"addOrUpdReservation URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;

		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());

			if (errorCode == ErrorCode.ACCEPT.getCode()) {

				return true;
			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		}
		return false;

	}

	// authapi 接口部分

	/*
	 * 用户登录
	 */

	public void login(String name, String password) throws WSException {
		syslogin(name, MD5Generator.reverseMD5Value(password));
	}

	private void syslogin(String name, String password) throws WSException {
		Element element = null;
		String service = WSConnector.authapiUrl + "login?name=" + name
				+ "&password=" + password;
		Logger.getLogger(this.getClass()).info("[login] service = " + service);
		element = getXMLNode(service);

		if (element != null) {
			Element errCodeNode = element.getElementsByTagName("errorCode") != null ? (Element) element
					.getElementsByTagName("errorCode").item(0) : null;
			Element errMsgNode = element.getElementsByTagName("errorMsg") != null ? (Element) element
					.getElementsByTagName("errorMsg").item(0) : null;
			Element userIdNode = element.getElementsByTagName("userId") != null ? (Element) element
					.getElementsByTagName("userId").item(0) : null;
			Element secTokenNode = element.getElementsByTagName("secToken") != null ? (Element) element
					.getElementsByTagName("secToken").item(0) : null;

			if (errCodeNode != null) {
				int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
						.getNodeValue());
				if (errorCode == ErrorCode.ACCEPT.getCode()) {
					this.userMap.put("loginName", name);
					this.userMap.put("password", password);
					if (userIdNode != null) {
						this.userMap.put("userId", userIdNode.getFirstChild()
								.getNodeValue());
					}
					if (secTokenNode != null
							&& secTokenNode.getFirstChild() != null) {
						this.userMap.put("secToken", secTokenNode
								.getFirstChild().getNodeValue());
					}
					Logger.getLogger(this.getClass()).info(
							"[login]  value = " + this.userMap.toString());
				} else {
					throw new WSException(ErrorCode.get(errorCode));
				}
			} else {
				throw new WSException(ErrorCode.CONN_TO_WS_ERR);
			}
		} else {
			throw new WSException(ErrorCode.LOGIN_FAILED);
		}
	}

	public UserType getUser() throws WSException {
		String service = WSConnector.authapiUrl + "getUser?senderId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken") + "&userId="
				+ this.userMap.get("userId");
		Logger.getLogger(this.getClass()).info("getUser URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				List<AssignmentType> assignmentTypes = new ArrayList<AssignmentType>();
				NodeList userListNodes = root.getElementsByTagName("user");
				if (userListNodes != null && userListNodes.getLength() > 0) {
					for (int i = 0; i < userListNodes.getLength(); i++) {
						Element ele = (Element) userListNodes.item(i);
						Element userIdNode = ele.getElementsByTagName("userId") != null ? (Element) ele
								.getElementsByTagName("userId").item(0) : null;
						Element nameNode = ele.getElementsByTagName("name") != null ? (Element) ele
								.getElementsByTagName("name").item(0) : null;
						Element passwordNode = ele
								.getElementsByTagName("password") != null ? (Element) ele
								.getElementsByTagName("password").item(0)
								: null;
						Element realNameNode = ele
								.getElementsByTagName("realName") != null ? (Element) ele
								.getElementsByTagName("realName").item(0)
								: null;
						Element phoneNode = ele.getElementsByTagName("phone") != null ? (Element) ele
								.getElementsByTagName("phone").item(0) : null;
						Element emailNode = ele.getElementsByTagName("email") != null ? (Element) ele
								.getElementsByTagName("email").item(0) : null;
						Element lastSecTokenNode = ele
								.getElementsByTagName("lastSecToken") != null ? (Element) ele
								.getElementsByTagName("lastSecToken").item(0)
								: null;
						Element lastLoginTimeNode = ele
								.getElementsByTagName("lastLoginTime") != null ? (Element) ele
								.getElementsByTagName("lastLoginTime").item(0)
								: null;
						Element cardIdNode = ele.getElementsByTagName("cardId") != null ? (Element) ele
								.getElementsByTagName("cardId").item(0) : null;
						int userId = -1;
						String cardId = "", name = "", password = "", realName = "", phone = "", email = "", lastSecToken = "", lastLoginTime = "";
						userId = Integer.parseInt(userIdNode.getFirstChild()
								.getNodeValue());
						cardId =cardIdNode==null?"":cardIdNode.getFirstChild().getNodeValue();
						name =nameNode==null?"":nameNode.getFirstChild().getNodeValue();
						password = passwordNode==null?"":passwordNode.getFirstChild().getNodeValue();
						realName =realNameNode==null?"": realNameNode.getFirstChild().getNodeValue();
						phone =phoneNode==null?"": phoneNode.getFirstChild().getNodeValue();
						email = emailNode==null?"":emailNode.getFirstChild().getNodeValue();
						lastSecToken =lastSecTokenNode==null?"": lastSecTokenNode.getFirstChild()
								.getNodeValue();
						lastLoginTime =lastLoginTimeNode==null?"": lastLoginTimeNode.getFirstChild()
								.getNodeValue();

						UserType user = new UserType(userId, name, password,
								realName, phone, email, lastSecToken,
								lastLoginTime, cardId);

						return user;
					}
				} else {
					throw new WSException(ErrorCode.get(errorCode));
				}
			}
		}
		return null;

	}

	/*
	 * 用户登出
	 */
	public boolean logout() throws WSException {
		String service = WSConnector.authapiUrl + "logout?userId="
				+ this.userMap.get("userId") + "&secToken="
				+ this.userMap.get("secToken");

		Logger.getLogger(this.getClass()).info("logout URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;
		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				return true;
			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		}

		return false;

	}

	public int createUser(UserType userType) throws WSException,
			UnsupportedEncodingException {
		String service = WSConnector.authapiUrl + "createUser?senderId=0"+ "&secToken=0&name=" + userType.getName()
				+ "&password="
				+ MD5Generator.reverseMD5Value(userType.getPassword());

		if (userType.getRealName() != null) {
			service += "&realName="
					+ URLEncoder.encode(userType.getRealName(), "UTF-8");
		}
		if (userType.getEmail() != null) {
			service += "&email=" + userType.getEmail();
		}
		if (userType.getPhone() != null) {
			service += "&phone=" + userType.getPhone();
		}
		if (userType.getUserRole() != null) {
			service += "&userRole=" + userType.getUserRole();
		}
		if (userType.getCardId() != null) {
			service += "&cardId=" + userType.getCardId();
		}

		Logger.getLogger(this.getClass()).info("createUser URL" + service);
		Element root = getXMLNode(service);
		if (root == null) {
			throw new WSException(ErrorCode.REJECT);
		}
		Element errCodeNode = root.getElementsByTagName("errorCode") != null ? (Element) root
				.getElementsByTagName("errorCode").item(0) : null;
		Element errMsgNode = root.getElementsByTagName("errorMsg") != null ? (Element) root
				.getElementsByTagName("errorMsg").item(0) : null;
		Element userIdNode = root.getElementsByTagName("userId") != null ? (Element) root
				.getElementsByTagName("userId").item(0) : null;

		if (errCodeNode != null) {
			int errorCode = Integer.parseInt(errCodeNode.getFirstChild()
					.getNodeValue());
			if (errorCode == ErrorCode.ACCEPT.getCode()) {
				int userId = Integer.parseInt(userIdNode.getFirstChild()
						.getNodeValue());
				return userId;
			} else {
				throw new WSException(ErrorCode.get(errorCode));
			}
		}
		return 0;
	}

}
