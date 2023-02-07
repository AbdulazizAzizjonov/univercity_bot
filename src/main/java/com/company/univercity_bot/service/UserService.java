package com.company.univercity_bot.service;

import com.company.univercity_bot.model.Users;
import com.company.univercity_bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users getUsersById (Long id) {

        Optional<Users> users = userRepository.findById(id);
        return users.orElse(null);
    }

    public boolean saveUser (Users users) {
      Users save = userRepository.save(users);
        return true;
    }

}
