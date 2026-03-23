package com.yeonseong.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardGetListRequestDto {

  private int page;

  private int size;

  private String keyword;

  private String memberName;

  public int getOffset() {
    return (this.page - 1) * this.size;
  }
}
