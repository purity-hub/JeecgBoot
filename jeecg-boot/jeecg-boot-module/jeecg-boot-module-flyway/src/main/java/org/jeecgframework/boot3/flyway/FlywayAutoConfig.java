package org.jeecgframework.boot3.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Simple auto-configuration listener that runs Flyway migrations on context refresh
 * when property 'jeecg.flyway.enabled' is true (default true).
 */
@Component
@ConditionalOnProperty(prefix = "jeecg.flyway", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlywayAutoConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Build Flyway with default locations (classpath:db/migration)
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        // Trigger migration
        flyway.migrate();
    }
}

