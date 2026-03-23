package com.yeonseong.board.repository.fake;

import com.yeonseong.board.entity.NoticeSendHistoryEntity;
import com.yeonseong.board.repository.NoticeSendHistoryRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.test.util.ReflectionTestUtils;

public class NoticeSendHistoryRepositoryFake implements NoticeSendHistoryRepository {

  Long id = 0L;
  Map<Long, NoticeSendHistoryEntity> sendHistoryEntityMap = new HashMap<>();

  @Override
  public int save(NoticeSendHistoryEntity noticeSendHistoryEntity) {
    ReflectionTestUtils.setField(noticeSendHistoryEntity, "id", ++id);
    sendHistoryEntityMap.put(id, noticeSendHistoryEntity);
    return 1;
  }

  private void update(NoticeSendHistoryEntity noticeSendHistoryEntity) {
    sendHistoryEntityMap.remove(noticeSendHistoryEntity.getId());
    sendHistoryEntityMap.put(noticeSendHistoryEntity.getId(), noticeSendHistoryEntity);
  }

  @Override
  public int updateStatusById(NoticeSendHistoryEntity noticeSendHistoryEntity) {
    update(noticeSendHistoryEntity);
    return 1;
  }

  public NoticeSendHistoryEntity findByLast() {

    if (sendHistoryEntityMap.isEmpty()) {
      throw new RuntimeException("저장된 이력이 없습니다.");
    }
    return sendHistoryEntityMap.get(this.id);
  }
}
