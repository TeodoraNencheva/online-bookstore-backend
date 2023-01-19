package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }
}
