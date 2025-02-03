package games.checkers;

import core.AbstractPlayer;
import core.Game;
import games.GameType;
import players.human.ActionController;
import players.human.HumanGUIPlayer;
import players.mcts.MCTSPlayer;
import players.simple.RandomPlayer;
import utilities.Utils;

import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CheckersGame extends Game {

    public CheckersGame(CheckersGameParameters params) {
        super(GameType.Checkers, new CheckersForwardModel(), new CheckersGameState(params, 2));
    }
    public CheckersGame(List<AbstractPlayer> agents, CheckersGameParameters params) {
        super(GameType.Checkers, agents, new CheckersForwardModel(), new CheckersGameState(params, agents.size()));
    }

    // put your experiments here
    public static void experiment() {


        // Random vs. Random. Play 10.000 times and record the win rate (= win / loss) of starting player
        // Do this 5 times to get 5 values for win rate.
        // Boxplot those 5 items (this will be 1 boxplot with 5 values)

        // 5 experiments, 5 boxplots
        // Each experiment, run 10.000 games and record number of moves

        // 5 experiments, 5 boxplots
        // Each experiment, run 10.000 games and record number of pieces of winning player left on the board

        


    }

    public static void main(String[] args) {
        boolean useGUI = Utils.getArg(args, "gui", false);


        ArrayList<AbstractPlayer> agents = new ArrayList<>();
        ActionController ac = new ActionController();
        agents.add(new RandomPlayer());
        agents.add(new RandomPlayer());


        CheckersGameParameters params = new CheckersGameParameters();
        CheckersFileManager chfm = new CheckersFileManager();

//        System.out.println("CheckersGame: CreateFile");
        chfm.CreateFile(params.fileName);
        chfm.WriteHeaders();

        experiment();

//        for (int i = 0; i < 100; i++) {
//            System.out.print(i + ",");
//            chfm.WriteData(i + ",");
//            if (i % 10 == 9) System.out.println(i+1);
            runOne(GameType.Checkers, null, agents, System.currentTimeMillis() + 1000,
                    false, null, ac, 0);
//            chfm.WriteData("\n");
//        }
//        System.out.print('\n');

//        Game game = new CheckersGame(agents, params);
//        game.run();
    }
}
