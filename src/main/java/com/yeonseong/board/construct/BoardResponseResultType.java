package com.yeonseong.board.construct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardResponseResultType {

  SUCCESS(HttpStatus.OK, "0000", "요청 처리 성공")
  , FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "9000", "요청 처리 실패")
  , BAD_REQUEST(HttpStatus.BAD_REQUEST, "9400", "잘못된 요청")
  , UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "9401", "회원 인증 실패")
  , ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "서버 에러 발생")
  ;

  private final HttpStatus httpStatus;
  private final String result;
  private final String resultMessage;
}
