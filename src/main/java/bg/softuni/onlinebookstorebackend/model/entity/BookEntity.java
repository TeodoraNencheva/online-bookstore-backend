package bg.softuni.onlinebookstorebackend.model.entity;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddNewBookDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;

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

    public BookEntity() {
    }

    public BookEntity(String title, AuthorEntity author, GenreEntity genre, String yearOfPublication, String summary, PictureEntity picture, BigDecimal price) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.yearOfPublication = yearOfPublication;
        this.summary = summary;
        this.picture = picture;
        this.price = price;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    public String getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public PictureEntity getPicture() {
        return picture;
    }

    public void setPicture(PictureEntity picture) {
        this.picture = picture;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
