package com.edgecloud.gateway.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtAuthenticationFilterTest {

    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter();

    @Test
    void rejectsV2ProjectAlertRequestWithoutBearerToken() throws Exception {
        var request = new MockHttpServletRequest("GET", "/api/v2/projects/project-1/alerts");
        var response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).contains("Missing or invalid Authorization header");
    }

    @Test
    void preservesBearerHeaderAndExposesTokenToDownstreamV2Request() throws Exception {
        var request = new MockHttpServletRequest("GET", "/api/v2/notifications");
        request.addHeader("Authorization", "Bearer signed-token");
        var response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(request.getHeader("Authorization")).isEqualTo("Bearer signed-token");
        assertThat(request.getAttribute("jwtToken")).isEqualTo("signed-token");
    }

    @Test
    void rejectsNotificationRequestWithoutBearerToken() throws Exception {
        var request = new MockHttpServletRequest("GET", "/api/v2/notifications/unread-count");
        var response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void internalNotificationPathIsNotGatewayJwtProtected() throws Exception {
        var request = new MockHttpServletRequest("POST", "/internal/notifications/alert-events");
        var response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(request.getAttribute("jwtToken")).isNull();
    }
}
