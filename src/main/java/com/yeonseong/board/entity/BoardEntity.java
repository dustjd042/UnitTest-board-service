package com.yeonseong.board.entity;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.construct.BoardStatusCode;
import com.yeonseong.board.dto.board.BoardCreateRequestDto;
import com.yeonseong.board.dto.board.BoardUpdateRequestDto;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity extends CommonEntity {

  private String title;

  private String content;

  private BoardStatusCode status;

  private MemberEntity memberEntity;

  public BoardEntity(BoardCreateRequestDto boardCreateRequestDto, MemberEntity memberEntity) {
    setCreate(memberEntity.getId());
    this.title = boardCreateRequestDto.getTitle();
    this.content = boardCreateRequestDto.getContent();
    this.status = BoardStatusCode.CREATE;
    this.memberEntity = memberEntity;
  }

  public void update(BoardUpdateRequestDto boardUpdateRequestDto) {
    setModify(SecurityUtils.getMemberId());
    this.title = boardUpdateRequestDto.getTitle();
    this.content = boardUpdateRequestDto.getContent();
  }

  public void delete() {
    setModify(SecurityUtils.getMemberId());
    this.status = BoardStatusCode.DELETE;
  }

  public void authorizeCheck(MemberEntity memberEntity) {

    if (this.status == BoardStatusCode.DELETE) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_BOARD);
    }

    if (!memberEntity.getId().equals(this.memberEntity.getId())) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.UNAUTHORIZED_BOARD);
    }
  }
}
