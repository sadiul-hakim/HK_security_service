package xyz.sadiulhakim.url;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.sadiulhakim.enumeration.HttpMethod;
import xyz.sadiulhakim.permission.Permission;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllowedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 75)
    private String apiPattern;

    @Enumerated(EnumType.STRING)
    private HttpMethod method;

    @ManyToOne
    private Permission permission;

    public AllowedUrl(String apiPattern, HttpMethod method) {
        this.apiPattern = apiPattern;
        this.method = method;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AllowedUrl that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
