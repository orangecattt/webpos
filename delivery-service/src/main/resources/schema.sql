CREATE TABLE IF NOT EXISTS delivery (
    delivery_id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id INT(4) UNSIGNED,
    status CHAR(16)
) engine=InnoDB;