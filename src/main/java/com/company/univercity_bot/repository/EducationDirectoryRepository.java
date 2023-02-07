package com.company.univercity_bot.repository;

import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.DirectoryInfo;
import com.company.univercity_bot.model.EducationDirectory;
import com.company.univercity_bot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface EducationDirectoryRepository extends JpaRepository<EducationDirectory, Integer> {
    List<EducationDirectory> findByEducationDegreeIdAndVisibleTrue (Integer id);

    @Transactional
    @Modifying
    @Query("update EducationDirectory  d set d.visible = false where d.id = ?1")
    int visibleFalse(Integer id);

    EducationDirectory findByNameUZ (String nameUz);

    EducationDirectory findByNameUZAndVisibleTrue (String nameUz);

    EducationDirectory findByDirectoryIdAndVisibleTrue (String directoryId);

    EducationDirectory findByIdAndVisibleTrue (Integer id);

    EducationDirectory deleteByDirectoryId (String directoryId);

    EducationDirectory findById (String educationDirectoryId);
}