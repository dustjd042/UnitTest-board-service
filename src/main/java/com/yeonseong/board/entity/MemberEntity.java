package com.yeonseong.board.entity;

import com.yeonseong.board.config.JwtProvider;
import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.construct.MemberRoleCode;
import com.yeonseong.board.construct.MemberStatusCode;
import com.yeonseong.board.dto.member.MemberJoinRequestDto;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.util.SecurityUtils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends CommonEntity {

  private String name;

  private String password;

  private String email;

  private String intro;

  private String accessToken;

  private String refreshToken;

  private LocalDateTime refreshTokenExpireDt;

  private LocalDateTime loginDt;

  private MemberRoleCode role;

  private MemberStatusCode status;

  public MemberEntity (PasswordEncoder passwordEncoder, MemberJoinRequestDto memberJoinRequestDto) {
    setCreate(SecurityUtils.SYSTEM_MEMBER_ID);
    this.name = memberJoinRequestDto.getName();
    this.password = passwordEncoder.encode(memberJoinRequestDto.getPassword());
    this.email = memberJoinRequestDto.getEmail();
    this.intro = memberJoinRequestDto.getIntro();
    this.role = MemberRoleCode.USER;
    this.status = MemberStatusCode.ACTIVE;
  }

  public void login(PasswordEncoder passwordEncoder, String password, JwtProvider jwtProvider) {

    if (!passwordEncoder.matches(password, this.getPassword())) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER);
    }

    if (this.status != MemberStatusCode.ACTIVE) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER);
    }

    token(jwtProvider);
  }

  public void token(JwtProvider jwtProvider) {
    setModify(getId());
    this.accessToken = jwtProvider.createToken(getId(), jwtProvider.ACCESS_TOKEN_EXPIRE);
    this.refreshToken = jwtProvider.createToken(getId(), jwtProvider.REFRESH_TOKEN_EXPIRE);
    this.refreshTokenExpireDt = LocalDateTime.ofInstant(Instant.now().plusMillis(jwtProvider.REFRESH_TOKEN_EXPIRE), ZoneId.systemDefault());
    this.loginDt = LocalDateTime.now();
  }

  public void logout() {
    setModify(getId());
    this.accessToken = null;
    this.refreshToken = null;
    this.refreshTokenExpireDt = null;
  }

  public String resetPassword(PasswordEncoder passwordEncoder) {
    setModify(getId());
    String newPassword = UUID.randomUUID().toString().substring(0, 8);
    this.password = passwordEncoder.encode(newPassword);
    return newPassword;
  }

  public void deleteMember() {
    setModify(getId());
    this.status = MemberStatusCode.LEAVE;
  }
}