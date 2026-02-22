package com.eventhub.repositories;

import com.eventhub.model.TelegramCodeSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<TelegramCodeSession, String> {
}
