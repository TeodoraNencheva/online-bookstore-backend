package bg.softuni.onlinebookstorebackend.repositories;

import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AuthorSpecification implements Specification<AuthorEntity> {
    private final SearchDTO searchDTO;

    public AuthorSpecification(SearchDTO searchDTO) {
        this.searchDTO = searchDTO;
    }

    @Override
    public Predicate toPredicate(Root<AuthorEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        Predicate firstNamePredicate = cb.like(cb.lower(root.get("firstName")),
                "%" + searchDTO.getSearchText().toLowerCase() + "%");

        Predicate lastNamePredicate = cb.like(cb.lower(root.get("lastName")),
                "%" + searchDTO.getSearchText().toLowerCase() + "%");

        Predicate biographyPredicate = cb.like(cb.lower(root.get("biography")),
                "%" + searchDTO.getSearchText().toLowerCase() + "%");

        return cb.or(firstNamePredicate, lastNamePredicate, biographyPredicate);
    }
}
