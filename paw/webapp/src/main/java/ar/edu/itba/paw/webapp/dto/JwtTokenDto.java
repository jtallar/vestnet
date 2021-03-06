package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.components.JwtTokenResponse;

import java.util.Collection;

public class JwtTokenDto {

    private String accessToken;
    private int accessMinutes;
    private String refreshToken;
    private int refreshMinutes;
    private Collection<String> roles;

    public static JwtTokenDto fromJwtTokenResponse(JwtTokenResponse jwtTokenResponse) {
        final JwtTokenDto jwtTokenDto = new JwtTokenDto();

        jwtTokenDto.setAccessToken(jwtTokenResponse.getAccessToken());
        jwtTokenDto.setAccessMinutes(jwtTokenResponse.getAccessTokenExpMinutes());
        jwtTokenDto.setRefreshToken(jwtTokenResponse.getRefreshToken());
        jwtTokenDto.setRefreshMinutes(jwtTokenResponse.getRefreshTokenExpMinutes());
        jwtTokenDto.setRoles(jwtTokenResponse.getUserRoles());

        return jwtTokenDto;
    }

    /* Getters and setters */

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getAccessMinutes() {
        return accessMinutes;
    }

    public void setAccessMinutes(int accessMinutes) {
        this.accessMinutes = accessMinutes;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getRefreshMinutes() {
        return refreshMinutes;
    }

    public void setRefreshMinutes(int refreshMinutes) {
        this.refreshMinutes = refreshMinutes;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

}
