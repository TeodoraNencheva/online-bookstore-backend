package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.service.AuthorService;
import bg.softuni.onlinebookstorebackend.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorOverviewDTO>> getAllAuthors(
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());

        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDetailsDTO> getAuthorDetails(@PathVariable("id") Long id) {
        AuthorDetailsDTO author = authorService.getAuthorDetails(id);
        return ResponseEntity.ok(author);
    }

    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AuthorEntity> addAuthor(@Valid @RequestPart AddNewAuthorDTO authorModel,
                                                  @RequestPart(required = false) MultipartFile picture,
                                                  UriComponentsBuilder uriComponentsBuilder) throws IOException {
        authorModel.setPicture(picture);
        AuthorEntity newAuthor = authorService.addNewAuthor(authorModel);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/api/authors/{id}")
                        .build(newAuthor.getId()))
                .build();
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AuthorEntity> updateAuthor(@Valid @RequestPart AddNewAuthorDTO authorModel,
                                                     @RequestPart(required = false) MultipartFile picture,
                                                     @PathVariable("id") Long id) throws IOException {
        authorModel.setPicture(picture);
        AuthorEntity updatedAuthor = authorService.updateAuthor(authorModel, id);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);

        Map<String, Object> body = ResponseService.generateGeneralResponse(String.format("Author with ID %s deleted", id));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping(path = "/search", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AuthorOverviewDTO>> search(@RequestBody SearchDTO searchDTO) {

        if (searchDTO.getSearchText() != null && !searchDTO.getSearchText().trim().isEmpty()) {
            return ResponseEntity.ok(authorService.searchAuthors(searchDTO));
        }

        return ResponseEntity.badRequest().build();
    }
}
