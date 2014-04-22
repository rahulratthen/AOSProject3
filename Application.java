import java.io.*;
	import java.net.InetSocketAddress;
	import java.net.SocketAddress;
	import java.nio.ByteBuffer;
	import java.util.ArrayList;
	import java.util.Date;
	import java.util.Iterator;
	import java.util.LinkedList;
	import java.util.List;
	import java.util.Queue;
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



		/**
		 * constructor
		 */
		public Application()
		{
			
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
			
			/* create server to receive messages*/
			mServer = new SctpServer(app,mConfigReader.getNodeConfig(mSelfNodeID)[0], mConfigReader.getNodeConfig(mSelfNodeID)[1], mConfigReader.getNodeConfig(mSelfNodeID)[2],mConfigReader.getNodeCount() - 1);
			mServerThread = new Thread(mServer);
			mServerThread.start();
					
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

			
			System.exit(0);

		}

	}

