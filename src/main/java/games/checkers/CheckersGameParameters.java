package games.checkers;

import core.AbstractParameters;
import core.Game;
import evaluation.optimisation.TunableParameters;
import games.GameType;

import java.util.Arrays;
import java.util.Objects;

public class CheckersGameParameters extends TunableParameters {

    public int gridWidth = 10;
    public int gridHeight = 10;

    public String fileName = "F:\\Unity Projects\\TabletopGames\\experiments\\test6.csv";
    public String inputFileName = "F:\\Unity Projects\\TabletopGames\\experiments\\input\\boardsetups\\classic.txt";

    public CheckersGameParameters() {
        this(0);
    }

    public CheckersGameParameters(long seed) {
        addTunableParameter("gridWidth", 10, Arrays.asList(6, 7, 8, 9, 10, 11, 12));
        addTunableParameter("gridHeight", 10, Arrays.asList(6, 7, 8, 9, 10, 11, 12));
        addTunableParameter("fileName", fileName);
        _reset();
    }

    @Override
    public void _reset() {
        gridWidth = (int) getParameterValue("gridWidth");
        gridHeight = (int) getParameterValue("gridHeight");
        fileName = (String) getParameterValue("fileName");
    }

    @Override
    protected AbstractParameters _copy() {
        CheckersGameParameters chgp = new CheckersGameParameters(getRandomSeed());
        chgp.gridWidth = gridWidth;
        chgp.gridHeight = gridHeight;
        chgp.fileName = fileName;
        return chgp;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckersGameParameters that = (CheckersGameParameters) o;
        return gridWidth == that.gridWidth && gridHeight == that.gridHeight && fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridWidth, gridHeight, fileName);
    }

    @Override
    public Object instantiate() {
        return GameType.Checkers.createGameInstance(2, this);
    }
}
