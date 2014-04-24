import java.util.Random;


public class MTTThread implements Runnable {
	
	Application parentThread;
	RvGenerator rv;
	
	/**
	 * 
	 * @param p
	 */
	public MTTThread(Application p)
	{
		parentThread = p;
		rv = new RvGenerator();
	}
	@Override
	public void run() {
		while(true)
		{
			
			int test2 = 0;
			
			try {
				//replace it with the exponential distribution RV
				
				
				Thread.sleep((int)rv.expRv(parentThread.mtt)*100);
				//Thread.sleep(20);
				parentThread.MTTExpired = true;
				//System.out.println("MTT expired");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
