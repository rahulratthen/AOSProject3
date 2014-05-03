
import java.io.*;
import java.net.*;
import java.nio.*;
import com.sun.nio.sctp.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.Date;

public class SctpServer implements Runnable {
	public static int MESSAGE_SIZE = 128;

	private String mAddress;
	private String mPort;
	private String mSelfNodeID;
	private int mNeighbourCount;
	Application parentThread;

	/**
	 * Starts the Sctp Server
	 * @param p
	 * @param mSelfNodeID
	 * @param mAddress
	 * @param mPort
	 * @param mNeighbourCount
	 */
	public SctpServer(Application p, String mSelfNodeID, String mAddress, String mPort, int mNeighbourCount) {
		super();
		parentThread = p;
		this.mAddress = mAddress;
		this.mPort = mPort;
		this.mNeighbourCount = mNeighbourCount;
		this.mSelfNodeID = mSelfNodeID;
	}

	@Override
	public void run() {
		int mMessageCount = 0;
		/*
		SocketAddress mServerAddress = new InetSocketAddress(mAddress, Integer.parseInt(mPort));


		try {
			SctpServerChannel mServerChannel = SctpServerChannel.open();

			mServerChannel.bind(mServerAddress);
			
			//Loop infinitely listening for messages from other nodes
			while(true){
				ByteBuffer mBuffer = ByteBuffer.allocate(MESSAGE_SIZE);
				String mMessage;
				String mMessageParts[];
				System.out.print("l");
				SctpChannel mClientChannel = mServerChannel.accept();

				mClientChannel.receive(mBuffer,null,null);

				mMessage = bufferToString(mBuffer);
				
				if(mMessage != null)
				{
					if(mMessage.length() > 5 && !mMessage.equals(""))
					{
						parentThread.rcvdMsg = mMessage;
						parentThread.requestReceived = true;
						
					}
				}
				
								
				mBuffer.flip();
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		try{
		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(mPort));

        while(true)
        {
        	String mMessage;
           Socket connectionSocket = welcomeSocket.accept();
           BufferedReader inFromClient =
              new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
           mMessage = inFromClient.readLine();
           if(mMessage != null)
			{
				if(mMessage.length() > 5 && !mMessage.equals(""))
				{
					parentThread.rcvdMsg = mMessage;
					parentThread.requestReceived = true;
					
				}
			}
           
        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Converts contents of a buffer to string to send it as SCTP message
	 * @param mBuffer
	 * @return
	 */
	private String bufferToString(ByteBuffer mBuffer) {
		mBuffer.position(0);
		mBuffer.limit(MESSAGE_SIZE);
		byte[] mBufArr = new byte[mBuffer.remaining()];
		mBuffer.get(mBufArr);
		return new String(mBufArr);
	}
}

