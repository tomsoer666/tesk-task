CREATE TABLE patients (
 patientId BIGINT IDENTITY PRIMARY KEY,
 patientName VARCHAR(50) NOT NULL,
 patientSurname  VARCHAR(50) NOT NULL,
 patientPatronymic  VARCHAR(50) NOT NULL,
 number  VARCHAR(15) NOT NULL
);
