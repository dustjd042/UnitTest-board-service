package com.yeonseong.board.repository.fake;

import com.yeonseong.board.entity.VerifyEmailEntity;
import com.yeonseong.board.repository.VerifyEmailRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

public class VerifyEmailRepositoryFake implements VerifyEmailRepository {

  Long id = 0L;
  Map<Long, VerifyEmailEntity> verifyEmailEntityMap = new HashMap<>();

  @Override
  public int save(VerifyEmailEntity verifyEmailEntity) {
    ReflectionTestUtils.setField(verifyEmailEntity, "id", ++id);
    verifyEmailEntityMap.put(id, verifyEmailEntity);
    return 1;
  }

  private void update(VerifyEmailEntity verifyEmailEntity) {
    verifyEmailEntityMap.remove(verifyEmailEntity.getId());
    verifyEmailEntityMap.put(verifyEmailEntity.getId(), verifyEmailEntity);
  }

  @Override
  public int updateStatusByEmailAndVerifyCode(VerifyEmailEntity verifyEmailEntity) {
    this.update(verifyEmailEntity);
    return 1;
  }

  @Override
  public Optional<VerifyEmailEntity> findByEmailAndVerifyCode(String email, String verifyCode) {
    for (var verifyEmailEntity : verifyEmailEntityMap.values()) {
      if (verifyEmailEntity.getEmail().equals(email)
          && verifyEmailEntity.getVerifyCode().equals(verifyCode)) {
        return Optional.of(verifyEmailEntity);
      }
    }
    return Optional.empty();
  }

  public VerifyEmailEntity findByLast() {

    if (verifyEmailEntityMap.isEmpty()) {
      throw new RuntimeException("저장된 이력이 없습니다.");
    }
    return verifyEmailEntityMap.get(this.id);
  }

  public void updateExpiredPast(VerifyEmailEntity verifyEmailEntity) {
    LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
    ReflectionTestUtils.setField(verifyEmailEntity, "verifyRequestDt", fiveMinutesAgo);
    this.update(verifyEmailEntity);
  }
}
