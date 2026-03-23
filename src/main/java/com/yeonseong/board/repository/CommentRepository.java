package com.yeonseong.board.repository;

import com.yeonseong.board.dto.comment.CommentGetListRequestDto;
import com.yeonseong.board.dto.comment.CommentGetListResponseDto;
import com.yeonseong.board.entity.CommentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository {

  int save(CommentEntity commentEntity);

  int updateById(CommentEntity commentEntity);

  int updateStatusById(CommentEntity commentEntity);

  Optional<CommentEntity> findById(Long id);

  int selectCommentListCount(CommentGetListRequestDto commentGetListRequestDto);

  List<CommentGetListResponseDto.CommentGetList> selectCommentList(CommentGetListRequestDto commentGetListRequestDto);
}
