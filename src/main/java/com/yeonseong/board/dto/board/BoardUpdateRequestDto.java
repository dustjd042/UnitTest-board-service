package com.yeonseong.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardUpdateRequestDto {

  private String title;

  private String content;

}
