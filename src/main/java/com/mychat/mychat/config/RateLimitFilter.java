package com.mychat.mychat.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration bucketConfig;
    private final RateLimitProperties props;

    public RateLimitFilter(ProxyManager<String> proxyManager,
                           BucketConfiguration bucketConfig,
                           RateLimitProperties props) {
        this.proxyManager = proxyManager;
        this.bucketConfig = bucketConfig;
        this.props = props;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (!props.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = resolveKey(request);
        Bucket bucket = proxyManager.builder().build(key, bucketConfig);

        if (bucket.tryConsume(1)) {
            if (props.isHeaders()) {
                response.setHeader("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
                response.setHeader("X-Rate-Limit-Burst-Capacity", String.valueOf(props.getBurstCapacity()));
                response.setHeader("X-Rate-Limit-Replenish-Rate", String.valueOf(props.getReplenishPermitsPerSec()));
            }
            filterChain.doFilter(request, response);
        } else {
            // 429 Too Many Requests
            response.setStatus(429);
            if (props.isHeaders()) {
                response.setHeader("Retry-After", "1");
            }
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            byte[] body = "{\"error\":\"rate_limited\",\"message\":\"Too many requests\"}"
                    .getBytes(StandardCharsets.UTF_8);
            response.getOutputStream().write(body);
        }
    }

    private String resolveKey(HttpServletRequest req) {
        String userId = req.getHeader(props.getUserHeader());
        if (userId != null && !userId.isBlank()) {
            return "user:" + userId;
        }
        String ip = req.getRemoteAddr();
        return "ip:" + (ip == null ? "unknown" : ip);
    }

}
