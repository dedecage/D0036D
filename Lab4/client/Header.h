#pragma once
#pragma comment(lib, "Ws2_32.lib")

#include <iostream>
#include <WinSock2.h>
#include <Ws2tcpip.h>
#include "Protocol.h"

#define SCK_version 2 0x0202

//! The client class communicates with the server and performs all necessary actions to play the game according to the provided game protocol
class Client {
private:
	//! The default client header included in all sent messages
	MsgHead myHeader;
	//! The TCP client socket
	SOCKET sock;
	//! Tracks if the client has been given a position
	bool positionSet;
	//! Variable used to store a clients latest coordinate 
	Coordinate position;
	//! Boolean stop flag used to terminate the play loop
	bool stopFlag;
	void play();
	void move(Coordinate c);
	void leave();
public:
	~Client();
	void connectToServer(std::string ipAddress, unsigned int port);
	
};

//! The GuiCommunicator class sends game data to the Java GUI 
class GuiCommunicator {
private:
	//! The UDP socket
	SOCKET guiSocket;
	//! Server hint structure
	sockaddr_in server;
public:
	GuiCommunicator();
	~GuiCommunicator();
	void repaint(unsigned int id, Coordinate pos);
};

//! Datagram is structured according to my lab 3 protocol
struct Datagram {
	byte one = 0;
	byte two = 0;
	byte three = 0;
	byte x;
	byte five = 0;
	byte six = 0;
	byte seven = 0;
	byte y;
	byte nine = 0;
	byte ten = 0;
	byte eleven = 0;
	byte col;
};
