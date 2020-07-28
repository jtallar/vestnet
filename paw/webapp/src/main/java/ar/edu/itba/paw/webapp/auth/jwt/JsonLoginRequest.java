package ar.edu.itba.paw.webapp.auth.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonLoginRequest {
    private String username;
    private String password;
    private boolean rememberMe;

    @JsonCreator
    public JsonLoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password,
                            @JsonProperty("remember_me") boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
}
