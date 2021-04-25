package com.spochi.repository;

import com.spochi.entity.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RewardRepository extends MongoRepository<Reward, String> {
    Optional<Reward> findByUserTypeIdAndInitiativeTypeId(int userTypeId, int initiativeTypeId);
}
