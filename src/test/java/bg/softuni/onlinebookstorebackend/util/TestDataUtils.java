package bg.softuni.onlinebookstorebackend.util;

import bg.softuni.onlinebookstorebackend.model.entity.*;
import bg.softuni.onlinebookstorebackend.model.enums.UserRoleEnum;
import bg.softuni.onlinebookstorebackend.repositories.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestDataUtils {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final OrderRepository orderRepository;
    private final SecureTokenRepository secureTokenRepository;

    public TestDataUtils(UserRepository userRepository, UserRoleRepository userRoleRepository, AuthorRepository authorRepository, BookRepository bookRepository, GenreRepository genreRepository, OrderRepository orderRepository, SecureTokenRepository secureTokenRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.orderRepository = orderRepository;
        this.secureTokenRepository = secureTokenRepository;
    }

    public void initRoles() {
        if (userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity(UserRoleEnum.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity(UserRoleEnum.USER);

            userRoleRepository.save(adminRole);
            userRoleRepository.save(userRole);
        }
    }

    public UserEntity createTestAdmin(String email) {

        initRoles();

        var admin = new UserEntity("Admin", "Adminov", email, "adminPass",
                new HashSet<>(userRoleRepository.findAll()));

        return userRepository.save(admin);
    }

    public UserEntity createTestUser(String email) {

        initRoles();

        var user = new UserEntity("User", "Userov", email, "userPass",
                userRoleRepository.
                        findAll().stream().
                        filter(r -> r.getName() != UserRoleEnum.ADMIN).
                        collect(Collectors.toSet()));

        return userRepository.save(user);
    }

    public AuthorEntity createTestAuthor() {
        var author = new AuthorEntity();
        author.setFirstName("Ivan");
        author.setLastName("Vazov");
        author.setBiography("short biography");
        return authorRepository.save(author);
    }

    public GenreEntity createTestGenre(String name) {
        GenreEntity genreEntity = new GenreEntity(name);
        return genreRepository.save(genreEntity);
    }

    public BookEntity createTestBook(AuthorEntity author, GenreEntity genre) {
        BookEntity book = new BookEntity("title", author, genre, "year", "summary", null, new BigDecimal(10));
        return bookRepository.save(book);
    }

    public OrderEntity createTestOrder() {
        return orderRepository.save(new OrderEntity(createTestUser("user@example.com"),
                createTestItems()));
    }

    public Map<BookEntity, Integer> createTestItems() {
        HashMap<BookEntity, Integer> items = new HashMap<>();
        items.put(createTestBook(createTestAuthor(), createTestGenre("novel")), 2);
        return items;
    }

    public Long getAddedBookId() {
        return bookRepository.findAll().stream()
                .max(Comparator.comparingLong(BookEntity::getId)).get().getId();
    }

    public void cleanUpDatabase() {
        orderRepository.deleteAll();
        secureTokenRepository.deleteAll();
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }
}
