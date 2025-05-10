package ru.sberSchool.tasks.task8.service;

import ru.sberSchool.tasks.task8.annotations.Cache;
import ru.sberSchool.tasks.task8.enums.CacheType;
import ru.sberSchool.tasks.task8.exceptions.NonSerializableDataException;

public interface NonSerializableService {
    @Cache(cacheType = CacheType.FILE)
    Object loadNonSerializable(String data) throws NonSerializableDataException;
}
