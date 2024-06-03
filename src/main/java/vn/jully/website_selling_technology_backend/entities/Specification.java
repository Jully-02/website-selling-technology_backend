package vn.jully.website_selling_technology_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "specification")
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specification_id")
    private long specificationId;

    @Column(name = "weight")
    private float weight;

    @Column(name = "dimension", length = 100)
    private String dimension;

    @Column(name = "size", length = 100)
    private String size;

    @Column(name = "color", length = 100)
    private String color;

    @Column(name = "guarantee")
    private float guarantee;

    @Column(name = "item_height", length = 100)
    private String itemHeight;

    @Column(name = "item_width", length = 100)
    private String itemWidth;

    @Column(name = "screen_size")
    private float screenSize;

    @Column(name = "item_weight")
    private float itemWeight;

    @Column(name = "product_dimension", length = 100)
    private String productDimension;

    @Column(name = "item_model_number", length = 100)
    private String itemModelNumber;

    @Column(name = "processor_brand", length = 100)
    private String processorBrand;

    @Column(name = "processor_type", length = 100)
    private String processorType;

    @Column(name = "processor_speed")
    private float processorSpeed;

    @Column(name = "ram_size")
    private int ramSize;

    @Column(name = "hard_drive_size")
    private int hardDriveSize;

    @Column(name = "hard_disk_technology", length = 100)
    private String hardDiskTechnology;

    @Column(name = "graphics_coprocessor", length = 100)
    private String graphicsCoprocessor;

    @Column(name = "graphic_card_desc", length = 100)
    private String graphicCardDesc;

    @Column(name = "hardware_platform", length = 100)
    private String hardwarePlatform;

    @Column(name = "operating_system", length = 100)
    private String operatingSystem;

    @Column(name = "average_battery_life")
    private float averageBatteryLife;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;
}
