import sys
import os
import math
import csv
import numpy as np
import scipy.stats as st
import matplotlib.pyplot as plt
import matplotlib.colors as mc
import colorsys


# read text file and return the data as a list of lists
# each line in the file contains rows and columns of '.', 'x', or 'o'
# '.' represents an empty cell, 'x' represents a piece of player 0
#     and 'o' represents a piece of player 1
def read_file(file_path):
    data = []
    with open(file_path, 'r') as file:
        for line in file:
            row = list(line.strip())
            data.append(row)
    return data

# display the board nicely in a visual format
def display_data(data, output_path, filename='board'):
    fig, ax = plt.subplots()
    cmap = mc.ListedColormap(['white', 'blue', 'red'])
    bounds = [0, 1, 2, 3]
    norm = mc.BoundaryNorm(bounds, cmap.N)

    # Convert the board data to a 2D array for plotting
    board = np.zeros((len(data), len(data[0])), dtype=int)
    for i, row in enumerate(data):
        for j, cell in enumerate(row):
            if cell == 'x':
                board[i, j] = 1
            elif cell == 'o':
                board[i, j] = 2

    # Display the board
    ax.imshow(board, cmap=cmap, norm=norm)
    ax.set_xticks([])
    ax.set_yticks([])
    filename = f"board_{filename}.png"
    fullpath = os.path.join(output_path, filename)
    plt.savefig(fullpath)
    plt.close()


def main():
    if len(sys.argv) < 3:
        print("Usage: python generateplots.py <input_path> <output_path>")
        sys.exit(1)

    input_path = sys.argv[1]
    output_path = sys.argv[2]

    # Ensure the output directory exists
    if not os.path.exists(output_path):
        os.makedirs(output_path)

    files = [f for f in os.listdir(input_path) if f.endswith('.txt')]
    if not files:
        print("No .txt files found in the input directory.")

    for file in files:
        data = read_file(input_path + '/' + file)
        print(f"Processing file: {file}")
        display_data(data, output_path, filename=file[:-4])  # Remove the '.txt' extension for the filename


if __name__ == "__main__":
    main()