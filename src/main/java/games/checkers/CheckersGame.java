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


    public void experiment() {
        
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

        System.out.print("Executing game: ");

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
