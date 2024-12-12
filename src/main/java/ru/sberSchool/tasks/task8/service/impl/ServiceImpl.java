package ru.sberSchool.tasks.task8.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task8.service.Service;

@Slf4j
public class ServiceImpl implements Service {

    @Override
    public double doHardWork(String task, int value) {
        log.debug("doHardWork[0]: work!");
        return Math.random() * value;
    }

    @Override
    public double processData(String task, int value) {
        log.debug("processData: Processing {} with value {}", task, value);
        return value * value * value;
    }

    @Override
    public double doAnotherHardWork(String task, int value) {
        log.debug("doAnotherHardWork[0]: another work!");
        return (Math.random() * value) + Math.random();
    }
}
