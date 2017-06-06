package dungeon;

import dungeon.character.DungeonCharacter;
import dungeon.character.Hero;
import dungeon.character.Monster;
import dungeon.character.action.BattleAction;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Alex Plagman on 5/22/2017.
 */
public class Round implements Serializable {
    private final Battle battle;
    private final Stack<Hero> heroesPlayed;
    private final Deque<Hero> heroesRemaining;
    private int numberOfMonsterActions;

    Round(Battle battle) {
        if (battle == null) {
            throw new IllegalArgumentException("Null battle!");
        }
        this.battle = battle;
        heroesPlayed = new Stack<>();
        heroesRemaining = new LinkedList<>(battle.getLivingHeroes());
        numberOfMonsterActions = 0;
    }

    Result play() {
        if (heroesPlayed.isEmpty()) {
            System.out.println(System.lineSeparator() + "New round begins!" + System.lineSeparator() + "----------------------------------");
            calculateNumberOfPlayerTurns();
        }
        Result result = performPlayerTurns();
        if (result == Result.NORMAL) {
            result = performMonsterTurns();
            return result;
        }
        return result;
    }

    private void calculateNumberOfPlayerTurns() {
        battle.getLivingHeroes().forEach((hero) -> {
            Optional<Monster> fastestMonster = battle.getLivingMonsters().stream().max(Comparator.comparing(DungeonCharacter::getAttackSpeed));
            int numTurns;
            numTurns = fastestMonster.map(monster -> Math.max(1, hero.getAttackSpeed() / monster.getAttackSpeed())).orElse(1);

            for (int i = 0; i < numTurns; ++i) {
                heroesRemaining.addLast(hero);
            }
        });
    }

    private Result performPlayerTurns() {
        if (battle.getLivingMonsters().isEmpty() || battle.getLivingHeroes().isEmpty()) {
            return Result.FINISHED;
        }
        while (!heroesRemaining.isEmpty()) {
            Hero currentHero = heroesRemaining.peekFirst();
            if (battle.getLivingMonsters().isEmpty()) {
                return Result.FINISHED;
            }
            System.out.println(currentHero.getName() + "'s turn:");
            Optional<BattleAction<Hero>> action = currentHero.promptForAction();
            if (action.isPresent()) {
                battle.logAction(action.get().perform(this, currentHero, selectTargets(action.get(), true)));
                heroesRemaining.removeFirst();
                heroesPlayed.push(currentHero);
            } else {
                if (heroesPlayed.isEmpty()) {
                    return Result.UNDONE;
                } else {
                    battle.undoLastAction();
                    heroesRemaining.addFirst(heroesPlayed.pop());
                }
            }
            System.out.println();
        }

        if (battle.getLivingMonsters().isEmpty()) {
            return Result.FINISHED;
        } else {
            return Result.NORMAL;
        }
    }

    private Result performMonsterTurns() {
        battle.getLivingMonsters().forEach((monster) -> {
            if (battle.getLivingHeroes().isEmpty()) {
                return;
            }

            // This call should always succeed (the Monster constructor ensures they have at least one action)
            monster.getRandomAction().ifPresent((action) -> {
                battle.logAction(action.perform(this, monster, selectTargets(action, false)));
                ++numberOfMonsterActions;
                System.out.println();
            });
        });

        if (battle.getLivingHeroes().isEmpty()) {
            return Result.FINISHED;
        }
        return Result.NORMAL;
    }

    private Collection<DungeonCharacter> selectTargets(BattleAction action, boolean playerControlled) {
        Collection<DungeonCharacter> targets = new ArrayList<>();
        switch (action.getTargetType()) {
            case ALL:
                if (playerControlled) {
                    targets.addAll(battle.getLivingMonsters());
                } else {
                    targets.addAll(battle.getLivingHeroes());
                }
            case SELF:
                return targets;
            case SINGULAR:
            case MULTIPLE:
                for (int i = 0; i < action.getTargetType().getCount(); ++i) {
                    // These calls should always succeed (each hero/monster turn ensures there are living heroes left)
                    if (playerControlled) {
                        battle.promptForMonster().ifPresent(targets::add);
                    } else {
                        battle.getRandomLivingHero().ifPresent(targets::add);
                    }
                }
            default:
                return targets;
        }
    }

    public void addTurn() {
        if (!heroesRemaining.isEmpty()) {
            heroesRemaining.addLast(heroesRemaining.peekFirst());
        }
    }

    void undo() {
        for (int i = 0; i < numberOfMonsterActions; ++i) {
            battle.undoLastAction();
        }
        numberOfMonsterActions = 0;
        assert !heroesPlayed.isEmpty();
        assert heroesRemaining.isEmpty();
        heroesRemaining.addFirst(heroesPlayed.pop());
    }

    enum Result {
        NORMAL, FINISHED, UNDONE
    }
}
