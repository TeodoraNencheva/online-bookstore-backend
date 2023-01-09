package bg.softuni.onlinebookstorebackend.model.mapper;

import bg.softuni.onlinebookstorebackend.model.dto.book.BookDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(source = "genre.name", target = "genre")
    @Mapping(source = "picture.url", target = "picture")
    BookDetailsDTO bookEntityToBookDetailsDTO(BookEntity bookEntity);

    @Mapping(source = "genre.name", target = "genre")
    @Mapping(source = "picture.url", target = "picture")
    BookOverviewDTO bookEntityToBookOverviewDTO(BookEntity bookEntity);
}

