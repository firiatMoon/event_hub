package com.eventhub.event_management.services.converter;

import com.eventhub.event_management.entities.UserEntity;
import com.eventhub.event_management.vo.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public User toUser(UserEntity userEntity) {
        return new User (
               userEntity.getId(),
               userEntity.getLogin(),
               userEntity.getAge(),
               userEntity.getRole()
        );
    }
}
