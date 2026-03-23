package com.yeonseong.board.controller;

import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.dto.BoardResponseDto;
import com.yeonseong.board.dto.board.BoardCreateRequestDto;
import com.yeonseong.board.dto.board.BoardCreateResponseDto;
import com.yeonseong.board.dto.board.BoardGetListRequestDto;
import com.yeonseong.board.dto.board.BoardGetListResponseDto;
import com.yeonseong.board.dto.board.BoardGetResponseDto;
import com.yeonseong.board.dto.board.BoardUpdateRequestDto;
import com.yeonseong.board.service.BoardService;
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
@RequestMapping("/api/board")
public class BoardController {

  private final BoardService boardService;

  /**
   * 게시판 리스트 조회 (페이징 및 검색)
   */
  @GetMapping("/list")
  public ResponseEntity<BoardResponseDto<BoardGetListResponseDto>> getList(
      @ModelAttribute BoardGetListRequestDto boardGetListRequestDto) {

    BoardGetListResponseDto boardGetListResponseDto = boardService.getList(boardGetListRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, boardGetListResponseDto));
  }

  /**
   * 게시글 상세 조회
   */
  @GetMapping("/{boardId}")
  public ResponseEntity<BoardResponseDto<BoardGetResponseDto>> get(@PathVariable Long boardId) {

    BoardGetResponseDto boardGetResponseDto = boardService.get(boardId);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, boardGetResponseDto));
  }

  /**
   * 게시글 등록
   */
  @PostMapping("")
  public ResponseEntity<BoardResponseDto<BoardCreateResponseDto>> create(
      @Valid @RequestBody BoardCreateRequestDto boardCreateRequestDto) {

    BoardCreateResponseDto boardGetResponseDto = boardService.create(boardCreateRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS, boardGetResponseDto));
  }

  /**
   * 게시글 수정 (전체 수정)
   */
  @PutMapping("/{boardId}")
  public ResponseEntity<BoardResponseDto<Object>> update(
      @PathVariable Long boardId
      , @Valid @RequestBody BoardUpdateRequestDto boardUpdateRequestDto) {

    boardService.update(boardId, boardUpdateRequestDto);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }

  /**
   * 게시글 삭제
   */
  @DeleteMapping("/{boardId}")
  public ResponseEntity<BoardResponseDto<Object>> delete(@PathVariable Long boardId) {
    boardService.delete(boardId);
    return ResponseEntity.ok(new BoardResponseDto<>(BoardResponseResultType.SUCCESS));
  }
}
