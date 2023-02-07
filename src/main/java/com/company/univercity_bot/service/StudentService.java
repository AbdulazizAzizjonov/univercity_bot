package com.company.univercity_bot.service;

import com.company.univercity_bot.model.Student;
import com.company.univercity_bot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent (Student student) {
        Student save = studentRepository.save(student);
        return save;
    }

    public Student getUserId (String userId) {
        Student student = studentRepository.findByUserNumber(userId);
        return student;
    }
}
