package com.yeonseong.board.entity;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.construct.CommentStatusCode;
import com.yeonseong.board.dto.comment.CommentCreateRequestDto;
import com.yeonseong.board.dto.comment.CommentUpdateRequestDto;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.util.SecurityUtils;
import lombok.Getter;

@Getter
public class CommentEntity extends CommonEntity {

  private String content;

  private CommentStatusCode status;

  private MemberEntity memberEntity;

  private BoardEntity boardEntity;

  public CommentEntity(CommentCreateRequestDto commentCreateRequestDto, MemberEntity memberEntity, BoardEntity boardEntity) {
    setCreate(memberEntity.getId());
    this.content = commentCreateRequestDto.getContent();
    this.status = CommentStatusCode.CREATE;
    this.memberEntity = memberEntity;
    this.boardEntity = boardEntity;
  }

  public void update(CommentUpdateRequestDto commentUpdateRequestDto) {
    setModify(SecurityUtils.getMemberId());
    this.content = commentUpdateRequestDto.getContent();
  }

  public void delete() {
    setModify(SecurityUtils.getMemberId());
    this.status = CommentStatusCode.DELETE;
  }

  public void authorizeCheck(MemberEntity memberEntity) {

    if (this.status == CommentStatusCode.DELETE) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_COMMENT);
    }

    if (!memberEntity.getId().equals(this.memberEntity.getId())) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.UNAUTHORIZED_COMMENT);
    }
  }
}
