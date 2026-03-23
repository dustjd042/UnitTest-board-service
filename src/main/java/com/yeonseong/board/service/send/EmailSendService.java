package com.yeonseong.board.service.send;

import com.yeonseong.board.construct.NoticeSendHistoryStatus;
import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.entity.NoticeSendHistoryEntity;
import com.yeonseong.board.repository.NoticeSendHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSendService extends AbstractNoticeSendService {

  private final JavaMailSender javaMailSender;

  public EmailSendService(NoticeSendHistoryRepository noticeSendHistoryRepository, JavaMailSender javaMailSender) {
    super(NoticeSendTypeCode.EMAIL, noticeSendHistoryRepository);
    this.javaMailSender = javaMailSender;
  }

  @Async
  public NoticeSendHistoryEntity processSend(NoticeSendHistoryEntity noticeSendHistoryEntity) {

     NoticeSendHistoryStatus noticeSendHistoryStatus = NoticeSendHistoryStatus.SUCCESS;

    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setTo(noticeSendHistoryEntity.getTo());
      simpleMailMessage.setSubject(noticeSendHistoryEntity.getTitle());
      simpleMailMessage.setText(noticeSendHistoryEntity.getContent());
      javaMailSender.send(simpleMailMessage);
    } catch (Exception exception){
      log.error("[EmailSendService Exception] ", exception);
      noticeSendHistoryStatus = NoticeSendHistoryStatus.FAIL;
    }

    noticeSendHistoryEntity.setResult(noticeSendHistoryStatus, "");
    return noticeSendHistoryEntity;
  }

}
