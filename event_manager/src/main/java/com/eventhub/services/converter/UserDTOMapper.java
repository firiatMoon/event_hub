package com.eventhub.services.converter;

import com.eventhub.dto.UserDTO;
import com.eventhub.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.id(),
                user.login(),
                user.age()
        );
    }
}
