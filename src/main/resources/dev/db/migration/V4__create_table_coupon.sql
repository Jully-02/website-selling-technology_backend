CREATE TABLE IF NOT EXISTS coupon (
    coupon_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true
);

ALTER TABLE `order`
ADD COLUMN coupon_id INT,
ADD CONSTRAINT fk_orders_coupon
FOREIGN KEY (coupon_id) REFERENCES coupon(coupon_id);

ALTER TABLE order_detail
ADD COLUMN coupon_id INT,
ADD CONSTRAINT fk_order_detail_coupon
FOREIGN KEY (coupon_id) REFERENCES coupon(coupon_id);

CREATE TABLE IF NOT EXISTS coupon_condition (
    coupon_condition_id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    attribute VARCHAR(255) NOT NULL,
    operator VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    discount_amount DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (coupon_id) REFERENCES coupon(coupon_id));