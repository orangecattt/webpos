CREATE TABLE IF NOT EXISTS orders (
    order_id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id INT(4) UNSIGNED,
    num INT(4) UNSIGNED
) engine=InnoDB;