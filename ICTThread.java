import java.util.Random;


public class ICTThread implements Runnable {
	
	Application parentThread;
	RvGenerator rv;
	double rangeMin, rangeMax, sleepTime;
	/**
	 * 
	 * @param p
	 */
	public ICTThread(Application p)
	{
		parentThread = p;
		rangeMin = (double)0.5*parentThread.ict;
		rangeMax = (double)1.5*parentThread.ict;
		rv = new RvGenerator();
	}
	@Override
	public void run() {
		while(true)
		{
			try {
				//replace it with the exponential distribution RV
				sleepTime = rangeMin + (rangeMax - rangeMin)*rv.uniRv();
				//System.out.println("ICT: "+(int)(sleepTime*1000));
				Thread.sleep((int)(sleepTime*1000));
				//Thread.sleep(1500);
				parentThread.ICTExpired = true;
				//System.out.println("ICT expired");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
