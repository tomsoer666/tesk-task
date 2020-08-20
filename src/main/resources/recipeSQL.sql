CREATE TABLE recipes (
 id BIGINT IDENTITY PRIMARY KEY,
 description VARCHAR(3000) NOT NULL,
 patientId BIGINT NOT NULL,
 doctorId BIGINT NOT NULL,
 dateOfCreation DATE NOT NULL,
 validity  VARCHAR(30) NOT NULL,
 priority  VARCHAR(6) NOT NULL,
 FOREIGN KEY (patientId) REFERENCES patients(patientId),
 FOREIGN KEY (doctorId) REFERENCES doctors(doctorId)
);
