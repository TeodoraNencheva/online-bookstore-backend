package bg.softuni.onlinebookstorebackend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private boolean accountVerified;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<UserRoleEntity> roles;

    @ElementCollection
    @CollectionTable(name = "carts_items",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "book_id")
    @Column(name = "quantity")
    private Map<BookEntity, Integer> cart;

    public UserEntity(String firstName, String lastName, String email, String password, Set<UserRoleEntity> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void addRole(UserRoleEntity role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void addToCart(BookEntity book, int quantity) {
        if (this.cart == null || this.cart.size() == 0) {
            cart = new LinkedHashMap<>();
        }

        cart.merge(book, quantity, Integer::sum);
    }

    public void removeFromCart(BookEntity book) {
        cart.remove(book);
    }

    public void emptyCart() {
        cart.clear();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
