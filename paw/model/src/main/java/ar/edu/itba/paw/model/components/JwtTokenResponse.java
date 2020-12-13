package ar.edu.itba.paw.model.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenResponse {
    private final String accessToken;
    private final int accessMinutes;
    private final String refreshToken;
    private final int refreshMinutes;

    private final Collection<String> userRoles;

    public JwtTokenResponse(String accessToken, int accessMinutes, String refreshToken, int refreshMinutes, Collection<String> userRoles) {
        this.accessToken = accessToken;
        this.accessMinutes = accessMinutes;
        this.refreshToken = refreshToken;
        this.refreshMinutes = refreshMinutes;

        this.userRoles = userRoles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getAccessTokenExpMinutes() {
        return accessMinutes;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getRefreshTokenExpMinutes() {
        return refreshMinutes;
    }

    public Collection<String> getUserRoles() {
        return userRoles;
    }

    public Map<String, Object> getResponseMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("accessMinutes", accessMinutes);
        map.put("refreshToken", refreshToken);
        map.put("refreshMinutes", refreshMinutes);
        map.put("roles", userRoles.toArray());
        return map;
    }
}
