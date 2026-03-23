package com.yeonseong.board.entity;

import com.yeonseong.board.construct.NoticeSendHistoryStatus;
import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.util.SecurityUtils;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeSendHistoryEntity extends CommonEntity {

  private String to;

  private String title;

  private String content;

  private NoticeSendTypeCode sendType;

  private LocalDateTime sendResultDt;

  private String sendResultDetail;

  private NoticeSendHistoryStatus status;

  public NoticeSendHistoryEntity (NoticeSendTypeCode noticeSendTypeCode, String to, String title, String content) {
    setCreate(SecurityUtils.SYSTEM_MEMBER_ID);
    this.to = to;
    this.title = title;
    this.content = content;
    this.sendType = noticeSendTypeCode;
  }

  public void setResult(NoticeSendHistoryStatus noticeSendHistoryStatus, String sendResultDetail) {
    setCreate(SecurityUtils.SYSTEM_MEMBER_ID);
    this.status = noticeSendHistoryStatus;
    this.sendResultDetail = sendResultDetail;
    this.sendResultDt = LocalDateTime.now();
  }

  public String getSlacMessage() {
    return "[" + title + "]\n" + content;
  }
}
