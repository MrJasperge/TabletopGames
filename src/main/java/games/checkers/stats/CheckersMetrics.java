package games.checkers.stats;

import core.AbstractForwardModel;
import core.AbstractPlayer;
import core.Game;
import evaluation.listeners.MetricsGameListener;
import evaluation.metrics.AbstractMetric;
import evaluation.metrics.Event;
import evaluation.metrics.IMetricsCollection;
import core.actions.AbstractAction;
import core.actions.LogEvent;
import core.components.Edge;
import core.interfaces.IGameEvent;
import evaluation.listeners.MetricsGameListener;
import evaluation.metrics.AbstractMetric;
import evaluation.metrics.Event;
import evaluation.metrics.IMetricsCollection;
import games.checkers.CheckersGameState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CheckersMetrics implements IMetricsCollection {

    public static class CheckersActions extends AbstractMetric {
        Set<String> playerNames;

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(Event.GameEvent.ACTION_TAKEN);
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            this.playerNames = playerNames;
            Map<String, Class<?>> columns = new HashMap<>();
            columns.put("Player", String.class);
            columns.put("PlayerType", String.class);
            columns.put("Action", String.class);
            columns.put("ActionClass", String.class);
            columns.put("ActionDescription", String.class);
            columns.put("ActionSize", Integer.class);

            for (int i = 0; i < nPlayersPerGame; i++) {
                columns.put("PiecesLeft-" + i, Integer.class);
            }
            return columns;
        }

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            Game g = listener.getGame();
            CheckersGameState chgs = (CheckersGameState) g.getGameState();
            AbstractForwardModel fm = g.getForwardModel();
            AbstractAction a = e.action.copy();
            AbstractPlayer currentPlayer = g.getPlayers().get(e.playerID);
            int actionSize = fm.computeAvailableActions(e.state, currentPlayer.getParameters().actionSpace).size();
            int currentPlayerPiecesLeft = chgs.getPieceCount(e.playerID);
            int otherPlayerPiecesLeft = chgs.getPieceCount(1 - e.playerID);

            records.put("Player", e.playerID);
            records.put("PlayerType", currentPlayer.toString());
            records.put("Action", e.action == null ? null : e.action.toString());
            records.put("ActionClass", e.action.getClass().getSimpleName());
            records.put("ActionDescription", e.action == null ? null : e.action.getString(e.state));
            records.put("ActionSize", actionSize);

            records.put("PiecesLeft-" + e.playerID, e.action == null ? null : currentPlayerPiecesLeft);
            records.put("PiecesLeft-" + (1 - e.playerID), e.action == null ? null : otherPlayerPiecesLeft);

            e.action = a;
            return true;
        }
    }
}

