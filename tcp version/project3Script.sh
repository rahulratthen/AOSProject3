remoteuser=lxd121230
remotecomputer0=net15.utdallas.edu
remotecomputer1=net01.utdallas.edu
remotecomputer2=net02.utdallas.edu
remotecomputer3=net03.utdallas.edu
remotecomputer4=net04.utdallas.edu
remotecomputer5=net05.utdallas.edu
remotecomputer6=net06.utdallas.edu
remotecomputer7=net07.utdallas.edu
remotecomputer8=net08.utdallas.edu
remotecomputer9=net09.utdallas.edu

rm ErrorLog.txt

rm FinalStats0.txt

javac *.java

ssh -l "$remoteuser" "$remotecomputer0" "cd $HOME/AOSProject3;java Application 0 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer1" "cd $HOME/AOSProject3;java Application 1 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer2" "cd $HOME/AOSProject3;java Application 2 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer3" "cd $HOME/AOSProject3;java Application 3 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer4" "cd $HOME/AOSProject3;java Application 4 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer5" "cd $HOME/AOSProject3;java Application 5 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer6" "cd $HOME/AOSProject3;java Application 6 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer7" "cd $HOME/AOSProject3;java Application 7 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer8" "cd $HOME/AOSProject3;java Application 8 ConfigFile.txt" &
ssh -l "$remoteuser" "$remotecomputer9" "cd $HOME/AOSProject3;java Application 9 ConfigFile.txt" &
