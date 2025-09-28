package com.mychat.mychat.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties("rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;

    @Min(1)
    private int replenishPermitsPerSec = 10;

    @Min(1)
    private int burstCapacity = 30;

    private boolean headers = true;

    private String userHeader = "X-User-Id";

    private boolean resetOnConfigChange = true;
}
