CREATE TABLE IF NOT EXISTS cart_item (
    cart_item_id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id INT(4) UNSIGNED,
    num INT(4) UNSIGNED
) engine=InnoDB;