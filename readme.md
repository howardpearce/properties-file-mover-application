Small Java project consisting of a server and client program.

Server monitors a directory for file changes. Reads in Java Properties files and filters the keys via regex. After filetering, it sends the file to the server and deletes its local copy.

The server receives and reconstructs the filtered files it receives from the client.

You can try this project out by doing the following:
 1. Clone the project onto your machine
 2. Compile the project using Gradle
	- gradlew build client
	- gradlew build server
 3. Run the client and server using the following commands (assuming you're at project root):
	- java -jar build/libs/client.jar config/client.properties
	- java -jar build/libs/server.jar config/server.properties

EDIT: Now supports multiple clients for one server.
