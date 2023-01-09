package bg.softuni.onlinebookstorebackend.model.dto.book;


import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;

public class BookOverviewDTO {
    private Long id;

    private String title;

    private AuthorEntity author;

    private String genre;

    private String picture;

    public BookOverviewDTO() {
    }

    public BookOverviewDTO(Long id, String title, AuthorEntity author, String genre, String picture) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
