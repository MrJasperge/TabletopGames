import csv
import matplotlib.pyplot as plt
import sys
import os

def read_csv(file_path):
    print("Reading CSV file...")
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

def plot_all_games_mean(data, output_path):
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

    # Create a dictionary to hold mean pieces left for each player type
    mean_pieces_left = {player_type: {'turns': [], 'pieces_left': []} for player_type in player_types}

    # Plot the mean pieces left for each player
    plt.figure(figsize=(10, 5))

    # TODO: fix this

    for game_id in set(row[game_id_index] for row in data[1:]):
        # Filter data for the current game ID
        game_data = [row for row in data[1:] if row[game_id_index] == game_id]

        # Initialize lists to hold pieces left for each player
        pieces_left_0 = []
        pieces_left_1 = []
        turns = []

        for row in game_data:
            turns.append(int(row[turn_index]))
            pieces_left_0.append(int(row[headers.index('PiecesLeft-0')]))
            pieces_left_1.append(int(row[headers.index('PiecesLeft-1')]))

        # Calculate mean pieces left for each turn
        mean_pieces_left_0 = [sum(pieces_left_0) / len(pieces_left_0)] * len(turns)
        mean_pieces_left_1 = [sum(pieces_left_1) / len(pieces_left_1)] * len(turns)


        # Get player names
        playername_0 = game_data[0][headers.index('PlayerType-0')]
        playername_1 = game_data[0][headers.index('PlayerType-1')]

        print(f"Processing GameID: {game_id} for players {playername_0} and {playername_1}")
        for turn, pieces_left_0_val, pieces_left_1_val in zip(turns, mean_pieces_left_0, mean_pieces_left_1):
            print(f"Turn: {turn}, {playername_0}: {pieces_left_0_val}, {playername_1}: {pieces_left_1_val}")
        
        print(f"Mean Pieces Left for {playername_0}: {mean_pieces_left_0}")
        print(f"Mean Pieces Left for {playername_1}: {mean_pieces_left_1}")
        
        # put the mean pieces left in the dictionary
        mean_pieces_left[playername_0]['turns'].extend(turns)
        mean_pieces_left[playername_0]['pieces_left'].extend(mean_pieces_left_0)
        mean_pieces_left[playername_1]['turns'].extend(turns)
        mean_pieces_left[playername_1]['pieces_left'].extend(mean_pieces_left_1)

    # Plot the mean pieces left for each player type
    for player_type, values in mean_pieces_left.items():
        plt.plot(values['turns'], values['pieces_left'], label=player_type)
    

    plt.title('Mean Pieces Left Over Time Across All Games')
    plt.xlabel('Turn')
    plt.ylabel('Mean Pieces Left')
    plt.legend()
    plt.grid()
    # Save the plot
    filename = "mean_pieces_left_all_games.png"
    full_path = output_path + filename
    plt.savefig(full_path)
    plt.close()
    

def main():
    print("Starting the plot generation script...")
    input_path = './results/CheckersActions.csv'  # Default input path
    output_path = './output/plots/'  # Default output path
    # Check if the script is run with command line arguments
    
    # get command line arguments for input and output paths
    if len(sys.argv) > 2:
        input_path = sys.argv[2]
        print(f"Using input path: {input_path}")
    else:
        print("No input path provided, using default: ./results/CheckersActions.csv")
    if len(sys.argv) > 3:
        output_path = sys.argv[3]
        print(f"Using output path: {output_path}")
    else:
        print("No output path provided, using default: ./output/plots/")
    
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
    

    # get command line arguments for functionality
    if len(sys.argv) > 1:
        if sys.argv[1] == 'mean':
            print("Plotting all games mean...")
            plot_all_games_mean(data, output_path)
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