package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }
}
