package com.company.univercity_bot.repository;

import com.company.univercity_bot.model.EducationDegree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationDegreeRepository extends JpaRepository<EducationDegree, Integer> {
    List<EducationDegree> findAllByVisibleIsTrue();

    EducationDegree findByNameUz (String nameUz);
}
