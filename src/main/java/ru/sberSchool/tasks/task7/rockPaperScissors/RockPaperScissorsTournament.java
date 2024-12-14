package ru.sberSchool.tasks.task7.rockPaperScissors;

//import ru.sberSchool.tasks.task7.rockPaperScissors.enums.RockPaperScissorsEnum;
//
//import java.io.File;
//import java.net.URL;
//import java.net.URLClassLoader;
//
//import static ru.sberSchool.tasks.Constants.PLUGIN_FOLDER;
//
//public class RockPaperScissorsTournament {
//
//    public static void main(String[] args) {
//        File folder = new File(PLUGIN_FOLDER);
//        if (!folder.exists() || !folder.isDirectory()) {
//            System.out.println("Plugin folder not found: " + PLUGIN_FOLDER);
//            return;
//        }
//
//        File[] pluginFiles = folder.listFiles((dir, name) -> name.endsWith(".jar"));
//        if (pluginFiles == null || pluginFiles.length == 0) {
//            System.out.println("No plugins found in folder: " + PLUGIN_FOLDER);
//            return;
//        }
//
//        PlayableRockPaperScissors currentChampion = loadPlugin(pluginFiles[0]);
//        if (currentChampion == null) {
//            System.out.println("Failed to load the first plugin.");
//            return;
//        }
//        System.out.println("Starting tournament with champion: " + currentChampion.getName());
//
//        for (int i = 1; i < pluginFiles.length; i++) {
//            PlayableRockPaperScissors challenger = loadPlugin(pluginFiles[i]);
//            if (challenger == null) {
//                System.out.println("Failed to load plugin: " + pluginFiles[i].getName());
//                continue;
//            }
//
//            System.out.println("Current champion: " + currentChampion.getName() + " vs Challenger: " + challenger.getName());
//
//            int championWins = 0;
//            int challengerWins = 0;
//
//            for (int round = 1; round <= 3; round++) {
//                RockPaperScissorsEnum championMove = currentChampion.play();
//                RockPaperScissorsEnum challengerMove = challenger.play();
//
//                System.out.println("Round " + round + ": " + currentChampion.getName() + " plays " + championMove + ", " +
//                        challenger.getName() + " plays " + challengerMove);
//
//                int result = championMove.compare(challengerMove);
//                if (result > 0) {
//                    championWins++;
//                } else if (result < 0) {
//                    challengerWins++;
//                }
//            }
//
//            if (challengerWins > championWins) {
//                System.out.println("Challenger " + challenger.getName() + " wins!");
//                currentChampion = challenger;
//            } else {
//                System.out.println("Champion " + currentChampion.getName() + " retains the title!");
//            }
//        }
//
//        System.out.println("Final champion: " + currentChampion.getName());
//    }
//
//    private static PlayableRockPaperScissors loadPlugin(File file) {
//        try {
//            URL[] urls = {file.toURI().toURL()};
//            URLClassLoader loader = new URLClassLoader(urls);
////            String className = file.getName().replace(".jar", "");
//            String className = "ru.sberSchool.tasks.task7.rockPaperScissors.entity.RandomPlayer";
//            Class<?> clazz = loader.loadClass(className);
//
//            loader.close();
//            return (PlayableRockPaperScissors) clazz.getDeclaredConstructor().newInstance();
//        } catch (Exception e) {
//            System.err.println("Error loading plugin from file: " + file.getName());
//            e.printStackTrace();
//            return null;
//        }
//    }
//}

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task7.rockPaperScissors.enums.RockPaperScissorsEnum;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import static ru.sberSchool.tasks.Constants.PLUGIN_FOLDER;

@Slf4j
public class RockPaperScissorsTournament {

    public static void main(String[] args) {
        RockPaperScissorsTournament tournament = new RockPaperScissorsTournament();
        tournament.runTournament();
    }

    public void runTournament() {
        File folder = new File(PLUGIN_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) {
            log.error("Plugin folder not found: {}", PLUGIN_FOLDER);
            return;
        }

        File[] pluginFiles = folder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (pluginFiles == null || pluginFiles.length == 0) {
            log.error("No plugins found in folder: {}", PLUGIN_FOLDER);
            return;
        }

        PlayableRockPaperScissors currentChampion = loadPlugin(pluginFiles[0]);
        if (currentChampion == null) {
            log.error("Failed to load the first plugin.");
            return;
        }
        log.info("Starting tournament with champion: {}", currentChampion.getName());

        for (int i = 1; i < pluginFiles.length; i++) {
            PlayableRockPaperScissors challenger = loadPlugin(pluginFiles[i]);
            if (challenger == null) {
                log.error("Failed to load plugin: {}", pluginFiles[i].getName());
                continue;
            }

            log.info("Current champion: {} vs Challenger: {}", currentChampion.getName(), challenger.getName());

            int championWins = 0;
            int challengerWins = 0;

            for (int round = 1; round <= 3; round++) {
                RockPaperScissorsEnum championMove = currentChampion.play();
                RockPaperScissorsEnum challengerMove = challenger.play();

                log.info("Round {}: {} plays {}, {} plays {}", round, currentChampion.getName(), championMove,
                        challenger.getName(), challengerMove);

                int result = championMove.compare(challengerMove);
                if (result > 0) {
                    championWins++;
                } else if (result < 0) {
                    challengerWins++;
                }
            }

            if (challengerWins > championWins) {
                log.info("Challenger {} wins!", challenger.getName());
                currentChampion = challenger;
            } else {
                log.info("Champion {} retains the title!", currentChampion.getName());
            }
        }

        log.info("Final champion: {}", currentChampion.getName());
    }

    private static PlayableRockPaperScissors loadPlugin(File file) {
        try {
            URL[] urls = {file.toURI().toURL()};
            URLClassLoader loader = new URLClassLoader(urls);
            String className = "ru.sberSchool.tasks.task7.rockPaperScissors.entity.RandomPlayer";
            Class<?> clazz = loader.loadClass(className);

            loader.close();
            return (PlayableRockPaperScissors) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Error loading plugin from file: {}", file.getName(), e);
            return null;
        }
    }
}


