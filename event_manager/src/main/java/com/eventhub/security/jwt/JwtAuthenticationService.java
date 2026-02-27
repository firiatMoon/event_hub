package com.eventhub.security.jwt;

import com.eventhub.dto.JwtTokenResponse;
import com.eventhub.dto.RefreshTokenDTO;
import com.eventhub.dto.SingInRequest;
import com.eventhub.services.UserService;
import com.eventhub.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class JwtAuthenticationService {

    private final JwtTokenManager jwtTokenManager;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationService(JwtTokenManager jwtTokenManager,
                                    UserService userService,
                                    AuthenticationManager authenticationManager) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public JwtTokenResponse authenticate(SingInRequest singInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                singInRequest.login(),
                singInRequest.password()
        ));

        User user = userService.findByLogin(singInRequest.login());

        return jwtTokenManager.generateAuthToken(singInRequest.login(), user.id(), user.role());
    }

    public JwtTokenResponse refreshToken(RefreshTokenDTO refreshTokenDto){
        String refreshToken = refreshTokenDto.refreshToken();
        if (!Objects.isNull(refreshToken) && jwtTokenManager.validateToken(refreshToken)) {
            User user = userService.findByLogin(jwtTokenManager.getLoginFromToken(refreshToken));

            return jwtTokenManager.refreshBaseToken(user.login(), refreshToken, user.id(), user.role());
        }
        throw new BadCredentialsException("Invalid or expired token");
    }
}
