#pragma once
#pragma comment (lib, "ws2_32.lib")

#include <iostream>
#include <WS2tcpip.h>
#include <WinSock2.h>
#include <thread>
#include "Protocol.h"
#include <vector>
#include <string>
#include <stdlib.h>     /* srand, rand */

#define SCK_version 2 0x0202

class GameGrid {

private:
	bool** grid;

public:
	GameGrid();
	~GameGrid();
	bool move(Coordinate xy);
};

class Server {
public:
	// Methods
	void run(int port, int maxClients);
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

};