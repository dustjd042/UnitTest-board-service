package com.yeonseong.board.construct;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardResponsePopupMessage {

  /* 공통 */
  EMPTY("")
  , BAD_REQUEST("요청 데이터가 올바르지 않습니다.")
  , UNAUTHORIZED("사용자 정보가 만료되었습니다.")
  , ERROR("시스템에 일시적인 오류가 발생했습니다.")

  /* 회원 */
  , DUPLICATE_ACCOUNT("이미 가입된 회원 계정 입니다.")
  , DUPLICATE_EMAIL("이미 가입된 회원 이메일 입니다.")
  , NOT_FOUND_MEMBER("회원 정보가 존재하지 않습니다.")

  /* 게시판 */
  , NOT_FOUND_BOARD("게시글 정보가 존재하지 않습니다.")
  , UNAUTHORIZED_BOARD("해당 게시글 수정 권한이 없습니다.")

  /* 댓글 */
  , NOT_FOUND_COMMENT("댓글 정보가 존재하지 않습니다.")
  , UNAUTHORIZED_COMMENT("해당 댓글 수정 권한이 없습니다.")

  /* 알림 */
  , NOT_FOUND_VERIFY_EMAIL("이메일 인증 정보가 존재하지 않습니다.")
  , TIME_OUT_VERIFY_EMAIL("이메일 인증 요청시간이 초과되었습니다.")
  ;
  private final String popupMessage;
}