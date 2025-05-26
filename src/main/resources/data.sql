
-- Insert or update initial movie data
MERGE INTO movies (id, title, movie_type) VALUES ('F001', 'You''ve Got Mail', 'REGULAR');
MERGE INTO movies (id, title, movie_type) VALUES ('F002', 'Matrix', 'REGULAR');
MERGE INTO movies (id, title, movie_type) VALUES ('F003', 'Cars', 'CHILDREN');
MERGE INTO movies (id, title, movie_type) VALUES ('F004', 'Fast & Furious X', 'NEW');