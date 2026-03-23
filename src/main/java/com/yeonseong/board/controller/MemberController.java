package com.yeonseong.board.controller;

import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.dto.BoardResponseDto;
import com.yeonseong.board.dto.member.MemberFindNameRequestDto;
import com.yeonseong.board.dto.member.MemberFindNameResponseDto;
import com.yeonseong.board.dto.member.MemberJoinRequestDto;
import com.yeonseong.board.dto.member.MemberLoginRequestDto;
import com.yeonseong.board.dto.member.MemberLoginResponseDto;
import com.yeonseong.board.dto.member.MemberRefreshTokenRequestDto;
import com.yeonseong.board.dto.member.MemberRefreshTokenResponseDto;
import com.yeonseong.board.dto.member.MemberResetPasswordRequestDto;
import com.yeonseong.board.dto.member.MemberResetPasswordResponseDto;
import com.yeonseong.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

  private final MemberService memberService;

  /**
   * 회원가입
   */
  @PostMapping("")
  public ResponseEntity<BoardResponseDto<Object>> join(@Valid @RequestBody MemberJoinRequestDto memberJoinRequestDto) {
    memberService.join(memberJoinRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }

  /**
   * 이름(아이디) 찾기
   */
  @GetMapping("/name")
  public ResponseEntity<BoardResponseDto<MemberFindNameResponseDto>> findName(@Valid MemberFindNameRequestDto memberFindNameRequestDto) {
    MemberFindNameResponseDto memberFindNameResponseDto = memberService.findName(memberFindNameRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, memberFindNameResponseDto));
  }

  /**
   * 비밀번호 초기화
   */
  @PatchMapping("/password")
  public ResponseEntity<BoardResponseDto<MemberResetPasswordResponseDto>> resetPassword(@Valid @RequestBody MemberResetPasswordRequestDto memberResetPasswordRequestDto) {
    MemberResetPasswordResponseDto memberResetPasswordResponseDto = memberService.resetPassword(memberResetPasswordRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, memberResetPasswordResponseDto));
  }

  /**
   * 탈퇴
   */
  @DeleteMapping("")
  public ResponseEntity<BoardResponseDto<Object>> deleteMember() {
    memberService.deleteMember();
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }

  /**
   * 로그인
   */
  @PostMapping("/login")
  public ResponseEntity<BoardResponseDto<MemberLoginResponseDto>> login(@Valid @RequestBody MemberLoginRequestDto memberLoginRequestDto) {
    MemberLoginResponseDto memberLoginResponseDto = memberService.login(memberLoginRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, memberLoginResponseDto));
  }

  /**
   * 토큰 갱신
   */
  @PatchMapping("/token")
  public ResponseEntity<BoardResponseDto<MemberRefreshTokenResponseDto>> refreshToken(@Valid @RequestBody MemberRefreshTokenRequestDto memberRefreshTokenRequestDto) {
    MemberRefreshTokenResponseDto memberRefreshTokenResponseDto = memberService.refreshToken(memberRefreshTokenRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, memberRefreshTokenResponseDto));
  }

  /**
   * 로그아웃
   */
  @DeleteMapping("/logout")
  public ResponseEntity<BoardResponseDto<Object>> logout() {
    memberService.logout();
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }
}