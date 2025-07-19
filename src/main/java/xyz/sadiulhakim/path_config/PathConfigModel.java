package xyz.sadiulhakim.path_config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "path_config")
public class PathConfigModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "domain")
    private String domain;

    @Column(name = "port")
    private int port;

    @Column(name = "scheme", length = 10)
    private String scheme;

    @Column(name = "url_pattern", length = 70)
    private String urlPattern;

    @Column(name = "trusted_token_key", length = 50)
    private String TrustedTokenKey;

    @Column(name = "trusted_token_value", length = 100)
    private String TrustedTokenValue;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PathConfigModel that)) return false;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
