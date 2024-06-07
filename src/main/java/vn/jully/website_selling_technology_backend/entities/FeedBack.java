package vn.jully.website_selling_technology_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "feedback")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FeedBack extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private long id;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "rate")
    private float rate;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

}
