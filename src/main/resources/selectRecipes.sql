SELECT r.id, r.description, r.patientID, r.doctorId, r.dateOfCreation, r.validity, r.priority, 
d.doctorname, d. doctorSurname, d.doctorPatronymic, d.specialization, 
p.patientName, p.patientSurname, p.patientPatronymic, p.number
 FROM  recipes as r
JOIN doctors as d on r.doctorId = d.doctorId 
JOIN patients as p on r.patientId = p.patientId
