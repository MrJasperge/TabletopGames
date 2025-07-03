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

    // The folder where the input files are stored
    private final String inputFolderName = "experiments/input/boardsetups/";
    public String inputFileName = "classic10x10.txt"; // Default input file

    // The folder where the output files are stored
    private final String outputFolderName = "experiments/output/";
    public String outputFileName = "checkers_output.csv"; // Default output file

    public CheckersGameParameters() {
        this(0);
    }

    public CheckersGameParameters(long seed) {
        addTunableParameter("gridWidth", 10, Arrays.asList(6, 7, 8, 9, 10, 11, 12));
        addTunableParameter("gridHeight", 10, Arrays.asList(6, 7, 8, 9, 10, 11, 12));
        addTunableParameter("inputFileName", inputFileName);
        addTunableParameter("outputFileName", outputFileName);
        _reset();
    }

    public CheckersGameParameters(long seed, String inputFileName, String outputFileName) {
        this(seed);

    }

    public String getInputPath() {
        return inputFolderName + inputFileName;
    }

    public String getOutputPath() {
        return outputFolderName + outputFileName;
    }

    @Override
    public void _reset() {
        gridWidth = (int) getParameterValue("gridWidth");
        gridHeight = (int) getParameterValue("gridHeight");
        this.inputFileName = (String) getParameterValue("inputFileName");
        this.outputFileName = (String) getParameterValue("outputFileName");
    }

    @Override
    protected AbstractParameters _copy() {
        CheckersGameParameters chgp = new CheckersGameParameters(getRandomSeed());
        chgp.gridWidth = gridWidth;
        chgp.gridHeight = gridHeight;
        chgp.inputFileName = inputFileName;
        chgp.outputFileName = outputFileName;
        return chgp;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckersGameParameters that = (CheckersGameParameters) o;
        return gridWidth == that.gridWidth && gridHeight == that.gridHeight
                && inputFileName.equals(that.inputFileName) && outputFileName.equals(that.outputFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridWidth, gridHeight, inputFileName, outputFileName);
    }

    @Override
    public Object instantiate() {
        return GameType.Checkers.createGameInstance(2, this);
    }
}
