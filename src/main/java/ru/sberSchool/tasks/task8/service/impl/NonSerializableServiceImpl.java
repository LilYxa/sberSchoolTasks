package ru.sberSchool.tasks.task8.service.impl;

import ru.sberSchool.tasks.task8.exceptions.NonSerializableDataException;
import ru.sberSchool.tasks.task8.service.NonSerializableService;

public class NonSerializableServiceImpl implements NonSerializableService {
    @Override
    public Object loadNonSerializable(String data) throws NonSerializableDataException {
        return new Object();
    }
}
