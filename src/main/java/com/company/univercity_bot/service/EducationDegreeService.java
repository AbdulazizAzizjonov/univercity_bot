package com.company.univercity_bot.service;

import com.company.univercity_bot.model.EducationDegree;
import com.company.univercity_bot.repository.EducationDegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationDegreeService {
    @Autowired
    private EducationDegreeRepository educationDegreeRepository;

    public EducationDegree saveDegreee (EducationDegree educationDegree) {
        EducationDegree educationDegree1 = new EducationDegree();
        educationDegree1.setNameUz(educationDegree.getNameUz());
        educationDegree1.setNameRu(educationDegree.getNameRu());
        educationDegree1.setVisible(true);

        EducationDegree save =educationDegreeRepository.save(educationDegree1);

        return save;
    }

    public EducationDegree getNameUz (String nameUz) {
        return educationDegreeRepository.findByNameUz(nameUz);
    }
 }
