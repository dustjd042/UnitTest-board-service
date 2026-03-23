package com.yeonseong.board.dto;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class BoardResponseDto<T> {

  private final String result;
  private final String resultMessage;
  private final String popupMessage;
  private final T data;

  public BoardResponseDto (BoardResponseResultType boardResponseResultType) {
    this.result = boardResponseResultType.getResult();
    this.resultMessage = boardResponseResultType.getResultMessage();
    this.popupMessage = BoardResponsePopupMessage.EMPTY.getPopupMessage();
    this.data = (T) new HashMap<String, Object>();
  }

  public BoardResponseDto (
      BoardResponseResultType boardResponseResultType
      , BoardResponsePopupMessage boardResponsePopupMessage
  ) {
    this.result = boardResponseResultType.getResult();
    this.resultMessage = boardResponseResultType.getResultMessage();
    this.popupMessage = boardResponsePopupMessage.getPopupMessage();
    this.data = (T) new HashMap<String, Object>();
  }

  public BoardResponseDto (
      BoardResponseResultType boardResponseResultType
      , T data
  ) {
    this.result = boardResponseResultType.getResult();
    this.resultMessage = boardResponseResultType.getResultMessage();
    this.popupMessage = "";
    this.data = data;
  }

  public BoardResponseDto (
      BoardResponseResultType boardResponseResultType
      , BoardResponsePopupMessage boardResponseResultMessage
      , T data
  ) {
    this.result = boardResponseResultType.getResult();
    this.resultMessage = boardResponseResultType.getResultMessage();
    this.popupMessage = boardResponseResultMessage.getPopupMessage();
    this.data = data;
  }
}
