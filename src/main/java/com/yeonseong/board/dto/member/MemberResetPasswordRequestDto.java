package com.yeonseong.board.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResetPasswordRequestDto {

  private String email;

  private String authCode;

}
