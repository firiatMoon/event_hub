package com.eventhub.event_management.services.converter;

import com.eventhub.event_management.entities.UserEntity;
import com.eventhub.event_management.vo.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter {

    public User toUser(UserEntity userEntity) {
        return new User (
               userEntity.getId(),
               userEntity.getLogin(),
               userEntity.getPassword(),
               userEntity.getAge(),
               userEntity.getRole()
        );
    }
}
