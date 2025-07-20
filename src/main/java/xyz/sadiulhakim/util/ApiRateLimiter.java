package xyz.sadiulhakim.util;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.jdbc.PrimaryKeyMapper;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.postgresql.Bucket4jPostgreSQL;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.Duration;

@Component
public class ApiRateLimiter {

    private final ProxyManager<String> proxyManager;

    public ApiRateLimiter(DataSource dataSource) {

        this.proxyManager = Bucket4jPostgreSQL
                .selectForUpdateBasedBuilder(dataSource)
                .primaryKeyMapper(PrimaryKeyMapper.STRING)
                .build();
    }

    public boolean canLoad(String userId, int callNumber, Duration timeoutDuration) {

        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(callNumber)
                .refillIntervally(callNumber, timeoutDuration)
                .build();

        BucketConfiguration config = BucketConfiguration.builder()
                .addLimit(bandwidth)
                .build();

        Bucket bucket = proxyManager.builder()
                .build(userId, () -> config);

        return bucket.tryConsume(1);
    }
}