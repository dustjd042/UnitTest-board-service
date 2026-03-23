package com.yeonseong.board.construct;

public enum NoticeSendTypeCode {

  EMAIL("emailSendService")
  , SLACK("slackSendService")
  ;

  private final String beanName;

  NoticeSendTypeCode(String beanName) {
    this.beanName = beanName;
  }

  public String getBeanName() {
    return beanName;
  }
}
