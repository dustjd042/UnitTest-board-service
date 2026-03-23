package com.yeonseong.board.repository.fake;

import com.yeonseong.board.dto.board.BoardGetListRequestDto;
import com.yeonseong.board.dto.board.BoardGetListResponseDto;
import com.yeonseong.board.dto.board.BoardGetListResponseDto.BoardGetList;
import com.yeonseong.board.entity.BoardEntity;
import com.yeonseong.board.repository.BoardRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.test.util.ReflectionTestUtils;

public class BoardRepositoryFake implements BoardRepository {

  Long id = 0L;
  Map<Long, BoardEntity> boardEntityMap = new HashMap<>();

  @Override
  public int save(BoardEntity boardEntity) {
    ReflectionTestUtils.setField(boardEntity, "id", ++id);
    boardEntityMap.put(id, boardEntity);
    return 1;
  }

  public void update(BoardEntity boardEntity) {
    boardEntityMap.remove(boardEntity.getId());
    boardEntityMap.put(boardEntity.getId(), boardEntity);
  }

  @Override
  public int updateById(BoardEntity boardEntity) {
    update(boardEntity);
    return 1;
  }

  @Override
  public int updateStatusById(BoardEntity boardEntity) {
    update(boardEntity);
    return 1;
  }

  @Override
  public Optional<BoardEntity> findById(Long id) {
    return Optional.of(boardEntityMap.get(id));
  }

  @Override
  public int selectBoardListCount(BoardGetListRequestDto boardGetListRequestDto) {
    return (int) boardEntityMap.values().stream()
        .filter(boardEntity -> {
          String keyword = boardGetListRequestDto.getKeyword();
          return keyword == null || boardEntity.getTitle().contains(keyword);
        })
        .filter(boardEntity -> {
          String memberName = boardGetListRequestDto.getMemberName();
          return memberName == null || boardEntity.getMemberEntity().getName().contains(memberName);
        })
        .count();
  }

  @Override
  public List<BoardGetListResponseDto.BoardGetList> selectBoardList(BoardGetListRequestDto boardGetListRequestDto) {
    return boardEntityMap.values().stream()
        .filter(boardEntity -> {
          String keyword = boardGetListRequestDto.getKeyword();
          return keyword == null || boardEntity.getTitle().contains(keyword);
        })
        .filter(boardEntity -> {
          String memberName = boardGetListRequestDto.getMemberName();
          return memberName == null || boardEntity.getMemberEntity().getName().contains(memberName);
        })
        .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
        .skip(boardGetListRequestDto.getOffset())
        .limit(boardGetListRequestDto.getSize())
        .map(
            boardEntity
                -> new BoardGetList(
                    boardEntity.getId()
                , boardEntity.getTitle()
                , boardEntity.getMemberEntity().getName()
                , boardEntity.getCreateDt()
            )
        )
        .collect(Collectors.toList());
  }

  public BoardEntity findByLast() {

    if (boardEntityMap.isEmpty()) {
      throw new RuntimeException("저장된 이력이 없습니다.");
    }
    return boardEntityMap.get(this.id);
  }
}
