package com.yeonseong.board.service.send;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import com.yeonseong.board.construct.NoticeSendHistoryStatus;
import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.entity.NoticeSendHistoryEntity;
import com.yeonseong.board.repository.NoticeSendHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackSendService extends AbstractNoticeSendService {

  private final Slack slack;
  private final String webhookUrl;

  public SlackSendService(NoticeSendHistoryRepository noticeSendHistoryRepository, Slack slack, @Value("${slack.webhook-url}") String webhookUrl) {
    super(NoticeSendTypeCode.SLACK, noticeSendHistoryRepository);
    this.slack = slack;
    this.webhookUrl = webhookUrl;
  }

  @Async
  public NoticeSendHistoryEntity processSend(NoticeSendHistoryEntity noticeSendHistoryEntity) {

    NoticeSendHistoryStatus noticeSendHistoryStatus = NoticeSendHistoryStatus.SUCCESS;
    String sendResultDetail = "";

    try {
      Payload payload = Payload.builder()
          .channel(noticeSendHistoryEntity.getTo())
          .text(noticeSendHistoryEntity.getSlacMessage())
          .build();
      WebhookResponse webhookResponse = slack.send(webhookUrl, payload);
      if (webhookResponse.getCode() != 200) {
        noticeSendHistoryStatus = NoticeSendHistoryStatus.FAIL;
        sendResultDetail = webhookResponse.getCode().toString();
      }
    } catch (Exception exception) {
      log.error("[SlackSendService Exception] ", exception);
      noticeSendHistoryStatus = NoticeSendHistoryStatus.FAIL;
    }

    noticeSendHistoryEntity.setResult(noticeSendHistoryStatus, sendResultDetail);
    return noticeSendHistoryEntity;
  }
}
