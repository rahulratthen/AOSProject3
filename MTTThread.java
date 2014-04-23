import java.util.Random;


public class MTTThread implements Runnable {
	
	Application parentThread;
	
	/**
	 * 
	 * @param p
	 */
	public MTTThread(Application p)
	{
		parentThread = p;
	}
	@Override
	public void run() {
		while(true)
		{
			try {
				//replace it with the exponential distribution RV
				Thread.sleep(1000);
				parentThread.MTTExpired = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
