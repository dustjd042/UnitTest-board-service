package com.yeonseong.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentGetListRequestDto {

  private int page;

  private int size;

  private Long boardId;

  private Long memberId;

  public int getOffset() {
    return (this.page - 1) * this.size;
  }
}
