package com.edgecloud.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class EdgecloudApiGatewayApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
    }

    @Test
    void preservesAlertRoutesAndPlacesProjectAlertEventsOnAlertService() {
        assertThat(route(3, "id")).isEqualTo("alert-rule-service");
        assertThat(route(3, "uri")).isEqualTo("lb://EDGECLOUD-ALERT-SERVICE");
        assertThat(route(3, "predicates[0]")).isEqualTo("Path=/api/v2/projects/*/alert-rules/**");

        assertThat(route(4, "id")).isEqualTo("alert-event-service");
        assertThat(route(4, "uri")).isEqualTo("lb://EDGECLOUD-ALERT-SERVICE");
        assertThat(route(4, "predicates[0]")).isEqualTo("Path=/api/v2/projects/*/alerts/**");
        assertThat(route(4, "filters[0]")).isNull();

        assertThat(route(5, "id")).isEqualTo("alert-service");
        assertThat(route(5, "predicates[0]")).isEqualTo("Path=/api/v1/alerts/**");
        assertThat(route(5, "filters[0]")).isEqualTo("StripPrefix=2");
    }

    private String route(int index, String property) {
        return environment.getProperty("spring.cloud.gateway.server.webmvc.routes[" + index + "]." + property);
    }
}
