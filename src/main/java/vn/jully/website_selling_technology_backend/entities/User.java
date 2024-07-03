package vn.jully.website_selling_technology_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "address", length = 512)
    private String address;

    @Column(name = "password", length = 512, nullable = false)
    private String password;

//    @Column(name = "created_at")
//    private Date createdAt;
//
//    @Column(name = "updated_at")
//    private Date updatedAt;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "active_code")
    private String activeCode;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "facebook_account_id")
    private int facebookAccountId;

    @Column(name = "google_account_id")
    private int googleAccountId;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH
            }
    )
    private List<FeedBack> feedBackList;

    @OneToMany(mappedBy = "user",
              fetch = FetchType.LAZY,
              cascade = {
        CascadeType.PERSIST, CascadeType.MERGE,
                CascadeType.DETACH, CascadeType.REFRESH
    }
    )
    private List<CartItem> cartItemList;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH
            }
    )
    private List<FavoriteProduct> favoriteProductList;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH
            }
    )
    private List<Order> orderList;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roleList;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Token> tokenList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    // Simplified method to always return true, removed redundant comment
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Simplified method to always return true, removed redundant comment
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Simplified method to always return true, removed redundant comment
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Updated to return the value of isActive
    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
