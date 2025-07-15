package com.example.security.handlers;

import com.example.security.dtos.LoginResponseDTO;
import com.example.security.entities.User;
import com.example.security.services.JWTService;
import com.example.security.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JWTService jwtService;
    @Value("{deploy.env}")
    private String deploymentEnv;
    public OAuth2SuccessHandler(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,ServletException{
        OAuth2AuthenticationToken token=(OAuth2AuthenticationToken)  authentication;
        DefaultOAuth2User oAuth2User= (DefaultOAuth2User) token.getPrincipal();

        String email=oAuth2User.getAttribute("email");

        User user=userService.getUserByEmail(email);

        if(user==null){
              User newUser=User.builder()
                              .name(oAuth2User.getAttribute("name"))
                              .email(email)
                              .build();
              user=userService.save(newUser);
        }
        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);
        LoginResponseDTO loggedINUser=new LoginResponseDTO(user.getId(), accessToken,refreshToken);
        Cookie cookie=new Cookie("refreshToken", loggedINUser.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deploymentEnv));
        response.addCookie(cookie);

        String frontendUrl="http:///localhost:8080/home.html?token="+accessToken;

        getRedirectStrategy().sendRedirect(request,response,frontendUrl);
    }
}
