-- Chèn dữ liệu vào bảng Directors
INSERT INTO Director ( director_name, Nationality) VALUES
( 'Phontharis', 'Thailand'),
( 'Fujiko Fujio', 'Japan'),
( 'James Cameron', 'USA');

-- Chèn dữ liệu vào bảng Accounts
INSERT INTO Account (Email, Password, RoleID) VALUES
('admin@cinestar.com', '@4', 1),
('customer@cinestar.com', '@4', 4),
('manager@cinestar.com', '@4', 3),
('staff@cinestar.com', '@4', 2);

-- Chèn dữ liệu vào bảng Movies
INSERT INTO Movie (movie_name, Duration, Actor, Status, DirectorID) VALUES
('PEE NAK 4', 125, 'Witthawat', 'active', 1),
('Doraemon', 115, 'Nobita', 'active', 2),
('Doraemon 2022', 120, 'Doraemon', 'inactive', 2),
('Avatar', 192, 'Kate Winslet', 'inactive', 3);