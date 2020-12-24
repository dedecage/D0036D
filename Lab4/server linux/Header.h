#pragma once

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <iostream>
#include "Protocol.h"
#include <thread>
#include <vector>
#include <string>

//! The GameGrid class handles the game logic
class GameGrid {

private:
	//! The 2d grid array
	bool** grid;

public:
	GameGrid();
	~GameGrid();
	bool move(Coordinate xy);
};

//! The server class communicates with clients and handle their responses according to the lab 4 instructions
class Server {
private:
	void startGame();
	void broadcastNewPlayer(unsigned int* clientID, const char* name);
	void broadcastNewPlayerPos(int* clientID, Coordinate* c);
	void broadcastPlayerLeft(int* clientID);
	bool checkState(int* id);
	void playerLost(int* id);
	void removePlayer(int* id);
	Coordinate convertToClient(Coordinate* c);
	Coordinate convertToGrid(Coordinate* c);
	void shutDown();
public:
	// Methods
	void run(int port, int maxClients);
};


