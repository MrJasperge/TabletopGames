package games.checkers;

import core.AbstractParameters;
import evaluation.TunableParameters;

import java.util.Arrays;
import java.util.Objects;

public class CheckersGameParameters extends TunableParameters {

    public int gridWidth = 8, gridHeight = 8;
    public String fileName = "rnd-mc-reg.txt";

    public CheckersGameParameters() {
        this(0);
    }

    public CheckersGameParameters(long seed) {
        super(seed);
        addTunableParameter("gridWidth", 8, Arrays.asList(6, 7, 8, 9, 10, 11, 12));
        addTunableParameter("gridHeight", 8, Arrays.asList(6, 7, 8, 9, 10, 11, 12));
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
    public CheckersGame instantiate() {
        return new CheckersGame(this);
    }
}
