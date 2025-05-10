package ru.sberSchool.tasks.task8.service.impl;

import ru.sberSchool.tasks.task8.service.Loader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LoaderImpl implements Loader {

    @Override
    public List<String> loadData(String dataset, int size) {
        return IntStream.range(0, size).mapToObj(i -> dataset + i).collect(Collectors.toList());
    }
}
