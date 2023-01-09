package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.error.AuthorNotFoundException;
import bg.softuni.onlinebookstorebackend.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {
    private final AuthorService authorService;

    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

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

        if (author == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(author);
    }

    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AuthorEntity> addAuthor(@Valid @RequestPart AddNewAuthorDTO authorModel,
                                                  @RequestPart(required = false) MultipartFile picture) throws IOException {
        authorModel.setPicture(picture);
        AuthorEntity newAuthor = authorService.addNewAuthor(authorModel);

        return ResponseEntity.ok(newAuthor);
    }

    @PutMapping("/{id}")
    public String updateAuthor(@Valid AddNewAuthorDTO authorModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @PathVariable("id") Long id) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("authorModel", authorModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authorModel",
                    bindingResult);
            return "redirect:/authors/update/{id}";
        }

        AuthorEntity updatedAuthor = authorService.updateAuthor(authorModel, id);
        if (updatedAuthor == null) {
            throw new AuthorNotFoundException(id);
        }
        return String.format("redirect:/authors/%d", updatedAuthor.getId());
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        if (authorService.getAuthorById(id) == null) {
            throw new AuthorNotFoundException(id);
        }

        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({AuthorNotFoundException.class})
    public ModelAndView onAuthorNotFound(AuthorNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("title", "Author not found");
        modelAndView.addObject("message", String.format("Author with id %s not found", ex.getId()));

        return modelAndView;
    }

    @GetMapping("/search")
    public String search(Model model,
                         @ModelAttribute SearchDTO searchDTO) {

        model.addAttribute("title", "Search for an author");
        model.addAttribute("action", "/authors/search");

        if (!model.containsAttribute("searchDTO")) {
            model.addAttribute("searchDTO", new SearchDTO());
        }

        if (searchDTO.getSearchText() != null && !searchDTO.getSearchText().trim().isEmpty()) {
            model.addAttribute("authors", authorService.searchAuthors(searchDTO));
        }

        return "search";
    }
}
