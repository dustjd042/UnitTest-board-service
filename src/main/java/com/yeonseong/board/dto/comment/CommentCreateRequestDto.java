package com.yeonseong.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequestDto {

  private String content;

  private Long boardId;

}
