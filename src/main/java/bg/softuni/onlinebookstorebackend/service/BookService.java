package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.dto.book.*;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.model.entity.PictureEntity;
import bg.softuni.onlinebookstorebackend.model.exception.AuthorNotFoundException;
import bg.softuni.onlinebookstorebackend.model.exception.BookNotFoundException;
import bg.softuni.onlinebookstorebackend.model.exception.GenreNotFoundException;
import bg.softuni.onlinebookstorebackend.model.mapper.BookMapper;
import bg.softuni.onlinebookstorebackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;
    private final CloudinaryService cloudinaryService;
    private final PictureRepository pictureRepository;

    public List<BookOverviewDTO> getAllBooks(Pageable pageable) {
        return bookRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(bookMapper::bookEntityToBookOverviewDTO)
                .toList();
    }

    public Long getAllBooksCount() {
        return bookRepository.count();
    }

    public List<BookOverviewDTO> getBooksByGenre(String genre, Pageable pageable) {
        Optional<GenreEntity> genreOpt = genreRepository.findByNameIgnoreCase(genre);

        if (genreOpt.isEmpty()) {
            throw new GenreNotFoundException(genre);
        }

        return bookRepository
                .getAllByGenre(genreOpt.get(), pageable)
                .getContent()
                .stream()
                .map(bookMapper::bookEntityToBookOverviewDTO)
                .toList();
    }

    public Long getBooksCountByGenre(String genre) {
        Optional<GenreEntity> genreOpt = genreRepository.findByNameIgnoreCase(genre);

        if (genreOpt.isEmpty()) {
            throw new GenreNotFoundException(genre);
        }

        return bookRepository.countAllByGenre(genreOpt.get());
    }

    public BookDetailsDTO getBookDetails(Long id) {
        Optional<BookEntity> bookOpt = bookRepository.findById(id);
        return bookOpt.map(bookMapper::bookEntityToBookDetailsDTO)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addNewBook(AddNewBookDTO bookModel) throws IOException {
        addNewBook(bookModel, null);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BookEntity addNewBook(AddNewBookDTO bookModel, MultipartFile file) throws IOException {
        Optional<AuthorEntity> authorOpt = authorRepository.findById(bookModel.getAuthorId());
        Optional<GenreEntity> genreOpt = genreRepository.findById(bookModel.getGenreId());

        if (authorOpt.isEmpty()) {
            throw new AuthorNotFoundException(bookModel.getAuthorId());
        }

        if (genreOpt.isEmpty()) {
            throw new GenreNotFoundException("with ID " + bookModel.getGenreId().toString());
        }

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            PictureEntity picture = new PictureEntity(cloudinaryService.upload(file));
            pictureRepository.save(picture);
            BookEntity newBook = new BookEntity(bookModel, authorOpt.get(), genreOpt.get(), picture);
            return bookRepository.save(newBook);
        } else {
            BookEntity newBook = new BookEntity(bookModel, authorOpt.get(), genreOpt.get(), null);
            return bookRepository.save(newBook);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BookEntity updateBook(AddNewBookDTO bookModel, Long id) throws IOException {
        return updateBook(bookModel, id, null);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BookEntity updateBook(AddNewBookDTO bookModel, Long id, MultipartFile file) throws IOException {
        Optional<BookEntity> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            throw new BookNotFoundException(id);
        }

        BookEntity book = bookOpt.get();
        Optional<AuthorEntity> authorOpt = authorRepository.findById(bookModel.getAuthorId());
        Optional<GenreEntity> genreOpt = genreRepository.findById(bookModel.getGenreId());

        if (authorOpt.isEmpty()) {
            throw new AuthorNotFoundException(bookModel.getAuthorId());
        }

        if (genreOpt.isEmpty()) {
            throw new GenreNotFoundException("with ID " + bookModel.getGenreId().toString());
        }

        book.setTitle(bookModel.getTitle());
        book.setAuthor(authorOpt.get());
        book.setGenre(genreOpt.get());
        book.setYearOfPublication(bookModel.getYearOfPublication());
        book.setSummary(bookModel.getSummary());

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            PictureEntity picture = new PictureEntity(cloudinaryService.upload(file));
            pictureRepository.save(picture);
            book.setPicture(picture);
        }

        book.setPrice(bookModel.getPrice());
        return bookRepository.save(book);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteBook(Long id) {
        Optional<BookEntity> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            throw new BookNotFoundException(id);
        }

        bookRepository.deleteById(id);
    }

    public List<BookOverviewDTO> searchBooks(SearchDTO searchDTO) {
        return this.bookRepository.findAll(new BookSpecification(searchDTO))
                .stream().map(bookMapper::bookEntityToBookOverviewDTO)
                .collect(Collectors.toList());
    }

    public List<BookOverviewDTO> getBooksByAuthor(Long authorId) {
        Optional<AuthorEntity> authorOpt = authorRepository.findById(authorId);
        if (authorOpt.isEmpty()) {
            throw new AuthorNotFoundException(authorId);
        }

        return bookRepository.getAllByAuthor(authorOpt.get()).stream()
                .map(bookMapper::bookEntityToBookOverviewDTO)
                .collect(Collectors.toList());
    }

    public BookAddedToCartDTO getAddedBook(AddBookToCartDTO bookDTO) {
        Optional<BookEntity> bookOpt = bookRepository.findById(bookDTO.getBookId());
        if (bookOpt.isEmpty()) {
            throw new BookNotFoundException(bookDTO.getBookId());
        }

        BookEntity book = bookOpt.get();
        return new BookAddedToCartDTO(book, bookDTO.getQuantity());
    }
}
