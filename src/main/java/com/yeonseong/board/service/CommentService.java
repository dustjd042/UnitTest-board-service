package com.yeonseong.board.service;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.dto.comment.CommentCreateRequestDto;
import com.yeonseong.board.dto.comment.CommentCreateResponseDto;
import com.yeonseong.board.dto.comment.CommentGetListRequestDto;
import com.yeonseong.board.dto.comment.CommentGetListResponseDto;
import com.yeonseong.board.dto.comment.CommentGetListResponseDto.CommentGetList;
import com.yeonseong.board.dto.comment.CommentUpdateRequestDto;
import com.yeonseong.board.entity.BoardEntity;
import com.yeonseong.board.entity.CommentEntity;
import com.yeonseong.board.entity.MemberEntity;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.repository.BoardRepository;
import com.yeonseong.board.repository.CommentRepository;
import com.yeonseong.board.repository.MemberRepository;
import com.yeonseong.board.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;

  public CommentGetListResponseDto getList(CommentGetListRequestDto commentGetListRequestDto) {
    int totalSize = commentRepository.selectCommentListCount(commentGetListRequestDto);
    List<CommentGetList> commentGetList = commentRepository.selectCommentList(commentGetListRequestDto);
    return new CommentGetListResponseDto(totalSize, commentGetList);
  }

  public CommentCreateResponseDto create(CommentCreateRequestDto commentCreateRequestDto) {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(()-> new BoardException(
            BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    BoardEntity boardEntity = boardRepository.findById(commentCreateRequestDto.getBoardId())
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_BOARD));

    CommentEntity commentEntity = new CommentEntity(commentCreateRequestDto, memberEntity, boardEntity);
    commentRepository.save(commentEntity);
    return new CommentCreateResponseDto(commentEntity.getId());
  }

  public void update(Long commentId, CommentUpdateRequestDto commentUpdateRequestDto) {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(()-> new BoardException(
            BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    CommentEntity commentEntity = commentRepository.findById(commentId)
        .orElseThrow(()-> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_COMMENT));

    commentEntity.authorizeCheck(memberEntity);

    commentEntity.update(commentUpdateRequestDto);
    commentRepository.updateById(commentEntity);
  }

  public void delete(Long commentId) {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(()-> new BoardException(
            BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    CommentEntity commentEntity = commentRepository.findById(commentId)
        .orElseThrow(()-> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_COMMENT));

    commentEntity.authorizeCheck(memberEntity);

    commentEntity.delete();
    commentRepository.updateStatusById(commentEntity);
  }
}
