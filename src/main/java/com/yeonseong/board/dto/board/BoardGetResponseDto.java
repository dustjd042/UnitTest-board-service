package com.yeonseong.board.dto.board;

import com.yeonseong.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardGetResponseDto {

  private String boardTitle;

  private String boardContent;

  private Long memberId;

  private String memberName;

  public BoardGetResponseDto(BoardEntity boardEntity) {
    this.boardTitle = boardEntity.getTitle();
    this.boardContent = boardEntity.getContent();
    this.memberId = boardEntity.getMemberEntity().getId();
    this.memberName = boardEntity.getMemberEntity().getName();
  }
}
