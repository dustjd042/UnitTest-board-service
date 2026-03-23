package com.yeonseong.board.repository.fake;

import com.yeonseong.board.entity.MemberEntity;
import com.yeonseong.board.repository.MemberRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberRepositoryFake implements MemberRepository {

  Long id = 0L;
  Map<Long, MemberEntity> memberEntityMap = new HashMap<>();

  @Override
  public int save(MemberEntity memberEntity) {
    ReflectionTestUtils.setField(memberEntity, "id", ++id);
    memberEntityMap.put(id, memberEntity);
    return 1;
  }

  public void update(MemberEntity memberEntity) {
    memberEntityMap.remove(memberEntity.getId());
    memberEntityMap.put(memberEntity.getId(), memberEntity);
  }

  @Override
  public int updateRefreshTokenAndLoginDtById(MemberEntity memberEntity) {
    update(memberEntity);
    return 1;
  }

  @Override
  public int updatePasswordById(MemberEntity memberEntity) {
    update(memberEntity);
    return 1;
  }

  @Override
  public int updateStatusById(MemberEntity memberEntity) {
    update(memberEntity);
    return 1;
  }

  @Override
  public boolean existsByName(String name) {
    for (MemberEntity memberEntity : memberEntityMap.values()) {
      if (memberEntity.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean existsByEmail(String email) {
    for (MemberEntity memberEntity : memberEntityMap.values()) {
      if (memberEntity.getEmail().equals(email)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Optional<MemberEntity> findById(Long id) {
    return Optional.of(memberEntityMap.get(id));
  }

  @Override
  public Optional<MemberEntity> findByName(String name) {
    for (MemberEntity memberEntity :memberEntityMap.values()) {
      if (memberEntity.getName().equals(name)) {
        return Optional.of(memberEntity);
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<MemberEntity> findByEmail(String email) {
    for (MemberEntity memberEntity :memberEntityMap.values()) {
      if (memberEntity.getEmail().equals(email)) {
        return Optional.of(memberEntity);
      }
    }
    return Optional.empty();
  }

  public MemberEntity findByLast() {

    if (memberEntityMap.isEmpty()) {
      throw new RuntimeException("저장된 이력이 없습니다.");
    }
    return memberEntityMap.get(this.id);
  }
}
