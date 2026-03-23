package com.yeonseong.board.repository;

import com.yeonseong.board.dto.board.BoardGetListRequestDto;
import com.yeonseong.board.dto.board.BoardGetListResponseDto;
import com.yeonseong.board.entity.BoardEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository {

  int save(BoardEntity boardEntity);

  int updateById(BoardEntity boardEntity);

  int updateStatusById(BoardEntity boardEntity);

  Optional<BoardEntity> findById(Long id);

  int selectBoardListCount(BoardGetListRequestDto boardGetListRequestDto);

  List<BoardGetListResponseDto.BoardGetList> selectBoardList(BoardGetListRequestDto boardGetListRequestDto);
}
