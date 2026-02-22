package com.eventhub.services.converter;

import com.eventhub.entities.UserEntity;
import com.eventhub.model.User;
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
