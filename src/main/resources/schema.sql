CREATE TABLE MoviePricing (
    code VARCHAR(255) PRIMARY KEY,
    base_days INT,
    base_price DECIMAL(10,2),
    extra_price_per_day DECIMAL(10,2)
);
CREATE TABLE Movie (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    code VARCHAR(255),
    FOREIGN KEY (code) REFERENCES MoviePricing(code)
);
CREATE TABLE Customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
CREATE TABLE MovieRental (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    days INT,
    customer_id BIGINT,
    movie_id VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (movie_id) REFERENCES Movie(id)
);