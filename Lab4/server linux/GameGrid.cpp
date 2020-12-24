#include "Header.h"

//! The constructor initializes the 2d grid array
GameGrid::GameGrid() {

	// Init message
	std::cout << "Game grid initiated" << std::endl;

	// Create the 2d grid array
	grid = new bool*[201];

	for (int i = 0; i < 201; i++) {
		grid[i] = new bool[201];
	}

	// Set all positions to 0 (perhaps unnecessary)
	for (int x = 0; x < 201; x++) {
		for (int y = 0; y < 201; y++) {
			grid[x][y] = false;
		}
	}
}

GameGrid::~GameGrid() {

	// Deallocate memory
	for (int i = 0; i < 201; i++) {
		delete[] grid[i];
	}
	delete[] grid;
}

//! Method for checking a move from a client. If a move is illegal, the method returns false, signifying that the player has lost and is out of the game.
bool GameGrid::move(Coordinate xy) {

	// Check out of bounds
	if (xy.x < 0 || xy.y < 0 || xy.x > 200 || xy.y > 200) {
		return false;
	}
	// Check occupied
	else {
		if (grid[xy.x][xy.y] == false) {
			grid[xy.x][xy.y] = true;
			return true;
		}
		return false;
	}
}
