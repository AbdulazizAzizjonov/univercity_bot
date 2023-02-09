package com.company.univercity_bot.repository;

import com.company.univercity_bot.model.EducationDirectory;
import com.company.univercity_bot.model.ShartnomaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface ShartnomaInfoRepository extends JpaRepository<ShartnomaInfo, Integer> {

    ShartnomaInfo findByEducationDirectoryIdAndVisibleTrue (Integer educationDirectory);

    @Transactional
    @Modifying
    @Query("update ShartnomaInfo  d set d.visible = false where d.educationDirectory = ?1")
    int visibleFalse(EducationDirectory id);

    List<ShartnomaInfo> findAllByVisibleTrue();
}
