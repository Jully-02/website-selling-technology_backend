package vn.jully.website_selling_technology_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "banner")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "model_code")
    @JsonProperty("model_code")
    private String modelCode;

    @Column(name = "promotion_title")
    @JsonProperty("promotion_title")
    private String promotionTitle;

    @Column(name = "discount")
    private String discount;

    @Column(name = "thumbnail")
    private String thumbnail;
}
