package com.yeonseong.board.repository.fake;

import com.yeonseong.board.dto.comment.CommentGetListRequestDto;
import com.yeonseong.board.dto.comment.CommentGetListResponseDto.CommentGetList;
import com.yeonseong.board.entity.CommentEntity;
import com.yeonseong.board.repository.CommentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.test.util.ReflectionTestUtils;

public class CommentRepositoryFake implements CommentRepository {

  Long id = 0L;
  Map<Long, CommentEntity> commentEntityMap = new HashMap<>();

  @Override
  public int save(CommentEntity commentEntity) {
    ReflectionTestUtils.setField(commentEntity, "id", ++id);
    commentEntityMap.put(id, commentEntity);
    return 1;
  }

  public void update(CommentEntity commentEntity) {
    commentEntityMap.remove(commentEntity.getId());
    commentEntityMap.put(commentEntity.getId(), commentEntity);
  }

  @Override
  public int updateById(CommentEntity commentEntity) {
    update(commentEntity);
    return 1;
  }

  @Override
  public int updateStatusById(CommentEntity commentEntity) {
    update(commentEntity);
    return 1;
  }

  @Override
  public Optional<CommentEntity> findById(Long id) {
    return Optional.of(commentEntityMap.get(id));
  }

  @Override
  public int selectCommentListCount(CommentGetListRequestDto commentGetListRequestDto) {
    return (int) commentEntityMap.values().stream()
        .filter(commentEntity -> (commentEntity.getBoardEntity().getId().equals(commentGetListRequestDto.getBoardId())))
        .filter(commentEntity -> {
          Long memberId = commentGetListRequestDto.getMemberId();
          return memberId == null || commentEntity.getMemberEntity().getId().equals(memberId);
        })
        .count();
  }

  @Override
  public List<CommentGetList> selectCommentList(CommentGetListRequestDto commentGetListRequestDto) {
    return commentEntityMap.values().stream()
        .filter(commentEntity -> (commentEntity.getBoardEntity().getId().equals(commentGetListRequestDto.getBoardId())))
        .filter(commentEntity -> {
          Long memberId = commentGetListRequestDto.getMemberId();
          return memberId == null || commentEntity.getMemberEntity().getId().equals(memberId);
        })
        .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
        .skip(commentGetListRequestDto.getOffset())
        .limit(commentGetListRequestDto.getSize())
        .map(
            commentEntity
                -> new CommentGetList(
                commentEntity.getId()
                , commentEntity.getContent()
                , commentEntity.getMemberEntity().getName()
                , commentEntity.getCreateDt()
            )
        )
        .collect(Collectors.toList());
  }

  public CommentEntity findByLast() {

    if (commentEntityMap.isEmpty()) {
      throw new RuntimeException("저장된 이력이 없습니다.");
    }
    return commentEntityMap.get(this.id);
  }
}