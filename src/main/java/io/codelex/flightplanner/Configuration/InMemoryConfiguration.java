package io.codelex.flightplanner.Configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "in-memory")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class InMemoryConfiguration {
    //This class is necessary to disable data source auto configuration
}
