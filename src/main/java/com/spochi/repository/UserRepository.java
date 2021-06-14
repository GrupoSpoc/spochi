package com.spochi.repository;

import com.spochi.entity.Initiative;
import com.spochi.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends EntityRepository<User> {
    Optional<User> findByUid(String uid);
    Optional<User> findByNickname(String nickname);
    Map<Integer,Integer> getUserInitiativesByStatus(String id);
}
