package xyz.sadiulhakim.limit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.sadiulhakim.enumeration.RateLimitTimeUnit;

import java.time.Duration;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 55)
    private String name;
    public int callNumber = 0;
    public int time = 0;
    public RateLimitTimeUnit timeUnit;

    public Duration toDuration() {
        return switch (timeUnit) {
            case MILLISECOND -> Duration.ofMillis(time);
            case SECOND -> Duration.ofSeconds(time);
            case MINUTE -> Duration.ofMinutes(time);
            case HOUR -> Duration.ofHours(time);
            case DAY -> Duration.ofDays(time);
            case MONTH -> Duration.ofDays(30L * time);
            case YEAR -> Duration.ofDays(365L * time);
        };
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof RateLimit that)) return false;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
