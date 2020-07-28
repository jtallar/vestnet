package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.components.JwtTokenResponse;

public class JwtTokenDto {
    private String accessToken;
    private int accessMinutes;
    private String refreshToken;
    private int refreshMinutes;

    public static JwtTokenDto fromJwtTokenResponse(JwtTokenResponse jwtTokenResponse) {
        final JwtTokenDto jwtTokenDto = new JwtTokenDto();
        jwtTokenDto.accessToken = jwtTokenResponse.getAccessToken();
        jwtTokenDto.accessMinutes = jwtTokenResponse.getAccessTokenExpMinutes();
        jwtTokenDto.refreshToken = jwtTokenResponse.getRefreshToken();
        jwtTokenDto.refreshMinutes = jwtTokenResponse.getRefreshTokenExpMinutes();

        return jwtTokenDto;
    }

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
}
