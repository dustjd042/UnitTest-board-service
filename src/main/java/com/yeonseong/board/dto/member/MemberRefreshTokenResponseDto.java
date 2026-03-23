package com.yeonseong.board.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRefreshTokenResponseDto {

  private String grantType;

  private String accessToken;

  private Long accessTokenExpireDt;

  private String refreshToken;

  private Long refreshTokenExpireDt;

}
