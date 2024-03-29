package bg.softuni.onlinebookstorebackend.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserOverviewDTO {
    private String fullName;
    private String username;
}
