package com.yeonseong.board.entity;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.construct.VerifyEmailStatusCode;
import com.yeonseong.board.dto.notice.NoticeSendVerifyEmailRequestDto;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.util.SecurityUtils;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerifyEmailEntity extends CommonEntity {

  private String email;

  private String verifyCode;

  private LocalDateTime verifyRequestDt;

  private VerifyEmailStatusCode status;

  public VerifyEmailEntity (NoticeSendVerifyEmailRequestDto noticeSendVerifyEmailRequestDto, String verifyCode) {
    setCreate(SecurityUtils.SYSTEM_MEMBER_ID);
    this.email = noticeSendVerifyEmailRequestDto.getEmail();
    this.verifyCode = verifyCode;
    this.verifyRequestDt = LocalDateTime.now();
    this.status = VerifyEmailStatusCode.SEND;
  }

  public void verifyEmail() {
    setCreate(SecurityUtils.SYSTEM_MEMBER_ID);
    this.status = VerifyEmailStatusCode.AUTHORIZE;
    if (LocalDateTime.now().isAfter(verifyRequestDt.plusMinutes(5))) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.TIME_OUT_VERIFY_EMAIL);
    }
  }

  public void verifyEmailCheck() {
    if (status != VerifyEmailStatusCode.AUTHORIZE
        || LocalDateTime.now().isAfter(this.verifyRequestDt.plusMinutes(5))) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_VERIFY_EMAIL);
    }
  }

}
