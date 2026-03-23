package com.yeonseong.board.construct;

public enum SlackChannel {

  SYSTEM_ALERT("시스템-알림")
  , INTERNAL_NOTICE("사내-공지")
  ;

  private final String channel;

  SlackChannel(String channel) {
    this.channel = channel;
  }

  public String getChannel() {
    return channel;
  }
}
