#include "Header.h"

//! Method for connecting to the server
void Client::connectToServer(std::string ipAddress, unsigned int port) {

    // Initialize winsock
    WSADATA wsaData;
    WORD ver = MAKEWORD(2, 2);
    int wsok = WSAStartup(ver, &wsaData);

    // Investigate WSAStartup
    if (wsok != 0) {
        std::cout << "Error! could not start WSA" << std::endl;
        return;
    }

    // Configure address
    sockaddr_in address;
    std::string strAddress = ipAddress;
    address.sin_family = AF_INET;
    address.sin_port = htons(port);
    inet_pton(AF_INET, strAddress.c_str(), &address.sin_addr);

    // Initialize socket
    sock = socket(AF_INET, SOCK_STREAM, 0);

    // Investigate connection
    int connected = SOCKET_ERROR;
    connected = connect(sock, (sockaddr*)&address, sizeof(address));
    if (connected != SOCKET_ERROR) {
        std::cout << "Connection successful" << std::endl;
    }
    else {
        std::cout << "Could not connect to server" << std::endl;
        return;
    }

    // Send join message
    JoinMsg jMsg{
        MsgHead { sizeof jMsg, 0, 0, Join },
        Human,
        Cube,
        "Smerdyakov"
    };
    send(sock, (char*)&jMsg, sizeof(jMsg), 0);

    // Receive server response
    char buffer[256] = { '\0*' };
    recv(sock, buffer, sizeof(buffer), 0);
    MsgHead response;
    memcpy(&response, buffer, buffer[0]);
    myHeader.id = response.id;

    std::cout << "Your player ID is: " << myHeader.id << std::endl;
    stopFlag = false;
    play();
}

//! The play method listens to the server socket and handles client input
void Client::play() {

    GuiCommunicator guiCom;

    fd_set readSet;
    timeval timeout;

    while (true) {

        if (stopFlag) {
            return;
        }

        char buffer[512] = { '\0' };

        FD_ZERO(&readSet);
        FD_SET(sock, &readSet);

        timeout.tv_sec = 0;
        timeout.tv_usec = 0;

        // Select ignores input until activity is found on the socket
        int retVal = select(0, &readSet, NULL, NULL, &timeout);

        if (retVal == SOCKET_ERROR) {
            std::cout << "select() failed with error:" << WSAGetLastError() << std::endl;
            return;
        }

        if (FD_ISSET(sock, &readSet)) {
            recv(sock, buffer, sizeof(buffer), 0);
            MsgHead head;
            memcpy(&head, buffer, sizeof(head));
            switch (head.type) {
            case Change: {
                ChangeMsg eventMessage;
                memcpy(&eventMessage, buffer, sizeof(eventMessage));
                switch (eventMessage.type) {
                case NewPlayer: {
                    NewPlayerMsg npMsg;
                    memcpy(&npMsg, buffer, sizeof(npMsg));
                    std::cout << "Client " << npMsg.name <<
                        " with ID: " << head.id << " has joined the game!" << std::endl;
                    break;
                }
                case NewPlayerPosition: {
                    NewPlayerPositionMsg nppMsg;
                    memcpy(&nppMsg, buffer, sizeof(nppMsg));
                    if (head.id == myHeader.id) {
                        position.x = nppMsg.pos.x;
                        position.y = nppMsg.pos.y;
                        std::cout << "Your new position is X: " << position.x
                            << " Y: " << position.y << std::endl;
                    }
                    else {
                        std::cout << "Client " << head.id << ":s position is X: " <<
                            nppMsg.pos.x << " Y: " << nppMsg.pos.y << std::endl;
                    }
                    guiCom.repaint(head.id, nppMsg.pos);
                    positionSet = true;
                    break;
                }
                case PlayerLeave: {
                    PlayerLeaveMsg plMsg;
                    memcpy(&plMsg, buffer, sizeof(plMsg));
                    std::cout << "Player with ID: " << head.id << " has left the game." << std::endl;
                    break;
                }
                }
            }
            }
        }

        // Check for client input if the game has started
        if (positionSet == true) {
            if (GetAsyncKeyState('W') & 1) {
                position.y++;
                move(position);
            }
            if (GetAsyncKeyState('A') & 1) {
                position.x--;
                move(position);
            }
            if (GetAsyncKeyState('S') & 1) {
                position.y--;
                move(position);
            }
            if (GetAsyncKeyState('D') & 1) {
                position.x++;
                move(position);
            }
            if (GetAsyncKeyState(VK_ESCAPE) & 1) {
                leave();
            }
        }
    }
}

//! The move function sends a MoveEvent to the server socket 
void Client::move(Coordinate c) {

    // Set client head
    myHeader.type = Event;

    // Create EventMsg
    EventMsg eMsg;
    eMsg.head = myHeader;
    eMsg.type = Move;

    // Create MoveEvent
    MoveEvent mEve;
    mEve.event = eMsg;
    mEve.pos = c;

    // Send to server
    send(sock, (char*)&mEve, sizeof(mEve), 0);

}

//! Sends a leave message to the server and shuts the client down
void Client::leave() {
    // Set client head
    myHeader.type = Leave;

    // Create LeaveMsg
    LeaveMsg lMsg;
    lMsg.head = myHeader;

    // Send to server
    send(sock, (char*)&lMsg, sizeof(lMsg), 0);

    Client::~Client();

    stopFlag = true;
}

//! The destructor is used to close the connection gracefully
Client::~Client() {
    WSACleanup();
    closesocket(sock);
    return;
}

//! Main function used to initialize the client
int main() {
    Client c;
    c.connectToServer("127.0.0.1", 5400);
}
