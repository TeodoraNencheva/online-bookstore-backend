package bg.softuni.onlinebookstorebackend.repositories;

import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long>,
        JpaSpecificationExecutor<BookEntity> {
    Page<BookEntity> getAllByGenre(GenreEntity genre, Pageable pageable);

    void deleteAllByAuthor_Id(Long id);

    List<BookEntity> getAllByAuthor(AuthorEntity author);
}
