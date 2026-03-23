package com.yeonseong.board.exception;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.construct.SlackChannel;
import com.yeonseong.board.dto.BoardResponseDto;
import com.yeonseong.board.service.send.NoticeSendService;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class BoardExceptionAdvisor {

  private final Map<String, NoticeSendService> noticesendServiceMap;

  @ExceptionHandler(BoardException.class)
  public ResponseEntity<BoardResponseDto<Object>> BoardException (BoardException boardException) {

    log.warn("[BoardException] BoardResponseResultType: {} | BoardResponseResultMessage: {}"
        , boardException.getBoardResponseResultType()
        , boardException.getBoardResponsePopupMessage());

    return ResponseEntity.status(boardException.getBoardResponseResultType().getHttpStatus())
        .body(new BoardResponseDto<>(boardException.getBoardResponseResultType(), boardException.getBoardResponsePopupMessage()));
  }

  @ExceptionHandler({
      MethodArgumentNotValidException.class
      , ConstraintViolationException.class
      , MissingPathVariableException.class
      , MissingServletRequestParameterException.class
      , NoHandlerFoundException.class
  })
  public ResponseEntity<BoardResponseDto<Object>> handleBadRequest(Exception exception) {

    log.warn("[BadRequestException] Message: {}", exception.getMessage());

    return ResponseEntity.status(BoardResponseResultType.BAD_REQUEST.getHttpStatus())
        .body(new BoardResponseDto<>(BoardResponseResultType.BAD_REQUEST, BoardResponsePopupMessage.BAD_REQUEST));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BoardResponseDto<Object>> handleException (Exception exception) {

    log.error("[Exception] ", exception);

    noticesendServiceMap.get(NoticeSendTypeCode.SLACK.getBeanName())
        .send(SlackChannel.SYSTEM_ALERT.getChannel(), "시스템 오류 발생", exception.getMessage());

    return ResponseEntity.status(BoardResponseResultType.ERROR.getHttpStatus())
        .body(new BoardResponseDto<>(BoardResponseResultType.ERROR, BoardResponsePopupMessage.ERROR));
  }
}
