package com.okta.spring.SpringBootOAuthClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class WebController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @RequestMapping("/securedPage")
    public String securedPage(Model model, Principal principal, OAuth2AuthenticationToken authentication, HttpServletResponse response) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient
                (authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String access_token = client.getAccessToken().getTokenValue();
        String refresh_token = client.getRefreshToken().getTokenValue();
        String access_token_expire_at = client.getAccessToken().getExpiresAt().toString();

        System.out.println("access token - " + access_token);
        System.out.println("access token expire at " + access_token_expire_at);
        System.out.println("refresh token - " + refresh_token);
        model.addAttribute("attr1", access_token);

        response.addCookie(new Cookie("access_token", access_token));

        return "securedPage";
    }

    /*@PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        return "index";
    }*/



}
