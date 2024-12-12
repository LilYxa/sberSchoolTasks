package ru.sberSchool.tasks.task7.rockPaperScissors.enums;

public enum RockPaperScissorsEnum {
    ROCK, PAPER, SCISSORS;

    public int compare(RockPaperScissorsEnum other) {
        if (this == other) return 0;
        return switch (this) {
            case ROCK -> (other == SCISSORS) ? 1 : -1;
            case PAPER -> (other == ROCK) ? 1 : -1;
            case SCISSORS -> (other == PAPER) ? 1 : -1;
        };
    }
}
