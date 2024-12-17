package ru.sberSchool.tasks.task7;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task7.rockPaperScissors.RockPaperScissorsTournament;

@Slf4j
public class RockPaperScissorsTournamentTest {

    @Test
    public void testTournament() {
        log.debug("testTournament[0]: Starting test for RockPaperScissorsTournament");

        RockPaperScissorsTournament tournament = new RockPaperScissorsTournament();
        tournament.runTournament();

        log.debug("testTournament[1]: Completed test for RockPaperScissorsTournament");
    }
}

