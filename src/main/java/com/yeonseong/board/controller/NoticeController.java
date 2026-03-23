package com.yeonseong.board.controller;

import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.dto.BoardResponseDto;
import com.yeonseong.board.dto.notice.NoticeSendVerifyEmailRequestDto;
import com.yeonseong.board.dto.notice.NoticeVerifyEmailRequestDto;
import com.yeonseong.board.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

  private final NoticeService noticeService;

  /**
   * 이메일 인증 번호 발송
   */
  @PostMapping("/verify-email")
  public ResponseEntity<BoardResponseDto<Object>> sendVerifyEmail(@Valid @RequestBody NoticeSendVerifyEmailRequestDto noticeSendVerifyEmailRequestDto) {
    noticeService.sendVerifyEmail(noticeSendVerifyEmailRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }

  /**
   * 이메일 인증
   */
  @PatchMapping("/verify-email")
  public ResponseEntity<BoardResponseDto<Object>> verifyEmail(@Valid @RequestBody NoticeVerifyEmailRequestDto noticeVerifyEmailRequestDto) {
    noticeService.verifyEmail(noticeVerifyEmailRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }
}
