package bg.softuni.onlinebookstorebackend.model.entity;

import bg.softuni.onlinebookstorebackend.model.cloudinary.CloudinaryImage;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pictures")
public class PictureEntity extends BaseEntity {
    private String url;
    private String publicId;

    public PictureEntity(CloudinaryImage cloudinaryImage) {
        this.url = cloudinaryImage.getUrl();
        this.publicId = cloudinaryImage.getPublicId();
    }
}
