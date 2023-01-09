package bg.softuni.onlinebookstorebackend.repositories;

import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BookSpecification implements Specification<BookEntity> {
    private final SearchDTO searchDTO;

    public BookSpecification(SearchDTO searchDTO) {
        this.searchDTO = searchDTO;
    }

    @Override
    public Predicate toPredicate(Root<BookEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        Predicate p = cb.conjunction();

        Predicate titlePredicate = cb.like(root.get("title"),
                "%" + searchDTO.getSearchText() + "%");

        Predicate summaryPredicate = cb.like(root.get("summary"),
                "%" + searchDTO.getSearchText() + "%");

        p.getExpressions().add(cb.or(titlePredicate, summaryPredicate));

        return p;
    }
}
