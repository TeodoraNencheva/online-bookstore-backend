package bg.softuni.onlinebookstorebackend.model.entity;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddNewBookDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class BookEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    private AuthorEntity author;

    @ManyToOne(optional = false)
    private GenreEntity genre;

    @Column(nullable = false)
    private String yearOfPublication;

    @Column(nullable = false, length = 1000)
    private String summary;

    @OneToOne
    private PictureEntity picture;

    @Column(nullable = false)
    private BigDecimal price;

    public BookEntity(AddNewBookDTO bookDTO, AuthorEntity author, GenreEntity genre,
                      PictureEntity picture) {
        this.title = bookDTO.getTitle();
        this.author = author;
        this.genre = genre;
        this.yearOfPublication = bookDTO.getYearOfPublication();
        this.summary = bookDTO.getSummary();
        this.picture = picture;
        this.price = bookDTO.getPrice();
    }
}
