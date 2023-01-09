package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.dto.book.*;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.model.entity.PictureEntity;
import bg.softuni.onlinebookstorebackend.model.error.AuthorNotFoundException;
import bg.softuni.onlinebookstorebackend.model.error.BookNotFoundException;
import bg.softuni.onlinebookstorebackend.model.error.GenreNotFoundException;
import bg.softuni.onlinebookstorebackend.model.mapper.BookMapper;
import bg.softuni.onlinebookstorebackend.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;
    private final CloudinaryService cloudinaryService;
    private final PictureRepository pictureRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, BookMapper bookMapper, CloudinaryService cloudinaryService, PictureRepository pictureRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookMapper = bookMapper;
        this.cloudinaryService = cloudinaryService;

        this.pictureRepository = pictureRepository;
    }

    public BookDetailsDTO getBookDetails(Long id) {
        Optional<BookEntity> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            return null;
        }

        return bookMapper.bookEntityToBookDetailsDTO(bookOpt.get());
    }

    public Page<BookOverviewDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::bookEntityToBookOverviewDTO);
    }

    public BookAddedToCartDTO getAddedBook(AddBookToCartDTO bookDTO) {
        Optional<BookEntity> bookOpt = bookRepository.findById(bookDTO.getBookId());
        if (bookOpt.isEmpty()) {
            throw new BookNotFoundException(bookDTO.getBookId());
        }

        BookEntity book = bookOpt.get();
        return new BookAddedToCartDTO(book.getTitle(), book.getAuthor().getFullName(),
                bookDTO.getQuantity());
    }

    public Page<BookOverviewDTO> getBooksByGenre(String genre, Pageable pageable) {
        Optional<GenreEntity> genreOpt = genreRepository.findByName(genre);

        if (genreOpt.isEmpty()) {
            throw new GenreNotFoundException(genre);
        }

        return bookRepository.getAllByGenre(genreOpt.get(), pageable)
                .map(bookMapper::bookEntityToBookOverviewDTO);
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BookEntity addNewBook(AddNewBookDTO bookModel) throws IOException {
        AuthorEntity author = authorRepository.findById(bookModel.getAuthorId()).get();
        GenreEntity genre = genreRepository.findById(bookModel.getGenreId()).get();

        if (bookModel.getPicture() != null && !bookModel.getPicture().getOriginalFilename().isEmpty()) {
            PictureEntity picture = new PictureEntity(cloudinaryService.upload(bookModel.getPicture()));
            pictureRepository.save(picture);
            BookEntity newBook = new BookEntity(bookModel, author, genre, picture);
            return bookRepository.save(newBook);
        } else {
            BookEntity newBook = new BookEntity(bookModel, author, genre, null);
            return bookRepository.save(newBook);
        }
    }

    public AddNewBookDTO getBookById(Long id) {
        Optional<BookEntity> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            return null;
        }

        BookEntity book = bookOpt.get();
        return bookEntityToAddNewBookDTO(book);
    }

    private AddNewBookDTO bookEntityToAddNewBookDTO(BookEntity book) {
        AddNewBookDTO result = new AddNewBookDTO();
        result.setTitle(book.getTitle());
        result.setAuthorId(book.getAuthor().getId());
        result.setGenreId(book.getGenre().getId());
        result.setYearOfPublication(book.getYearOfPublication());
        result.setSummary(book.getSummary());
        result.setPrice(book.getPrice());
        return result;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BookEntity updateBook(AddNewBookDTO bookModel, Long id) throws IOException {
        Optional<BookEntity> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            return null;
        }

        BookEntity book = bookOpt.get();
        AuthorEntity author = authorRepository.findById(bookModel.getAuthorId()).get();
        GenreEntity genre = genreRepository.findById(bookModel.getGenreId()).get();

        book.setTitle(bookModel.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        book.setYearOfPublication(bookModel.getYearOfPublication());
        book.setSummary(bookModel.getSummary());

        if (bookModel.getPicture() != null && !bookModel.getPicture().getOriginalFilename().isEmpty()) {
            PictureEntity picture = new PictureEntity(cloudinaryService.upload(bookModel.getPicture()));
            pictureRepository.save(picture);
            book.setPicture(picture);
        }

        book.setPrice(bookModel.getPrice());
        return bookRepository.save(book);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<BookOverviewDTO> searchBooks(SearchDTO searchDTO) {
        return this.bookRepository.findAll(new BookSpecification(searchDTO))
                .stream().map(bookMapper::bookEntityToBookOverviewDTO)
                .collect(Collectors.toList());
    }
}
