package com.yeonseong.board.repository;

import com.yeonseong.board.entity.NoticeSendHistoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeSendHistoryRepository {

  int save(NoticeSendHistoryEntity noticeSendHistoryEntity);

  int updateStatusById(NoticeSendHistoryEntity noticeSendHistoryEntity);

}
