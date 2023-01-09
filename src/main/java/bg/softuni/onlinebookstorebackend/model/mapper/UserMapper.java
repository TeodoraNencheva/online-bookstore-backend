package bg.softuni.onlinebookstorebackend.model.mapper;

import bg.softuni.onlinebookstorebackend.model.dto.user.UserRegistrationDTO;
import bg.softuni.onlinebookstorebackend.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserEntity userRegistrationDtoToUserEntity(UserRegistrationDTO registerDTO);
}
