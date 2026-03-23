package com.yeonseong.board.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeVerifyEmailRequestDto {

  private String email;

  private String verifyCode;

}

