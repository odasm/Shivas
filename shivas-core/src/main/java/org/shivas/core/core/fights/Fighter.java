package org.shivas.core.core.fights;

import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.types.BaseFighterType;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:43
 */
public abstract class Fighter {
    public static Comparator<Fighter> compareBy(final CharacteristicType charac) {
        return new Comparator<Fighter>() {
            public int compare(Fighter o1, Fighter o2) {
                return o1.getStats().get(charac).total() -
                       o2.getStats().get(charac).total();
            }
        };
    }

    protected Fight fight;
    protected FightTurn turn;

    protected FightTeam team;
    protected FightCell currentCell;
    protected OrientationEnum currentOrientation;
    protected boolean dead;

    public abstract Integer getId();
    public abstract boolean isReady();
    public abstract Statistics getStats();

    public Fight getFight() {
        return fight;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
    }

    public FightTurn getTurn() {
        return turn;
    }

    public void setTurn(FightTurn turn) {
        this.turn = turn;
    }

    public FightTeam getTeam() {
        return team;
    }

    public void setTeam(FightTeam team) {
        this.team = team;
    }

    public FightCell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(FightCell currentCell) {
        if (this.currentCell != null) {
            this.currentCell.setCurrentFighter(null);
        }
        this.currentCell = currentCell;
        currentCell.setCurrentFighter(this);
    }

    public void takePlaceOf(Fighter other) {
        if (other == null) throw new IllegalArgumentException("other mustn't be null");

        FightCell old = this.currentCell;

        this.currentCell = other.currentCell;
        this.currentCell.setCurrentFighter(this);

        other.currentCell = old;
        other.currentCell.setCurrentFighter(other);
    }

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(OrientationEnum currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public abstract BaseFighterType toBaseFighterType();
}