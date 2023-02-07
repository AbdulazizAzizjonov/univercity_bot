package com.company.univercity_bot.repository;

import com.company.univercity_bot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Transactional
    @Query("SELECT s FROM Student s WHERE s.UserNumber=?1")
    Student findByUserNumber (String number);


}
