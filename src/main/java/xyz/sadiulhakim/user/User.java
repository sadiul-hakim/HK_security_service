package xyz.sadiulhakim.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.sadiulhakim.enumeration.UserLevel;
import xyz.sadiulhakim.role.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 55)
    private String fullName;

    @Column(length = 75)
    private String username;

    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserLevel level;

    private boolean enabled = true;

    private LocalDateTime joinedAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    public User(String fullName, String username, String password, UserLevel level) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.level = level;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof User user)) return false;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
