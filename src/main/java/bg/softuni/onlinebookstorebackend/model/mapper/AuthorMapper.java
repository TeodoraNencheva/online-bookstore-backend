package bg.softuni.onlinebookstorebackend.model.mapper;

import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorNameDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    @Mapping(source = "picture.url", target = "picture")
    AuthorOverviewDTO authorEntityToAuthorOverviewDTO(AuthorEntity author);

    @Mapping(source = "picture.url", target = "picture")
    AuthorDetailsDTO authorEntityToAuthorDetailsDTO(AuthorEntity author);

    AuthorNameDTO authorEntityToAuthorNameDTO(AuthorEntity author);
}
