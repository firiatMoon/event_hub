package com.eventhub.controllers;

import com.eventhub.dto.*;
import com.eventhub.security.jwt.JwtAuthenticationService;
import com.eventhub.services.UserService;
import com.eventhub.services.converter.UserDTOMapper;
import com.eventhub.model.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final UserDTOMapper userDTOMapper;

    private final JwtAuthenticationService jwtAuthenticationService;

    public UserController(UserService userService,
                          UserDTOMapper userDTOMapper, JwtAuthenticationService jwtAuthenticationService) {
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @Operation(summary = "Получение пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.getByUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTOMapper.toUserDTO(user));
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping
    public ResponseEntity<UserDTO> registration( @RequestBody @Valid final SingUpRequest singUpRequest) {
        log.info("Get request for sing-up user: {}", singUpRequest.login());
        User user = userService.registrationUser(singUpRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDTOMapper.toUserDTO(user));
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody @Valid SingInRequest signInRequest) {
        log.info("Get request for sing-in user: {}", signInRequest.login());
        JwtTokenResponse token = jwtAuthenticationService.authenticate(signInRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }

    @Operation(summary = "Refresh-токен")
    @PostMapping("/refresh")
    public JwtTokenResponse refresh(@RequestBody RefreshTokenDTO refreshTokenDto) {
        return jwtAuthenticationService.refreshToken(refreshTokenDto);
    }
}
