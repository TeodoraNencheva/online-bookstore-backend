package bg.softuni.onlinebookstorebackend.model.cloudinary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CloudinaryImage {
    private String url;
    private String publicId;
}
