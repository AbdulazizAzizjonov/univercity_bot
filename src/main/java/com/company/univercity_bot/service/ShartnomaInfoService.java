package com.company.univercity_bot.service;

import com.company.univercity_bot.model.ShartnomaInfo;
import com.company.univercity_bot.repository.ShartnomaInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShartnomaInfoService {

    @Autowired
    private ShartnomaInfoRepository shartnomaInfoRepository;

    public ShartnomaInfo save (ShartnomaInfo shartnomaInfo) {
        ShartnomaInfo save = shartnomaInfoRepository.save(shartnomaInfo);
        return save;
    }
}
