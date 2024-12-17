package ru.sberSchool.tasks.task7.rockPaperScissors.entity;

import ru.sberSchool.tasks.task7.rockPaperScissors.PlayableRockPaperScissors;
import ru.sberSchool.tasks.task7.rockPaperScissors.enums.RockPaperScissorsEnum;

import java.util.Random;
import java.util.UUID;

public class RandomPlayer implements PlayableRockPaperScissors {
    private UUID id;
    private final String name;
    private final Random random;

    public RandomPlayer() {
        this.id = UUID.randomUUID();
        this.name = "RandomPlayer" + id.toString();
        this.random = new Random();
    }

    @Override
    public RockPaperScissorsEnum play() {
        RockPaperScissorsEnum[] moves = RockPaperScissorsEnum.values();
        return moves[random.nextInt(moves.length)];
    }

    @Override
    public String getName() {
        return name;
    }
}
