package com.eventhub.event_management.security.jwt;

import com.eventhub.event_management.dto.JwtTokenResponse;
import com.eventhub.event_management.dto.RefreshTokenDTO;
import com.eventhub.event_management.dto.SingInRequest;
import com.eventhub.event_management.services.UserService;
import com.eventhub.event_management.vo.User;
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
        return jwtTokenManager.generateAuthToken(singInRequest.login());
    }

    public JwtTokenResponse refreshToken(RefreshTokenDTO refreshTokenDto){
        String refreshToken = refreshTokenDto.refreshToken();
        if (!Objects.isNull(refreshToken) && jwtTokenManager.validateToken(refreshToken)) {
            User user = userService.findByLogin(jwtTokenManager.getLoginFromToken(refreshToken));

            return jwtTokenManager.refreshBaseToken(user.login(), refreshToken);
        }
        throw new BadCredentialsException("Invalid or expired token");
    }
}
