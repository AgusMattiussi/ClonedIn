package ar.edu.itba.paw.webapp.form;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {
  @JsonProperty("access_token")
  private String accessToken;
  /* TODO:
  @JsonProperty("refresh_token")
  private String refreshToken;
  */

    public AuthenticationResponse(String accessToken/*, String refreshToken*/) {
        this.accessToken = accessToken;
        //this.refreshToken = refreshToken;
    }

    public AuthenticationResponse() {}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

