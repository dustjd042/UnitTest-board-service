package com.yeonseong.board.service;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.dto.board.BoardCreateRequestDto;
import com.yeonseong.board.dto.board.BoardCreateResponseDto;
import com.yeonseong.board.dto.board.BoardGetListRequestDto;
import com.yeonseong.board.dto.board.BoardGetListResponseDto;
import com.yeonseong.board.dto.board.BoardGetListResponseDto.BoardGetList;
import com.yeonseong.board.dto.board.BoardGetResponseDto;
import com.yeonseong.board.dto.board.BoardUpdateRequestDto;
import com.yeonseong.board.entity.BoardEntity;
import com.yeonseong.board.entity.MemberEntity;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.repository.BoardRepository;
import com.yeonseong.board.repository.MemberRepository;
import com.yeonseong.board.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;

  public BoardGetListResponseDto getList(BoardGetListRequestDto boardGetListRequestDto) {

    int totalSize = boardRepository.selectBoardListCount(boardGetListRequestDto);
    List<BoardGetList> boardGetList = boardRepository.selectBoardList(boardGetListRequestDto);

    return new BoardGetListResponseDto(totalSize, boardGetList);
  }

  public BoardGetResponseDto get(Long boardId) {

    BoardEntity boardEntity = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_BOARD));

    return new BoardGetResponseDto(boardEntity);
  }

  public BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto) {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(()-> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    BoardEntity boardEntity = new BoardEntity(boardCreateRequestDto, memberEntity);
    boardRepository.save(boardEntity);

    return new BoardCreateResponseDto(boardEntity.getId());
  }

  public void update(Long boardId, BoardUpdateRequestDto boardUpdateRequestDto) {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(()-> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    BoardEntity boardEntity = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_BOARD));

    boardEntity.authorizeCheck(memberEntity);

    boardEntity.update(boardUpdateRequestDto);
    boardRepository.updateById(boardEntity);
  }

  public void delete(Long boardId) {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(()-> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    BoardEntity boardEntity = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_BOARD));

    boardEntity.authorizeCheck(memberEntity);

    boardEntity.delete();
    boardRepository.updateStatusById(boardEntity);
  }
}
