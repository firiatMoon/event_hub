package com.eventhub.event_management;

import com.eventhub.event_management.enums.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UserTestUtils userTestUtils;

    private static final String BEARER_PREFIX = "Bearer ";


    protected final ObjectMapper mapper = new ObjectMapper();

    private static volatile boolean isSharedSetupDone = false;

    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("postgres")
            .withUsername("event-management")
            .withPassword("event-management");

    static {
        if (!isSharedSetupDone) {
            POSTGRESQL_CONTAINER.start();
            isSharedSetupDone = true;
        }
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("test.postgres.post", POSTGRESQL_CONTAINER::getFirstMappedPort);
    }

    @EventListener
    public void closeContainer(ContextStoppedEvent event) {
        POSTGRESQL_CONTAINER.stop();
    }

    public String getAuthorizationHeader (Role role) {
        return BEARER_PREFIX + userTestUtils.getJwtTokenWithRole(role);
    }

}
