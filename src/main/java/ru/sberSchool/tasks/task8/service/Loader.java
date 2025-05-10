package ru.sberSchool.tasks.task8.service;

import ru.sberSchool.tasks.task8.annotations.Cache;
import ru.sberSchool.tasks.task8.enums.CacheType;

import java.util.List;

public interface Loader {

    @Cache(cacheType = CacheType.IN_MEMORY, listLimit = 100000)
    List<String> loadData(String dataset, int size);
}
