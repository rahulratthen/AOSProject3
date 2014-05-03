import java.io.*;
import java.net.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

	import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;

public class Application 
{		
		public static int MESSAGE_SIZE = 128;

		private static int mSelfNodeID = 0;
		private static String mConfigFile = null;
		private static ConfigReader mConfigReader = null; //Class used to read the config text file
		private static SctpServer mServer = null;
		private static Thread mServerThread = null;
		
		private static ArrayList<Integer> clock;
		ArrayList<Integer> checkPoint;
		ArrayList<Boolean> checkPointTaken;
		ArrayList<Boolean> sentTo;
		ArrayList<Integer> minTo;
		ArrayList<Integer> rClock = new ArrayList<Integer> ();
		ArrayList<Integer> rCheckPoint = new ArrayList<Integer> ();
		ArrayList<Boolean> rCheckPointTaken = new ArrayList<Boolean> ();
		int rNodeID;
		int forcedCheckPoint = 0;
		String rcvdMsg=null;
		boolean MTTExpired=false;
		boolean ICTExpired = false;
		boolean requestReceived = false;
		double mtt, ict;
		int independentChkpoints;
		
		

		/**
		 * constructor
		 */
		public Application()
		{
			clock = new ArrayList<Integer>();
			checkPoint = new ArrayList<Integer>();
			checkPointTaken  = new ArrayList<Boolean>();
			sentTo = new ArrayList<Boolean>();
			minTo = new ArrayList<Integer>();
		}
		
		public int getRandomDestination()
		{
			Random r = new Random();
			int n = r.nextInt(mConfigReader.getNodeCount());
			while(n==mSelfNodeID)
				{
					n = r.nextInt(mConfigReader.getNodeCount());
					//System.out.print("r");
				}
			
			return n;
			
		}
		
		public void applicationModule()
		{
			try{
			independentChkpoints = 0;
			while(independentChkpoints<50) //Loop until n requests are satisfied
			{
				//if(mSelfNodeID == 1 && csCount == 20)
					//break;
				//System.out.print(".");
				
				int test = 0;
				if(MTTExpired)
				{
					//System.out.println("MTT TImer expired");
					int test2 = 0;
					try 
					{
						MTTExpired = false;
						prepareMessage(getRandomDestination());
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
				
				if(ICTExpired)
				{
					int test3 = 0;
					independentChkpoints++;
					//System.out.println("ICT TImer expired");
					
					try 
					{
						ICTExpired = false;
						takeCheckPoint();
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
					
				}
				
				//If request for token was received
				if(requestReceived)
				{
					//System.out.println("Request Received");
					
					requestReceived = false;
					receiveMessage(rcvdMsg);
					
				}
				
				

			}
			}
			catch(Exception e)
			{
				try {
					File file  = new File("ErrorLog.txt");
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(mSelfNodeID+"Error: "+e);
					bw.write("\n");
					bw.close();
					fw.close();
				}
				catch(Exception ex)
				{
					
				}
			}
		}

		public void initializeArrays()
		{
			mtt = mConfigReader.getMTT();
			ict = mConfigReader.getICT();
			for(int i = 0; i < mConfigReader.getNodeCount(); i++)
			{
				clock.add(0);
				checkPoint.add(0);
				checkPointTaken.add(false);
				sentTo.add(false);
				minTo.add(Integer.MAX_VALUE);
			}
			try {
				File file  = new File("Checkpoint"+mSelfNodeID+".txt");
				FileWriter fw = new FileWriter(file,false);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.close();
				fw.close();
			}
			catch(Exception e)
			{
				
			}
			takeCheckPoint();
		}
		
		
		
		public void takeCheckPoint() 
		{
			
			System.out.println("CheckPoint taken");
			try {
			
			for(int i = 0; i < mConfigReader.getNodeCount(); i++)
			{
				sentTo.set(i, false);
				minTo.set(i, Integer.MAX_VALUE);
				if(i != mSelfNodeID)
				{
					checkPointTaken.set(i, true);
				}
			}
			
			clock.set(mSelfNodeID, clock.get(mSelfNodeID) + 1);
			
			
			
			checkPoint.set(mSelfNodeID, checkPoint.get(mSelfNodeID) + 1);
			
			
				File file  = new File("Checkpoint"+mSelfNodeID+".txt");
				FileWriter fw = new FileWriter(file,true);
				BufferedWriter bw = new BufferedWriter(fw);
				//bw.write("Independent CheckPoint #"+checkPoint.get(mSelfNodeID)+"\n");
				bw.write("Independent CheckPoint #"+independentChkpoints+"\n");
				bw.write("Forced CheckPoints #"+forcedCheckPoint+"\n");
				bw.write(encodeMessage()+"\n");
				bw.write("\n");
				bw.close();
				fw.close();
			}
			catch(Exception e)
			{
				
			}
			
		}
		
		public void prepareMessage(int nodeID)
		{
			//System.out.println("printMessage called");
			//if(mSelfNodeID == 0) System.out.println("Dest : "+nodeID);
			
			
			sentTo.set(nodeID, true);
			minTo.set(nodeID, Math.min(minTo.get(nodeID), clock.get(mSelfNodeID)));
			sendMessage(encodeMessage(), nodeID); //send encoded msg
		}
		
		public void sendMessage(String message, int destID)
		{
			/*
			System.out.println("Sending msg to "+destID);
			
			SocketAddress mSocketAddress = new InetSocketAddress(mConfigReader.getNodeConfig(destID)[1],Integer.parseInt(mConfigReader.getNodeConfig(destID)[2]));
			MessageInfo mMessageInfo = MessageInfo.createOutgoing(null,0);

			try {

				SctpChannel mSctpChannel = SctpChannel.open();
				System.out.println("1");
				mSctpChannel.connect(mSocketAddress);
				System.out.println("2");
				ByteBuffer mByteBuffer = ByteBuffer.allocate(MESSAGE_SIZE);
				System.out.println("3");
				mByteBuffer.put(message.getBytes());
				System.out.println("4");
				mByteBuffer.flip();
				System.out.println("5");
				mSctpChannel.send(mByteBuffer,mMessageInfo);
				System.out.println("6");
				mSctpChannel.close();
				System.out.println("7");
				//System.out.println("Sending msg to "+ destID);
			} catch (Exception e) {
				System.out.println("Exception: " +  e);

			}
			System.out.println("Sent msg to "+destID);
			*/
			try{
			Socket clientSocket = new Socket(mConfigReader.getNodeConfig(destID)[1],Integer.parseInt(mConfigReader.getNodeConfig(destID)[2]));
			  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			  outToServer.writeBytes(message);
			  clientSocket.close();
			} catch (Exception e) {
				//System.out.println("Exception: " +  e);

			}

		}
		
		public void printFinalStats(int fileNo)
		{
			try {
				File file  = new File("FinalStats"+fileNo+".txt");
				FileWriter fw = new FileWriter(file,true);
				BufferedWriter bw = new BufferedWriter(fw);
				//bw.write("Independent CheckPoint #"+checkPoint.get(mSelfNodeID)+"\n");
				bw.write(Integer.toString(forcedCheckPoint));
				bw.write("\n");
				bw.close();
				fw.close();
			}
			catch(Exception e)
			{
				
			}
		}
		
		public void receiveMessage(String msg)
		{
			
			try{
			rNodeID = getSourceNodeID(msg);
			getReceivedClock(msg);
			getReceivedCheckPoint(msg);
			getReceivedCheckPointTaken(msg);
			
			//System.out.println("Message received " + msg);
			
			for(int k = 0; k < mConfigReader.getNodeCount(); k++)
			{
				
				/*
				 boolean A = sentTo.get(k);
				System.out.println(minTo.get(k) + " , "+ rClock.get(rNodeID));
				boolean B =  minTo.get(k) < rClock.get(rNodeID) ;
				
				boolean D = rClock.get(rNodeID) > Math.max(clock.get(k), rClock.get(k));
				
				
				boolean F = (rCheckPoint.get(mSelfNodeID) == checkPoint.get(mSelfNodeID));
				boolean G =  rCheckPointTaken.get(mSelfNodeID);
				boolean E = F && G;
				boolean C = D||E;
				
			System.out.println(A+"  "+B+"  "+C);
				
				if( A && B && C )
				{
					takeCheckPoint();
					forcedCheckPoint++;
					System.out.println("Forced Checkpoint taken #"+forcedCheckPoint);
					
				}
				 */
				
				//System.out.println("Rclock of node "+rNodeID + " is " + rClock.get(rNodeID) + " Min to of " + k + " is " + minTo.get(k));
				if(  sentTo.get(k) && 
						(rClock.get(rNodeID) > minTo.get(k)) && 
							( ( rClock.get(rNodeID) > Math.max(clock.get(k), rClock.get(k)) ) || ( rCheckPoint.get(mSelfNodeID) == checkPoint.get(mSelfNodeID) ) && ( rCheckPointTaken.get(mSelfNodeID) ) )  )
				{
					takeCheckPoint();
					forcedCheckPoint++;
					System.out.println("Forced Checkpoint taken #"+forcedCheckPoint);
					
				}
			}
			
			clock.set(mSelfNodeID, Math.max(clock.get(mSelfNodeID), rClock.get(rNodeID)));
			for(int k = 0; k < mConfigReader.getNodeCount(); k++)
			{
				if(k != mSelfNodeID)
				{
					clock.set(k, Math.max(clock.get(k), rClock.get(k)));
					if(rCheckPoint.get(k) < checkPoint.get(k))
					{
						//skip
					}
					else if(rCheckPoint.get(k) > checkPoint.get(k))
					{
						checkPoint.set(k, rCheckPoint.get(k));
						checkPointTaken.set(k, rCheckPointTaken.get(k));
					}
					else if(rCheckPoint.get(k) == checkPoint.get(k))
					{
						checkPointTaken.set(k, checkPointTaken.get(k) || rCheckPointTaken.get(k)); 
					}
				}
				
			}
			}
			catch(Exception e)
			{
				try {
					File file  = new File("ErrorLog.txt");
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(mSelfNodeID+"Rcvd msg: "+msg);
					bw.write("\n");
					bw.close();
					fw.close();
				}
				catch(Exception ex)
				{
					
				}
			}
		}
		
		public String encodeMessage()
		{
			String msg = Integer.toString(mSelfNodeID);

			msg += "!";

			for(int i=0; i < clock.size(); i++)
			{
				if(i!=clock.size()-1)
					msg += Integer.toString(clock.get(i)) + ",";
				else
					msg += Integer.toString(clock.get(i));
			}
			
			msg += "!";

			for(int i=0; i < checkPoint.size(); i++)
			{
				if(i!=checkPoint.size()-1)
					msg += Integer.toString(checkPoint.get(i)) + ",";
				else
					msg += Integer.toString(checkPoint.get(i));
			}
			
			msg += "!";

			for(int i=0; i < checkPointTaken.size(); i++)
			{
				if(i!=checkPointTaken.size()-1)
					msg += Boolean.toString(checkPointTaken.get(i)) + ",";
				else
					msg += Boolean.toString(checkPointTaken.get(i));
			}
			

			return msg;
		}
		
		public int getSourceNodeID(String msg)
		{
			
			String[] segments = msg.split("!");
			return Integer.parseInt(segments[0].trim());
			
		}
		
		public void getReceivedClock(String msg)
		{
			try{
			rClock.clear();
			String[] segments = msg.split("!");
			String parts[] = segments[1].split(",");
			
			for(int i=0; i<parts.length; i++)
			{
				rClock.add(Integer.parseInt(parts[i].trim()));
			}
			}
			catch(Exception e)
			{
				try {
					File file  = new File("ErrorLog.txt");
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(mSelfNodeID+"Rcvd msg: "+msg);
					bw.write("\n");
					bw.close();
					fw.close();
				}
				catch(Exception ex)
				{
					
				}
			}
			
		}
		
		public void getReceivedCheckPoint(String msg)
		{
			try{
			rCheckPoint.clear();
			String[] segments = msg.split("!");
			String parts[] = segments[2].split(",");
			
			for(int i=0; i<parts.length; i++)
			{
				rCheckPoint.add(Integer.parseInt(parts[i].trim()));
			}
			}
			catch(Exception e)
			{
				try {
					File file  = new File("ErrorLog.txt");
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(mSelfNodeID+"Rcvd msg: "+msg);
					bw.write("\n");
					bw.close();
					fw.close();
				}
				catch(Exception ex)
				{
					
				}
			}
			
		}
		
		public void getReceivedCheckPointTaken(String msg)
		{
			try{
			rCheckPointTaken.clear();
			String[] segments = msg.split("!");
			String parts[] = segments[3].split(",");
			
			for(int i=0; i<parts.length; i++)
			{
				rCheckPointTaken.add(Boolean.parseBoolean(parts[i].trim()));
			}
			}
			catch(Exception e)
			{
				try {
					File file  = new File("ErrorLog.txt");
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(mSelfNodeID+"Rcvd msg: "+msg);
					bw.write("\n");
					bw.close();
					fw.close();
				}
				catch(Exception ex)
				{
					
				}
			}
			
		}

		/**
		 * Starts the application
		 * @param args
		 */
		public static void main(String[] args) 
		{
			Application app = new Application();

			mSelfNodeID = Integer.parseInt(args[0]);
			mConfigFile = args[1];
			mConfigReader = new ConfigReader(mConfigFile);
			app.initializeArrays();
			
			/* create server to receive messages*/
			mServer = new SctpServer(app,mConfigReader.getNodeConfig(mSelfNodeID)[0], mConfigReader.getNodeConfig(mSelfNodeID)[1], mConfigReader.getNodeConfig(mSelfNodeID)[2],mConfigReader.getNodeCount() - 1);
			mServerThread = new Thread(mServer);
			mServerThread.start();
			
			MTTThread mtt = new MTTThread(app);
			new Thread(mtt).start();
			
			ICTThread ict = new ICTThread(app);
			new Thread(ict).start();
				
			app.applicationModule();
			app.printFinalStats(0);
			/*
			//Create a communication channel to every other node
			for(int i=0; i< mConfigReader.getNodeCount();i++)
			{
				if(i!= mSelfNodeID)
				{
					SocketAddress mSocketAddress = new InetSocketAddress(mConfigReader.getNodeConfig(i)[1],Integer.parseInt(mConfigReader.getNodeConfig(i)[2]));
					MessageInfo mMessageInfo = MessageInfo.createOutgoing(null,0);

					try {

						SctpChannel mSctpChannel = SctpChannel.open();
						mSctpChannel.connect(mSocketAddress);
						ByteBuffer mByteBuffer = ByteBuffer.allocate(MESSAGE_SIZE);
						mByteBuffer.put("test".getBytes());
						mByteBuffer.flip();
						mSctpChannel.send(mByteBuffer,mMessageInfo);
					} catch (Exception e) {
						System.out.println("Exception: " +  e);

					}
				}


			}
*/
			System.out.println("System has taken enough checkpoints. Terminating...");
			System.exit(0);

		}

	}

