package com.eventhub.event_management;

import com.eventhub.event_management.entities.UserEntity;
import com.eventhub.event_management.enums.Role;
import com.eventhub.event_management.repositories.UserRepository;
import com.eventhub.event_management.security.jwt.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserTestUtils {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    private static final String DEFAULT_ADMIN_LOGIN = "admin";
    private static final String DEFAULT_USER_LOGIN = "user";
    private static volatile boolean isUserInitialized = false;

    @Autowired
    public UserTestUtils(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String getJwtTokenWithRole(Role userRole) {
        if (!isUserInitialized) {
            initializeTestUsers();
            isUserInitialized = true;
        }

        return switch (userRole) {
            case ADMIN -> jwtTokenManager.generateAuthToken(DEFAULT_ADMIN_LOGIN).getJwtToken();
            case USER -> jwtTokenManager.generateAuthToken(DEFAULT_USER_LOGIN).getJwtToken();
        };
    }

    private void initializeTestUsers() {
        createUser(DEFAULT_ADMIN_LOGIN, "admin", 23, Role.ADMIN);
        createUser(DEFAULT_USER_LOGIN, "user", 21, Role.USER);
    }

    private void createUser(String login, String password, Integer age, Role role) {
        if(userRepository.existsByLogin(login)){
            return;
        }

        UserEntity user = new UserEntity();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setAge(age);
        user.setRole(role);
        userRepository.save(user);
    }
}
