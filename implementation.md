# Synopsis

The implementation will take the shape of a client-server model, where each module instantiates a single client object to talk to other modules through the server.
The server will keep track of all the messages sent in a queue, and will deliver messages upon client request.

# Client

The client MUST REGISTER ITS NAME with the server before trying to perform any operations, as the name uniquely identifies the client to other clients.
All operations done without registration will fail and result in immediate termination of the connection.

Currently implemented are the following operations:

 - Register a name: attempts to register a unique name, returns true if successful and false if the name is already claimed. This is implemented along with the
 code to make the connection
 
 - Get a message: Retrieve the oldest message not yet retrieved from the server. Immediately returns null if no messages are available.
 
 - Send a message: Place a message in the mailbox of another client. Returns false if the message could not be send for e.g. a client by that name is not online, 
 or true if the send was successful.
 
 - Get a set of clients: Retrieves a HashSet of currently connected client names.

# Server

All details of server operation are masked from usage; as no one should ever need to create a server, it is not possible. The IMCServer class has its own main method
that should be run as is.

All messages sent to the server result in a response message with some code indicating the result.

# Details of each file

 - IMCServer: a self-contained server class with its own main method. Run as-is before attempting to run any client instances.
 - IMCMessage: defines a message, with public fields that may not be necessary for every type of message. Data to be sent MUST BE SERIALIZABLE.
 - IMCConstants: defines constants for the entire IMC. Currently, only the port number is defined for the usage of both the server and the client. Possible
 other uses for this class are to define names for each individual module.
 - IMCMesType: an enum representing the different types of messages. Names should be self-explanatory.
 - IMCClient: the class that every module should instantiate and use to communicate with other modules.
 - ClientTester: a small dummy module that can be used to play with the features of the IMC. Run multiple instances to see the behavior if there were many modules.
 
# TODO

 - Design decision: what to do when clients go offline?
 - Documentation for applicable classes
 - More classification of data if necessary