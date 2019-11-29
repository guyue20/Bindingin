package com.bdyx.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "config.properties")
//@Configuration
//@ConfigurationProperties(prefix = "demo")
public class Configbase {
    String testtr;
}
