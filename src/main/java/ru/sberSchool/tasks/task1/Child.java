package ru.sberSchool.tasks.task1;

/**
 * The {@code Child} class extends the {@link Parent} class, inheriting its behavior and
 * demonstrating the order of execution of static and instance initialization blocks,
 * as well as constructors in a child class.
 * @author Elland Ilia
 * @version 1.0
 */
public class Child extends Parent{

    static {
        System.out.println("Child:static 1");
    }

    {
        System.out.println("Child:instance 1");
    }

    static {
        System.out.println("Child:static 2");
    }

    /**
     * No-argument constructor. Executes after the instance initialization blocks and
     * calls the parent class's no-argument constructor.
     */
    public Child() {
        System.out.println("Child:constructor");
    }

    /**
     * Parameterized constructor.
     * Calls the parent class's parameterized constructor and executes after the instance initialization blocks.
     *
     * @param name the name of the child entity, passed to the parent class's constructor.
     */
    public Child(String name) {
        super(name);
        System.out.println("Child:name-constructor");
    }

    {
        System.out.println("Child:instance 2");
    }
}
