package com.eventhub.services;

import com.eventhub.dto.SingUpRequest;
import com.eventhub.entities.UserEntity;
import com.eventhub.enums.Role;
import com.eventhub.repositories.UserRepository;
import com.eventhub.services.converter.UserEntityMapper;
import com.eventhub.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final LocaleMessageService messageService;

    public UserService(UserRepository userRepository,
                       UserEntityMapper userEntityMapper, PasswordEncoder passwordEncoder, LocaleMessageService messageService) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    public User registrationUser(SingUpRequest singUpRequest){
        if(userRepository.existsByLogin(singUpRequest.login())){
            throw new IllegalArgumentException(messageService.getMessage("err.user.already-taken"));
        }

        UserEntity user = new UserEntity();
        user.setLogin(singUpRequest.login());
        user.setPassword(passwordEncoder.encode(singUpRequest.password()));
        user.setAge(singUpRequest.age());
        user.setRole(Role.USER);
        UserEntity savedUser = userRepository.save(user);

        return userEntityMapper.toUser(savedUser);
    }

    public User getByUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage("err.user.not-found")));
        return userEntityMapper.toUser(userEntity);
    }

    public User findByLogin(String login) {
        UserEntity user =  userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(messageService.getMessage("err.user.not-found")));
        return userEntityMapper.toUser(user);
    }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
