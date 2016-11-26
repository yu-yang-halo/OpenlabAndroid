package cn.elnet.andrmb.elconnector.util;

import cn.elnet.andrmb.elconnector.ErrorCode;

public interface IWSErrorCodeListener {
  public void handleErrorCode(ErrorCode errorcode);
  public void handleMessage(String message);
}
