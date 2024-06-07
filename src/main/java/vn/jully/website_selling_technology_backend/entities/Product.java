package vn.jully.website_selling_technology_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Data
@Getter
@Setter
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name= "product_id")
    private long id;

    @Column(name = "title", length = 256)
    private String title;

    @Column(name = "price")
    private float price;

    @Column(name = "thumbnail", length = 256)
    private String thumbnail;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "discount")
    private float discount;

    @Column(name = "average_rate")
    private float averageRate;

//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate () {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate () {
//        updatedAt = LocalDateTime.now();
//    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "specification_id")
    @JsonIgnore
    private Specification specification;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnore
    private List<Category> categoryList;

    @OneToMany(mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<FeedBack> feedBackList;

    @OneToMany(mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<ProductImage> productImageList;

    @OneToMany(mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH
            }
    )
    @JsonIgnore
    private List<OrderDetail> orderDetailList;

    @OneToMany(mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<FavoriteProduct> favoriteProductList;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
}
