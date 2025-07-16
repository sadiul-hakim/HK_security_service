package xyz.sadiulhakim.limit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.sadiulhakim.role.Role;
import xyz.sadiulhakim.url.AllowedUrl;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallRateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 55)
    private String name;

    @OneToOne
    private AllowedUrl allowedUrls;

    @ManyToOne
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "api_call_rate_limit_limits",
            joinColumns = @JoinColumn(name = "api_call_rate_limit_id"),
            inverseJoinColumns = @JoinColumn(name = "rate_limit_id")
    )
    private List<RateLimit> rateLimits = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ApiCallRateLimit that)) return false;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
