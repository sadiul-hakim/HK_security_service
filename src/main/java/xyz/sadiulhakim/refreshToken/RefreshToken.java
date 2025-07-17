package xyz.sadiulhakim.refreshToken;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Setter
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    private Instant expiryDate;

    @Column(unique = true, nullable = false)
    private String username;

    public boolean isTokenExpired() {
        return getExpiryDate().isBefore(Instant.now());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof RefreshToken that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
