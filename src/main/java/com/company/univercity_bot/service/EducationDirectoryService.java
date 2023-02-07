package com.company.univercity_bot.service;

import com.company.univercity_bot.model.EducationDirectory;
import com.company.univercity_bot.repository.EducationDirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationDirectoryService {

    @Autowired
    private EducationDirectoryRepository educationDirectoryRepository;

    public EducationDirectory saveDirectory(EducationDirectory educationDirectory) {
        EducationDirectory save = educationDirectoryRepository.save(educationDirectory);

        return save;
    }

    public void  deleteDirectory(String directoryId) {
        educationDirectoryRepository.deleteByDirectoryId(directoryId);
    }

    public EducationDirectory getDirectory(String name) {
        EducationDirectory s = educationDirectoryRepository.findByNameUZ(name);

        return s;
    }



}
