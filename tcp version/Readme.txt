README
Submitted by:
Lakshmi Shanti Duddu
Rahul Ratthen Dhanendran
Viswam Nathan

Compiling Instructions:

Run the script with the following command:

sh project3Script.sh

In the script file modify the first line to the appropriate netID
The script will automatically compile all the java files in the current folder and run the program.
By default, 10 nodes will be run in the background, printing messages on the screen and will eventually terminate with the following message:
"System has taken enough checkpoints. Terminating..."

The ConfigFile.txt can be modified to change the parameters:
The first line indicates the number of nodes
The second line has two numbers: the first one indicates the mean MTT in milliseconds, and the second number indicates the mean ICT in seconds.
The remaining lines indicate the node ID, netmachine name and port number for each of the nodes in the system.
The net machines listed in the Config text file and the script file should match

After each run, the number of forced checkpoints taken by each node is written to the "FinalStats0.txt" file.
