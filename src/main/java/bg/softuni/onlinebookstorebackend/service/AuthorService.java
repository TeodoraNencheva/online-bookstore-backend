package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.entity.PictureEntity;
import bg.softuni.onlinebookstorebackend.model.mapper.AuthorMapper;
import bg.softuni.onlinebookstorebackend.repositories.AuthorRepository;
import bg.softuni.onlinebookstorebackend.repositories.AuthorSpecification;
import bg.softuni.onlinebookstorebackend.repositories.BookRepository;
import bg.softuni.onlinebookstorebackend.repositories.PictureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final PictureRepository pictureRepository;
    private final CloudinaryService cloudinaryService;

    public Page<AuthorOverviewDTO> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).map(authorMapper::authorEntityToAuthorOverviewDTO);
    }

    public AddNewAuthorDTO getAuthorById(Long id) {
        Optional<AuthorEntity> authorOpt = authorRepository.findById(id);
        return authorOpt.map(AddNewAuthorDTO::new).orElse(null);
    }

    public AuthorDetailsDTO getAuthorDetails(Long id) {
        Optional<AuthorEntity> authorOpt = authorRepository.findById(id);
        return authorOpt.map(authorMapper::authorEntityToAuthorDetailsDTO).orElse(null);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AuthorEntity addNewAuthor(AddNewAuthorDTO authorModel) throws IOException {
        if (authorModel.getPicture() != null && !Objects.requireNonNull(authorModel.getPicture().getOriginalFilename()).isEmpty()) {
            PictureEntity picture = new PictureEntity(cloudinaryService.upload(authorModel.getPicture()));
            pictureRepository.save(picture);
            AuthorEntity newAuthor = new AuthorEntity(authorModel, picture);
            return authorRepository.save(newAuthor);
        }

        AuthorEntity newAuthor = new AuthorEntity(authorModel);
        return authorRepository.save(newAuthor);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AuthorEntity updateAuthor(AddNewAuthorDTO authorModel, Long id) throws IOException {
        Optional<AuthorEntity> authorOpt = authorRepository.findById(id);
        if (authorOpt.isEmpty()) {
            return null;
        }

        AuthorEntity author = authorOpt.get();
        author.setFirstName(authorModel.getFirstName());
        author.setLastName(authorModel.getLastName());
        author.setBiography(authorModel.getBiography());

        if (authorModel.getPicture() != null && !Objects.requireNonNull(authorModel.getPicture().getOriginalFilename()).isEmpty()) {
            PictureEntity picture = new PictureEntity(cloudinaryService.upload(authorModel.getPicture()));
            pictureRepository.save(picture);
            author.setPicture(picture);
        }

        return authorRepository.save(author);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public void deleteAuthor(Long id) {
        bookRepository.deleteAllByAuthor_Id(id);
        authorRepository.deleteById(id);
    }

    public List<AuthorOverviewDTO> searchAuthors(SearchDTO searchDTO) {
        return this.authorRepository.findAll(new AuthorSpecification(searchDTO))
                .stream().map(authorMapper::authorEntityToAuthorOverviewDTO)
                .collect(Collectors.toList());
    }
}
