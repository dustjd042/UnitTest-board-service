package com.yeonseong.board.controller;

import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.dto.BoardResponseDto;
import com.yeonseong.board.dto.comment.CommentCreateRequestDto;
import com.yeonseong.board.dto.comment.CommentCreateResponseDto;
import com.yeonseong.board.dto.comment.CommentGetListRequestDto;
import com.yeonseong.board.dto.comment.CommentGetListResponseDto;
import com.yeonseong.board.dto.comment.CommentUpdateRequestDto;
import com.yeonseong.board.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;

  /**
   * 댓글 리스트 조회
   */
  @GetMapping("/list")
  public ResponseEntity<BoardResponseDto<CommentGetListResponseDto>> getList(
      @ModelAttribute CommentGetListRequestDto commentGetListRequestDto) {

    CommentGetListResponseDto commentGetListResponseDto = commentService.getList(commentGetListRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, commentGetListResponseDto));
  }

  /**
   * 댓글 등록
   */
  @PostMapping("")
  public ResponseEntity<BoardResponseDto<CommentCreateResponseDto>> create(
      @Valid @RequestBody CommentCreateRequestDto commentCreateRequestDto) {

    CommentCreateResponseDto commentCreateResponseDto = commentService.create(commentCreateRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, commentCreateResponseDto));
  }

  /**
   * 댓글 수정
   */
  @PutMapping("/{commentId}")
  public ResponseEntity<BoardResponseDto<Object>> update(
      @PathVariable Long commentId
      , @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {

    commentService.update(commentId, commentUpdateRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }

  /**
   * 댓글 삭제
   */
  @DeleteMapping("/{commentId}")
  public ResponseEntity<BoardResponseDto<Object>> delete(@PathVariable Long commentId) {
    commentService.delete(commentId);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }
}
