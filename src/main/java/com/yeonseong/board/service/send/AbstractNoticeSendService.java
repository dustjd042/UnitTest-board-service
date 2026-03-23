package com.yeonseong.board.service.send;

import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.entity.NoticeSendHistoryEntity;
import com.yeonseong.board.repository.NoticeSendHistoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractNoticeSendService implements NoticeSendService {

  protected final NoticeSendTypeCode noticeSendTypeCode;

  protected final NoticeSendHistoryRepository noticeSendHistoryRepository;

  @Override
  public final void send(String to, String title, String content) {

    NoticeSendHistoryEntity noticeSendHistoryEntity = new NoticeSendHistoryEntity(noticeSendTypeCode, to, title, content);

    noticeSendHistoryRepository.save(noticeSendHistoryEntity);

    processSend(noticeSendHistoryEntity);

    noticeSendHistoryRepository.updateStatusById(noticeSendHistoryEntity);
  }

  protected abstract NoticeSendHistoryEntity processSend(NoticeSendHistoryEntity noticeSendHistoryEntity);
}