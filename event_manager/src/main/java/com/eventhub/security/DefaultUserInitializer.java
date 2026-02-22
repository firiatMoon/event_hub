package com.eventhub.security;

import com.eventhub.entities.UserEntity;
import com.eventhub.enums.Role;
import com.eventhub.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        createUserIfNotExists("user", "user", Role.USER);
        createUserIfNotExists("admin", "admin", Role.ADMIN);
    }

    private void createUserIfNotExists(String login, String password, Role role) {
        if(userService.isUserExistsByLogin(login)){
            return;
        }
        UserEntity createdUser = new UserEntity();
        createdUser.setLogin(login);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setAge(21);
        createdUser.setRole(role);
        userService.saveUser(createdUser);
    }
}
