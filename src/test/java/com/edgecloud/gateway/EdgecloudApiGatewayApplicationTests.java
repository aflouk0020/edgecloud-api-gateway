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
    void preservesAlertRoutesAndPlacesProjectAlertApisOnAlertService() {
        assertThat(route(3, "id")).isEqualTo("notification-service");
        assertThat(route(3, "uri")).isEqualTo("lb://EDGECLOUD-NOTIFICATION-SERVICE");
        assertThat(route(3, "predicates[0]")).isEqualTo("Path=/api/v2/notifications/**");
        assertThat(route(3, "filters[0]")).isNull();

        assertThat(route(4, "id")).isEqualTo("alert-rule-service");
        assertThat(route(4, "uri")).isEqualTo("lb://EDGECLOUD-ALERT-SERVICE");
        assertThat(route(4, "predicates[0]")).isEqualTo("Path=/api/v2/projects/*/alert-rules/**");

        assertThat(route(5, "id")).isEqualTo("alert-escalation-policy-service");
        assertThat(route(5, "uri")).isEqualTo("lb://EDGECLOUD-ALERT-SERVICE");
        assertThat(route(5, "predicates[0]")).isEqualTo("Path=/api/v2/projects/*/escalation-policy,/api/v2/projects/*/escalation-policy/**");

        assertThat(route(6, "id")).isEqualTo("alert-maintenance-window-service");
        assertThat(route(6, "uri")).isEqualTo("lb://EDGECLOUD-ALERT-SERVICE");
        assertThat(route(6, "predicates[0]")).isEqualTo(
                "Path=/api/v2/projects/*/maintenance-windows,/api/v2/projects/*/maintenance-windows/**");
        assertThat(route(6, "filters[0]")).isNull();

        assertThat(route(7, "id")).isEqualTo("alert-event-service");
        assertThat(route(7, "predicates[0]")).isEqualTo("Path=/api/v2/projects/*/alerts/**");
        assertThat(route(8, "id")).isEqualTo("project-service");
        assertThat(route(8, "uri")).isEqualTo("lb://EDGECLOUD-PROJECT-SERVICE");
        assertThat(route(8, "predicates[0]")).isEqualTo("Path=/api/v2/projects/**");
        assertThat(route(8, "filters[0]")).isNull();
        assertThat(route(9, "id")).isEqualTo("alert-service");
        assertThat(route(9, "filters[0]")).isEqualTo("StripPrefix=2");
    }

    private String route(int index, String property) {
        return environment.getProperty("spring.cloud.gateway.server.webmvc.routes[" + index + "]." + property);
    }
}
