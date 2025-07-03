import sys
import os
import math
import csv
import numpy as np
import scipy.stats as st
import matplotlib.pyplot as plt
import matplotlib.colors as mc
import colorsys
import pandas as pd
import tabulate

# credit: https://www.reddit.com/r/learnpython/comments/ila9xp/nice_plots_for_scientific_papers_theses_and/
import scienceplots
# plt.style.use(['science', 'no-latex'])

colors = {
    'OSLA': 'blue',
    'MCTS': 'red',
    'Rand': 'green'
}

# Adjust the colors to be more distinct
# credit: https://stackoverflow.com/questions/37765197/darken-or-lighten-a-color-in-matplotlib
def adjust_lightness(color, amount=0.5):
    try:
        c = mc.cnames[color]
    except:
        c = color
    c = colorsys.rgb_to_hls(*mc.to_rgb(c))
    return colorsys.hls_to_rgb(c[0], max(0, min(1, amount * c[1])), c[2])

def read_csv(file_path):
    data = []
    with open(file_path, 'r') as file:
        reader = csv.reader(file)
        for row in reader:
            data.append(row)
    return data

def plot_first_game(data, output_path):
    print("Plotting the first game...")
    # Assuming the first row is the header
    headers = data[0]

    # Column GameID is one of the headers, this is a single game so make one plot per game
    if 'GameID' not in headers:
        raise ValueError("CSV file must contain 'GameID' column")
    game_id_index = headers.index('GameID')

    game_id = data[1][game_id_index]  # Get the GameID from the first row
    
    # Extract the data for the first game
    # get the value of GameID from the first row
    print(f"Plotting data for GameID: {game_id}")
    data = [row for row in data if row[game_id_index] == game_id]

    # Ensure the data is in the correct format
    if not all(isinstance(row, list) for row in data):
        raise ValueError("CSV data is not in the expected format")
    # Ensure the headers are in the correct format
    if not isinstance(headers, list):
        raise ValueError("CSV headers are not in the expected format")
    

    # x axis is the Turn column
    if 'Turn' not in headers:
        raise ValueError("CSV file must contain 'Turn' column")
    turn_index = headers.index('Turn')
    x = [int(row[turn_index]) for row in data[1:]]

    # Plot two lines, one for each player. PiecesLeft-0 and PiecesLeft-1
    if 'PiecesLeft-0' not in headers or 'PiecesLeft-1' not in headers:
        raise ValueError("CSV file must contain 'PiecesLeft-0' and 'PiecesLeft-1' columns")
    
    # only plot the first game, so only use one GameID value
    y0 = [int(row[headers.index('PiecesLeft-0')]) for row in data[1:] if row[game_id_index] == data[1][game_id_index]]
    y1 = [int(row[headers.index('PiecesLeft-1')]) for row in data[1:] if row[game_id_index] == data[1][game_id_index]]

    playername_0 = data[1][headers.index('PlayerType-0')] if 'PlayerType-0' in headers else 'Player 0'
    playername_1 = data[1][headers.index('PlayerType-1')] if 'PlayerType-1' in headers else 'Player 1'
    
    plt.figure(figsize=(10, 5))
    plt.plot(x, y0, label=playername_0, color='blue')
    plt.plot(x, y1, label=playername_1, color='orange')
    plt.title('Pieces Left Over Time')
    plt.xlabel('Turn')
    plt.ylabel('Pieces Left')
    plt.legend()
    plt.grid()
    # Save plot with GameID in the file name
    game_id = data[1][game_id_index]
    filename = f"game_{game_id}_pieces_left.png"
    full_path = output_path + filename
    plt.savefig(full_path)
    plt.close()

def get_all_games_data(data):
    # plot the mean of all games
    print("Plotting the mean of all games...")
    headers = data[0]

    if 'GameID' not in headers:
        raise ValueError("CSV file must contain 'GameID' column")
    game_id_index = headers.index('GameID')

    if 'Turn' not in headers:
        raise ValueError("CSV file must contain 'Turn' column")
    turn_index = headers.index('Turn')

    if 'PiecesLeft-0' not in headers or 'PiecesLeft-1' not in headers or 'PlayerType-0' not in headers or 'PlayerType-1' not in headers:
        raise ValueError("CSV file must contain 'PiecesLeft-0', 'PiecesLeft-1', 'PlayerType-0' and 'PlayerType-1' columns")
    
    # Get unique Player Types from data
    player_types = set(row[headers.index('PlayerType-0')] for row in data[1:])

    # this is the data we want to record:
    # player_types: {OSLA, MCTS, Random} these are the lines in the plot
    # Actually, we want only two players playing each other, 
    #   we don't want to plot OLSA vs Random and OSLA vs MCTS
    #   because OLSA might perform differently against different players
    #   we want seperate plots for each player type against each other
    #   so first, get all unique player combinations
    #   then, for each combination, get all games where those two players played against each other
    #   this can be done by checking the PlayerType-0 and PlayerType-1 columns and filtering on GameID

    player_combinations = set()

    matchup_data = {}

    for row in data[1:]:
        player_0 = row[headers.index('PlayerType-0')]
        player_1 = row[headers.index('PlayerType-1')]
        player_combinations.add(tuple((player_0, player_1)))

    print(f"Found {len(player_combinations)} unique player combinations: {player_combinations}")

    # Now, for each player combination, we will plot the mean of all games in a separate plot
    for player_pair in player_combinations:
        player_0, player_1 = player_pair

        # Filter data for the current player pair
        filtered_data = [row for row in data[1:] if row[headers.index('PlayerType-0')] == player_0 and row[headers.index('PlayerType-1')] == player_1]
    
        if not filtered_data:
            print(f"No data found for player combination: {player_0} vs {player_1}")
            continue

        # Plot the mean for the current player pair
        
        # get every game by GameID
        game_ids = set(row[game_id_index] for row in filtered_data)

        game_turns = {}

        # for each game, get the turns and pieces left for both players
        for game_id in game_ids:
            game_data = [row for row in filtered_data if row[game_id_index] == game_id]
            if not game_data:
                continue
            
            # x axis is the Turn column
            x = [int(row[turn_index]) for row in game_data]

            # y axis is the PiecesLeft-0 and PiecesLeft-1 columns
            y0 = [int(row[headers.index('PiecesLeft-0')]) for row in game_data]
            y1 = [int(row[headers.index('PiecesLeft-1')]) for row in game_data]

            # save this data for later averaging
            game_turns[game_id] = {
                'y0': y0,
                'y1': y1
            }
        
        # Now we have all the game data for the current player pair, we can calculate the mean
        if not game_turns:
            print(f"No game data found for player combination: {player_0} vs {player_1}")
            continue
        else:
            print(f"game_turns for {player_0} vs {player_1}: {len(game_turns)} games found")
        
        matchup_data[player_pair] = game_turns

    return matchup_data

def plot_mean_games(matchup_data, resolution, output_path):
    # Calculate the mean for each turn
    for player_pair, game_turns in matchup_data.items():
        player_0, player_1 = player_pair

        player_name_0 = player_0[0:4]
        player_name_1 = player_1[0:4]
        p_0_lightness = 1
        p_1_lightness = 1
        if player_name_0 == player_name_1:
            p_0_lightness = 1.5
            p_1_lightness = 0.5

        new_y0s = []
        new_y1s = []

        # first normalize the turns to game percentage
        for game_id, turns in game_turns.items():
            stepSize = (len(turns['y0']) - 1) / (resolution - 1)
            new_y0 = []
            new_y1 = []

            for i in range(resolution - 1):
                index = i * stepSize
                low = math.floor(index)
                high = low + 1
                fraction = max(0, index - low)

                # Interpolate the value
                new_y0.append(turns['y0'][low] * (1 - fraction) + turns['y0'][high] * fraction)
                new_y1.append(turns['y1'][low] * (1 - fraction) + turns['y1'][high] * fraction)

            new_y0.append(turns['y0'][-1])  # Append the last value
            new_y0s.append(new_y0)
            new_y1.append(turns['y1'][-1])  # Append the last value
            new_y1s.append(new_y1)

        # Now we have the mean for each turn, we can plot it
        plt.figure(figsize=(10, 6))

        # Uncomment the following lines to plot all individual games
        # for new_y0 in new_y0s:
        #     plt.plot(new_y0, color='blue', alpha=0.1)
        # for new_y1 in new_y1s:
        #     plt.plot(new_y1, color='orange', alpha=0.1)
        
        # Calculate the mean for each turn
        mean_y0 = np.mean(new_y0s, axis=0)
        mean_y1 = np.mean(new_y1s, axis=0)

        # Calculate the confidence intervals and plot them shaded
        conf_y0 = st.t.interval(0.95, len(new_y0s)-1, loc=np.mean(new_y0s, axis=0), scale=st.sem(new_y0s, axis=0))
        conf_y1 = st.t.interval(0.95, len(new_y1s)-1, loc=np.mean(new_y1s, axis=0), scale=st.sem(new_y1s, axis=0))
        plt.fill_between(range(len(mean_y0)), conf_y0[0], conf_y0[1], color=adjust_lightness(colors[player_name_0], p_0_lightness), alpha=0.1)
        plt.fill_between(range(len(mean_y1)), conf_y1[0], conf_y1[1], color=adjust_lightness(colors[player_name_1], p_1_lightness), alpha=0.1)

        plt.plot(mean_y0, label='Player 1: ' + player_0, color=adjust_lightness(colors[player_name_0], p_0_lightness), linewidth=2)
        plt.plot(mean_y1, label='Player 2: ' + player_1, color=adjust_lightness(colors[player_name_1], p_1_lightness), linewidth=2)
        # plot the standard deviation as a shaded area
        # std_y0 = np.std(new_y0s, axis=0)
        # std_y1 = np.std(new_y1s, axis=0)
        # plt.fill_between(range(len(mean_y0)), mean_y0 - std_y0, mean_y0 + std_y0, color=adjust_lightness(colors[player_name_0], p_0_lightness), alpha=0.1)
        # plt.fill_between(range(len(mean_y1)), mean_y1 - std_y1, mean_y1 + std_y1, color=adjust_lightness(colors[player_name_1], p_1_lightness), alpha=0.1)
        plt.xlabel("Game Percentage")
        plt.ylabel("Pieces Left")
        plt.title(f"Average Number of Pieces Left: {player_0} vs {player_1}")
        plt.legend()
        plt.grid()
        plt.savefig(f"{output_path}/mean_{player_0}_vs_{player_1}.png")
        plt.close()

def plot_all_games(matchup_data, output_path):
    for player_pair, game_turns in matchup_data.items():
        player_0, player_1 = player_pair

        plt.figure(figsize=(10, 6))

        # Plot each game
        for game_id, turns in game_turns.items():
            plt.plot(turns['y0'], label=f"{player_0} (Game {game_id})", color='blue', alpha=0.5)
            plt.plot(turns['y1'], label=f"{player_1} (Game {game_id})", color='orange', alpha=0.5)
        plt.xlabel("Turn")
        plt.ylabel("Pieces Left")
        plt.title(f"Pieces Left: {player_0} vs {player_1}")
        plt.legend()
        plt.grid()
        plt.savefig(f"{output_path}/all_{player_0}_vs_{player_1}.png")
        plt.close()
    
def plot_tables(matchup_data, output_path):
    # Create a 3x3 table for game results
    # horizontal header: MCTS, OSLA, Random
    # vertical header: MCTS, OSLA, Random
    # each cell contains the number of games won, lost and drawn between the two players
    
    # game result data table
    table_game_data = []
    # game duration data table
    table_duration_data = []
    # pieces left data table
    table_pieces_left_data = []

    for player_0 in ['MCTS', 'OSLA', 'Rand']:
        for player_1 in ['MCTS', 'OSLA', 'Rand']:
            table_game_data.append([player_0, player_1, 0, 0, 0, 0])  # Wins player 0, Wins player 1, Draws, Total Games
            table_duration_data.append([player_0, player_1, 0, 0, 0, 0])  # Average Duration Player 0, Average Duration Player 1, Average Duration Draws, Total Duration
            table_pieces_left_data.append([player_0, player_1, 0, 0, 0, 0])  # Total Pieces Left Player 0, Total Pieces Left Player 1, Average Pieces Left Draws, Average Total Pieces Left
    # Count the number of games won, lost and drawn between the two players


    for player_pair, game_turns in matchup_data.items():
        player_0, player_1 = player_pair

        # find the row in the table_data that matches the player pair
        player_0 = player_0[0:4]  # Get the first 4 characters of the player name
        player_1 = player_1[0:4]  # Get the first 4 characters of the player name

        # get table row for player_0 vs player_1
        index = next((i for i, row in enumerate(table_game_data) if row[0] == player_0 and row[1] == player_1), None)

        if index is None:
            print(f"Could not find table row for player pair: {player_0} vs {player_1}")
            continue

        for game_id, turns in game_turns.items():
            # turns is in the format {'y0': [pieces_left_0], 'y1': [pieces_left_1]}
            y0 = turns['y0'][-1]  # Last value for Player 0
            y1 = turns['y1'][-1]  # Last value for Player 1

            if y0 == 0:
                # Player 1 won
                table_game_data[index][3] += 1  # Increment wins for Player 0
            elif y1 == 0:
                # Player 0 won
                table_game_data[index][2] += 1  # Increment wins for Player 1
            else:
                # Draw
                table_game_data[index][4] += 1   # Increment draws
            # Increment total games
            table_game_data[index][5] += 1
    
    table_game_data.append(['Total', '', 0, 0, 0, 0])  # Add a total row
    # Calculate totals
    for row in table_game_data[:-1]:  # Exclude the total row
        row[2] = int(row[2])  # Wins Player 1
        row[3] = int(row[3])  # Wins Player 2
        row[4] = int(row[4])  # Draws
        row[5] = int(row[5])  # Total Games
        table_game_data[-1][2] += row[2]  # Total Wins Player 1
        table_game_data[-1][3] += row[3]  # Total Wins Player 2
        table_game_data[-1][4] += row[4]  # Total Draws
        table_game_data[-1][5] += row[5]  # Total Games

    # Display percentages in the table
    for row in table_game_data[:-1]:  # Exclude the total row
        if row[5] > 0:
            row[2] = f"{row[2]} ({row[2] / row[5] * 100:.1f}%)"
            row[3] = f"{row[3]} ({row[3] / row[5] * 100:.1f}%)"
            row[4] = f"{row[4]} ({row[4] / row[5] * 100:.1f}%)"
    # Add the total row percentages
    if table_game_data[-1][5] > 0:
        table_game_data[-1][2] = f"{table_game_data[-1][2]} ({table_game_data[-1][2] / table_game_data[-1][5] * 100:.1f}%)"
        table_game_data[-1][3] = f"{table_game_data[-1][3]} ({table_game_data[-1][3] / table_game_data[-1][5] * 100:.1f}%)"
        table_game_data[-1][4] = f"{table_game_data[-1][4]} ({table_game_data[-1][4] / table_game_data[-1][5] * 100:.1f}%)"
    
    # Convert the table data to a DataFrame for better formatting
    df = pd.DataFrame(table_game_data, columns=['Player 1', 'Player 2', 'Wins Player 1', 'Wins Player 2', 'Draws', 'Total Games'])

    df.to_csv(f"{output_path}/table_{player_0}_vs_{player_1}.csv", index=False)

    # Plot the table
    fig, ax = plt.subplots(figsize=(8, 6))
    ax.axis('tight')
    ax.axis('off')
    table = ax.table(cellText=df.values, colLabels=df.columns, cellLoc='center', loc='center')
    table.auto_set_font_size(False)
    table.set_fontsize(12)
    table.scale(1.2, 1.2)
    plt.title("Game Results Table")
    plt.show()
    plt.close()

def main():
    input_path = './results/CheckersActions.csv'  # Default input path
    output_path = './output/plots/'  # Default output path
    resolution = 101  # Default resolution for interpolation
    # Check if the script is run with command line arguments
    
    # get command line arguments for input and output paths
    if len(sys.argv) <= 2:
        print("Usage: python3 generateplots.py [mean|first] [input_path] [output_path]\n")
    if len(sys.argv) > 2:
        input_path = sys.argv[2]
        print(f"Using input path: {input_path}")
    else:
        print(f"No input path provided, using default: {input_path}")
    if len(sys.argv) > 3:
        output_path = sys.argv[3]
        print(f"Using output path: {output_path}")
    else:
        print(f"No output path provided, using default: {output_path}")

    # Ensure the output directory exists
    if not os.path.exists(output_path):
        os.makedirs(output_path)
        print(f"Created output directory: {output_path}")

    # Read the CSV file
    if not os.path.exists(input_path):
        print(f"Input file does not exist: {input_path}")
        return
    
    data = read_csv(input_path)
    if not data:
        print("No data found in the CSV file.")
        return
    
    print("\n")

    matchup_data = get_all_games_data(data)

    # get command line arguments for functionality
    if len(sys.argv) > 1:
        if sys.argv[1] == 'mean':
            print("Plotting all games mean...")
            plot_mean_games(matchup_data, resolution, output_path)
        elif sys.argv[1] == 'tables':
            print("Plotting all games tables...")
            plot_tables(matchup_data, output_path)
        elif sys.argv[1] == 'all':
            print("Plotting all games mean and tables")
            plot_mean_games(matchup_data, resolution, output_path)
            plot_tables(matchup_data, output_path)
        elif sys.argv[1] == 'first':
            print("Plotting the first game...")
            plot_first_game(data, output_path)
        else:
            print(f"Unknown command line argument: {sys.argv[1]}")
            return
    else:
        print("No command line argument provided, plotting the first game by default...")
        plot_first_game(data, output_path)


if __name__ == "__main__":
    main()
    print("Plot generation completed successfully.")