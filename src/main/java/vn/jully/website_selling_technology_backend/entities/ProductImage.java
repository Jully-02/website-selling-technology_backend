package vn.jully.website_selling_technology_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
//@Data
@Getter
@Setter
@Table(name = "product_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private long id;

    @Column(name = "image_name", length = 256)
    private String imageName;

    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
}
