package com.eventhub.event_management.services.converter;

import com.eventhub.event_management.dto.UserDTO;
import com.eventhub.event_management.vo.User;
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
