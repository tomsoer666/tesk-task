CREATE TABLE doctors (
 doctorId BIGINT IDENTITY PRIMARY KEY,
 doctorName VARCHAR(50) NOT NULL,
 doctorSurname  VARCHAR(50) NOT NULL,
 doctorPatronymic  VARCHAR(50) NOT NULL,
 specialization  VARCHAR(150) NOT NULL
);
