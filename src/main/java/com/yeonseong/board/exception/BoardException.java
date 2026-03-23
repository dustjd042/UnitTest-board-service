package com.yeonseong.board.exception;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {

  private final BoardResponseResultType boardResponseResultType;
  private final BoardResponsePopupMessage boardResponsePopupMessage;

  public BoardException(BoardResponseResultType boardResponseResultType, BoardResponsePopupMessage boardResponsePopupMessage) {
    this.boardResponseResultType = boardResponseResultType;
    this.boardResponsePopupMessage = boardResponsePopupMessage;
  }
}
