package io.github.lc.oss.identity.trex.app.controllers.v1;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import io.github.lc.oss.commons.serialization.Response;
import io.github.lc.oss.commons.web.annotations.HttpCachable;
import io.github.lc.oss.commons.web.config.Authorities;
import io.github.lc.oss.identity.trex.Messages;
import io.github.lc.oss.identity.trex.app.model.Credentials;
import io.github.lc.oss.identity.trex.app.model.TokenResponse;
import io.github.lc.oss.identity.trex.app.service.IdentityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@PreAuthorize(Authorities.PUBLIC)
public class PublicController extends io.github.lc.oss.commons.web.controllers.AbstractController {
    @Autowired
    IdentityService identityService;

    @HttpCachable
    @GetMapping(path = "/favicon.ico")
    public ResponseEntity<byte[]> favicon() {
        return this.notFound();
    }

    @HttpCachable
    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView landing(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("views/landing");
    }

    @HttpCachable
    @GetMapping(path = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("views/error");
    }

    @PostMapping(path = "/api/v1/login/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<TokenResponse>> auth( //
            @PathVariable("appId") String appId, //
            @RequestBody Credentials credentials //
    ) {
        String jwt = this.identityService.login(appId, credentials);
        if (StringUtils.isBlank(jwt)) {
            return this.respond(Messages.Authentication.InvalidCredentials);
        }
        return this.respond(new TokenResponse(jwt));
    }

    /**
     * Note: you are never assured the logout logic will be called, so do not rely
     * on it. Users may simply walk away and never explicitly log out; however, if
     * they do then this method may be invoked.
     */
    @DeleteMapping(path = "/api/v1/login/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout( //
            @PathVariable("appId") String appId, //
            HttpServletRequest request //
    ) {
        this.identityService.logout(appId, request);
        return this.noContent();
    }
}
