package ru.sberSchool.tasks.task1;


/**
 * Abstract class representing a parent entity with initialization blocks and constructors.
 * This class demonstrates the order of static and instance initialization blocks and constructors
 * in Java.
 * @author Elland Ilia
 * @version 1.0
 */
public abstract class Parent {

    /**
     * The name of the parent entity.
     */
    private String name;

    static {
        System.out.println("Parent:static 1");
    }

    {
        System.out.println("Parent:instance 1");
    }

    static {
        System.out.println("Parent:static 2");
    }

    /**
     * No-argument constructor. Executes after the instance initialization blocks.
     */
    public Parent() {
        System.out.println("Parent:constructor");
    }

    {
        System.out.println("Parent:instance 2");
    }

    /**
     * Parameterized constructor.
     * Initializes the {@code name} field and executes after the instance initialization blocks.
     *
     * @param name the name of the parent entity.
     */
    public Parent(String name) {
        this.name = name;
        System.out.println("Parent:name-constructor");
    }
}
