import java.util.Random;
import java.io.*;


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
				
				//System.out.println("MTT :"+(int)(rv.expRv(parentThread.mtt)*1000));
				if(parentThread.mSelfNodeID == 0)
				{
				try {
					File file  = new File("MTTLog.txt");
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(Integer.toString((int)(rv.expRv(parentThread.mtt))));
					bw.write("\n");
					bw.close();
					fw.close();
				}
				catch(Exception ex)
				{
					
				}
				}
				Thread.sleep((int)(rv.expRv(parentThread.mtt)));
				//Thread.sleep(20);
				parentThread.MTTExpired = true;
				//System.out.println("MTT expired");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
