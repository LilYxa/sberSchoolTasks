package ru.sberSchool.tasks.task1;

public class Task1 {

    public static void run() {
        System.out.println("Создание объекта конструктором без параметров Child():");
        Child firstChild = new Child();

        System.out.println("Создание объекта конструктором с параметром Child(name):");
        Child secondChild = new Child("Ivan");
    }
}