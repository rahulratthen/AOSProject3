import java.util.Random;


public class ICTThread implements Runnable {
	
	Application parentThread;
	
	/**
	 * 
	 * @param p
	 */
	public ICTThread(Application p)
	{
		parentThread = p;
	}
	@Override
	public void run() {
		while(true)
		{
			try {
				//replace it with the exponential distribution RV
				Thread.sleep(3000);
				parentThread.ICTExpired = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
