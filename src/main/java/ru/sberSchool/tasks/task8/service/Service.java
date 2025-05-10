package ru.sberSchool.tasks.task8.service;

import ru.sberSchool.tasks.task8.annotations.Cache;
import ru.sberSchool.tasks.task8.enums.CacheType;

public interface Service {

    @Cache(cacheType = CacheType.IN_MEMORY, identityBy = {String.class})
    double doHardWork(String task, int value);

    @Cache(cacheType = CacheType.FILE, identityBy = {String.class})
    double doAnotherHardWork(String task, int value);

    @Cache(cacheType = CacheType.FILE, zip = true, fileNamePrefix = "processData")
    double processData(String task, int value);
}
