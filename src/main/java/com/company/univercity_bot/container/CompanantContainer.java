package com.company.univercity_bot.container;

import com.company.univercity_bot.enums.AdminStatus;
import com.company.univercity_bot.enums.UserStatus;
import com.company.univercity_bot.model.*;

import java.util.HashMap;
import java.util.Map;

public class CompanantContainer {

    public static Map<Long, Users> userDeails = new HashMap<>();
    public static Map<String, InfoImage> infoImageMap = new HashMap<>();
    public static Map<String, AdminStatus> infoImageStepMap = new HashMap<>();
    public static Map<String, UserStatus> UserStepMap = new HashMap<>();
    public static Map<String, AdminStatus> AdminStepMap = new HashMap<>();
    public static Map<String, ContactConnection> contactConnectionMap = new HashMap<>();
    public static Map<String, AdminStatus> contactConnectionStepMap = new HashMap<>();
    public static Map<String, Student> studentMap = new HashMap<>();
    public static Map<String, UserStatus> studentStepMap = new HashMap<>();
    public static Map<String, DirectoryInfo> directoryInfoMap = new HashMap<>();
    public static Map<String, AdminStatus> directoryInfoStepMap = new HashMap<>();
    public static Map<String, BiuUnivercity> biuUnivercityMap = new HashMap<>();
    public static Map<String, AdminStatus> biuUnivercityStepMap = new HashMap<>();
    public static Map<String, EducationDegree> educationDegreeMap = new HashMap<>();
    public static Map<String, AdminStatus> educationDegreeStepMap = new HashMap<>();
    public static Map<String, EducationDirectory> educationDirectoryMap = new HashMap<>();
    public static Map<String, AdminStatus> educationDirectoryStepMap = new HashMap<>();
    public static Map<String, ShartnomaInfo> shartnomaInfoMap = new HashMap<>();
    public static Map<String, AdminStatus> shartnomaInfoStepMap = new HashMap<>();
    public static Map<String, OrderShartnoma> orderShartnomaMap = new HashMap<>();
    public static Map<String, AdminStatus> orderShartnomaStepMap = new HashMap<>();

}
