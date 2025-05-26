-- Insert into MoviePricing
INSERT INTO MoviePricing (code, base_days, base_price, extra_price_per_day) VALUES ('regular', 2, 2.00, 1.50);
INSERT INTO MoviePricing (code, base_days, base_price, extra_price_per_day) VALUES ('childrens', 3, 1.50, 1.50);
INSERT INTO MoviePricing (code, base_days, base_price, extra_price_per_day) VALUES ('new', 2, 3.00, 0.00);

-- Insert into Movie
INSERT INTO Movie (id, title, code) VALUES ('F001', 'You''ve Got Mail', 'regular');
INSERT INTO Movie (id, title, code) VALUES ('F002', 'Matrix', 'regular');
INSERT INTO Movie (id, title, code) VALUES ('F003', 'Cars', 'childrens');
INSERT INTO Movie (id, title, code) VALUES ('F004', 'Fast & Furious X', 'new');

-- Insert into Customer
INSERT INTO Customer (name) VALUES ('John Doe');
INSERT INTO Customer (name) VALUES ('Jane Smith');
INSERT INTO Customer (name) VALUES ('Alice Johnson');
INSERT INTO Customer (name) VALUES ('Bob Brown');

-- Insert into MovieRental
INSERT INTO MovieRental (days, customer_id, movie_id) VALUES (3, 1, 'F001');
INSERT INTO MovieRental (days, customer_id, movie_id) VALUES (1, 2, 'F002');
INSERT INTO MovieRental (days, customer_id, movie_id) VALUES (8, 3, 'F003');
INSERT INTO MovieRental (days, customer_id, movie_id) VALUES (3, 4, 'F004');
