#include "Header.h"

// Master socket
int serverSocket;

// Max value needed for select() in linux
int maxSd;

// Values
int connectedClients;
bool stopFlag = false;

// Objects
GameGrid* grid;

// Containers
std::vector<std::pair<int, bool>> playerStates;
std::vector<std::pair<int, int>> clientSockets;

// Default MsgHead
MsgHead responseHead;

int main() {
	Server s;
	s.run(54001, 2);
}

//! Starts the server. The server connects to the given port and waits for maxClients to connect
void Server::run(int port, int maxClients) {
	// Initialize server socket
	serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	maxSd = serverSocket;

	// Investigate socket startup
	if (serverSocket == -1) {
		perror("cannot create socket");
		exit(EXIT_FAILURE);
	}

	// Configure port
	sockaddr_in hint;
	hint.sin_family = AF_INET;
	hint.sin_port = htons(port);
	hint.sin_addr.s_addr = INADDR_ANY;

	// Bind and listen
	bind(serverSocket, (sockaddr*)&hint, sizeof(hint));
	listen(serverSocket, SOMAXCONN);

	// Socket settings
	sockaddr_in client;
	int clientSize = sizeof(client);

	connectedClients = 0;

	// Wait for max clients to enter
	while (true) {

		int clientSocket = accept(serverSocket, NULL, NULL);

		if (0 > clientSocket) {
			perror("accept failed");
			close(clientSocket);
			exit(EXIT_FAILURE);
		}
		else {
			if (clientSocket > maxSd) {
				maxSd = clientSocket;
			}
			connectedClients++;
			responseHead.id++;

			std::pair<int, int> idSocket = std::make_pair(responseHead.id,
				clientSocket);
			clientSockets.push_back(idSocket);

			// Receive join message
			char buffer[256] = { '\0' };
			recv(clientSocket, buffer, sizeof(buffer), NULL);
			JoinMsg jMsg;
			memcpy(&jMsg, buffer, buffer[0]);

			std::cout << jMsg.name << " has connected to the server!"
				<< std::endl;

			// Send response to newly connected client
			responseHead.type = Join;
			send(clientSocket, (char*)&responseHead, sizeof(responseHead), 0);
			responseHead.seq_no++;

			broadcastNewPlayer(&responseHead.id, jMsg.name);

			// Start when lobby is full
			if (connectedClients == maxClients) {
				startGame();
			}

			if (stopFlag) {
				break;
			}

		}
	}
}

//! Starts the game and initializes the game loop. The loop stops when all clients have left
void Server::startGame() {

	// Delay so clients have time to receive NewPlayerPositionMsg
	sleep(2);

	std::cout << "Game Started" << std::endl;

	// Initialize game grid
	grid = new GameGrid();

	// Initialize player states and send NewPlayerPositionMsg
	responseHead.type = Change;

	for (int i = 0; i < connectedClients; i++) {

		// Initialize player positions
		int randX = rand() % 200;
		int randY = rand() % 200;
		Coordinate c;
		c.x = randX;
		c.y = randY;
		grid->move(c);
		std::pair<int, bool> idState = std::make_pair(i + 1, true); // true = alive
		playerStates.push_back(idState);

		// Broadcast
		int id = i + 1;
		Coordinate conv = convertToClient(&c); // Translate coordinates
		broadcastNewPlayerPos(&id, &conv);

	}

	fd_set readSet;
	timeval timeout;

	// Listen to all client input
	while (true) {

		char buffer[512] = { '\0' };

		for (int i = 0; i < clientSockets.size(); i++) {

			int clientSocket = clientSockets.at(i).second;

			FD_ZERO(&readSet);
			FD_SET(clientSocket, &readSet);

			timeout.tv_sec = 0;
			timeout.tv_usec = 0;

			// Select ignores input until activity is found on the socket
			int retVal = select(maxSd + 1, &readSet, NULL, NULL, &timeout);

			if (FD_ISSET(clientSocket, &readSet)) {

				recv(clientSocket, buffer, sizeof(buffer), 0);
				MsgHead head;
				memcpy(&head, buffer, sizeof(head));

				/* Increment sequence number when a new message has been received, client
				 sequence numbers are ignored to avoid asynchrony */
				responseHead.seq_no++;

				switch (head.type) {
				case Event: {
					MoveEvent mEvent;
					memcpy(&mEvent, buffer, sizeof(mEvent));
					int clientID = mEvent.event.head.id;

					// Check if client sending request is alive
					if (checkState(&clientID)) {
						//Manage coordinates
						Coordinate storedMove = mEvent.pos;
						Coordinate cor = convertToGrid(&mEvent.pos);
						// If client makes an illegal move
						bool legalMove = grid->move(cor);
						if (!legalMove) {
							playerLost(&clientID);
							std::cout << "Client " << clientID << " has lost!"
								<< std::endl;
						}
						else {
							broadcastNewPlayerPos(&clientID, &storedMove);
						}
					}
					break;
				}
				case Leave: {
					LeaveMsg lMsg;
					memcpy(&lMsg, buffer, sizeof(lMsg));
					int clientID = lMsg.head.id;
					broadcastPlayerLeft(&clientID);
					removePlayer(&clientID);
					connectedClients--;

					if (connectedClients == 0) {
						shutDown();
					}
					break;
				}
				}
			}
		}

		if (stopFlag) {
			break;
		}
	}

}

//! Broadcasts that a new player has connected
void Server::broadcastNewPlayer(unsigned int* clientID, const char* name) {
	// Create change message
	responseHead.id = *clientID;
	responseHead.type = Change;

	ChangeMsg cMsg;
	cMsg.head = responseHead;
	cMsg.type = NewPlayer;

	NewPlayerMsg npMsg;
	npMsg.msg = cMsg;
	npMsg.desc = Human;
	npMsg.form = Cube;
	strcpy(npMsg.name, name);

	// Send to all clients
	for (int i = 0; i < connectedClients; i++) {
		send(clientSockets.at(i).second, (char*)&npMsg, sizeof(npMsg), 0);
		responseHead.seq_no++;
	}
}

//! Broadcasts new player position 
void Server::broadcastNewPlayerPos(int* clientID, Coordinate* c) {
	// Create change message
	responseHead.id = *clientID;
	responseHead.type = Change;

	ChangeMsg cMsg;
	cMsg.head = responseHead;
	cMsg.type = NewPlayerPosition;

	NewPlayerPositionMsg nppMsg;
	nppMsg.msg = cMsg;
	nppMsg.pos = *c;

	// Send to all clients
	for (int j = 0; j < connectedClients; j++) {
		send(clientSockets.at(j).second, (char*)&nppMsg, sizeof(nppMsg), 0);
		responseHead.seq_no++;
	}
}

//! Broadcasts that a player has left 
void Server::broadcastPlayerLeft(int* clientID) {
	// Create change message
	responseHead.id = *clientID;
	responseHead.type = Change;

	ChangeMsg cMsg;
	cMsg.head = responseHead;
	cMsg.type = PlayerLeave;

	PlayerLeaveMsg plMsg;
	plMsg.msg = cMsg;

	// Send to all clients
	for (int j = 0; j < connectedClients; j++) {
		send(clientSockets.at(j).second, (char*)&plMsg, sizeof(plMsg), 0);
		responseHead.seq_no++;
	}
}

//! Converts GUI coordinate system to -100, 100 coordinate system 
Coordinate Server::convertToClient(Coordinate* c) {
	c->x = c->x - 100;
	c->y = (c->y - 100) * -1;
	return *c;
}

//! Converts -100, 100 coordinate system to GUI coordinate system 
Coordinate Server::convertToGrid(Coordinate* c) {
	c->x = c->x + 100;
	c->y = (c->y * -1) + 100;
	return *c;
}

//! Checks if player with parameter id is active (alive)
bool Server::checkState(int* id) {
	for (int i = 0; i < playerStates.size(); i++) {
		if (playerStates.at(i).first == *id) {
			return playerStates.at(i).second;
		}
	}
	return false;
}

//! Sets player with parameter id as inactive (dead) 
void Server::playerLost(int* id) {
	for (int i = 0; i < playerStates.size(); i++) {
		if (playerStates.at(i).first == *id) {
			playerStates.at(i).second = false;
		}
	}
}

//! Removes a client who has left 
void Server::removePlayer(int* id) {
	for (int i = 0; i < clientSockets.size(); i++) {
		if (clientSockets.at(i).first == *id) {
			int clientSocket = clientSockets.at(i).second;
			close(clientSocket);
			clientSockets.erase(clientSockets.begin() + i);
		}
	}
}

//! Server cleanup when all clients have left 
void Server::shutDown() {
	std::cout << "Server shutting down..." << std::endl;
	close(serverSocket);
	delete (grid);
	stopFlag = true;
}