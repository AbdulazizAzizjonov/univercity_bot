package com.company.univercity_bot.repository;

import com.company.univercity_bot.model.EducationDirectory;
import com.company.univercity_bot.model.ShartnomaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ShartnomaInfoRepository extends JpaRepository<ShartnomaInfo, Integer> {

    ShartnomaInfo findByEducationDirectoryId (Integer educationDirectory);
}
