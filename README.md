# Multiclient-java-chat-CLI
a simple multiclient chat created with java for trying out sockets and multithreading

Steps to run the project
-Start a server i.e Server.java
-Manually enter the ip addr of the server machine. If server is in the same machine use 127.0.0.1 as server ip.(Yes, I'm lazy)


Commands:
1. To send text message: @<username> <text message>
2. To get the username of online users: status
3. To send a file to a user: upload @<username> <complete path of the file>(Note: path where the file is to be saved should be entered manually in the code. I'll improve this feature later)
4. To start audio chat with a user: #startaudio <username>
5. To stop audio chat: #stopaudio

This being a fun project for learning purpose, I haven't implemented these commands in the best possible way
