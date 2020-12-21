#include "Header.h"

//! The constructor sets up the UDP socket
GuiCommunicator::GuiCommunicator() {

	// Assign socket
	guiSocket = socket(AF_INET, SOCK_DGRAM, 0);

	// Startup Winsock
	WSADATA data;
	WORD version = MAKEWORD(2, 2);
	int wSok = WSAStartup(version, &data);

	// Investigate Winsock
	if (wSok != 0) {
		std::cout << "Can't start Winsock! " << std::endl;
		return;
	}

	// Set hint structure for the server
	server.sin_family = AF_INET;
	server.sin_port = htons(5300);

	inet_pton(AF_INET, "127.0.0.1", &server.sin_addr);

}

//! The destructor is used to close the connection gracefully
GuiCommunicator::~GuiCommunicator() {
	WSACleanup();
	closesocket(guiSocket);
}

//! Communicates with the Java GUI according to my lab 3 protocol 
void GuiCommunicator::repaint(unsigned int id, Coordinate pos) {
	
	int x = pos.x + 100;
	int y = (pos.y * -1) + 100;
	int color = id;

	// Create datagram
	Datagram dtg;
	dtg.x = x;
	dtg.y = y;
	dtg.col = color;

	int sendOk = sendto(guiSocket, (const char*)&dtg, sizeof(dtg), 0, (sockaddr*)&server, sizeof(server));

	if (sendOk == SOCKET_ERROR) {
		std::cout << "Could not send UDP datagram!" << std::endl;
	}
}