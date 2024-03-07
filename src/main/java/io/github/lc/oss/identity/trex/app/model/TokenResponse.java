package io.github.lc.oss.identity.trex.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.lc.oss.commons.serialization.Jsonable;

@JsonInclude(Include.NON_EMPTY)
public class TokenResponse implements io.github.lc.oss.commons.api.identity.TokenResponse, Jsonable {
    private final Long expiration;
    private final String token;

    public TokenResponse(@JsonProperty("token") String token) {
        this.expiration = null;
        this.token = token;
    }

    public TokenResponse(@JsonProperty("expiration") Long expiration, @JsonProperty("token") String token) {
        this.expiration = expiration;
        this.token = token;
    }

    @Override
    public Long getExpiration() {
        return this.expiration;
    }

    @Override
    public String getToken() {
        return this.token;
    }
}
