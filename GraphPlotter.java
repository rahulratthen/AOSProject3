import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class GraphPlotter {

	public static void main(String args[])
	{
		int totalForced = 0;
		double avg=0;
		for(int i=0;i<10;i++)
		{
			totalForced = 0;
			try {
				BufferedReader br = new BufferedReader(new FileReader("FinalStats"+i+".txt"));
				String line = br.readLine();

		        while (line != null) {
		            totalForced += Integer.parseInt(line);
		        	
		        	line = br.readLine();
		        }
		        avg += (double)totalForced/500; 
				br.close();
			}
			catch(Exception e)
			{
				
			}
		}
		avg = avg/10;
		
		System.out.println("Average :"+avg );
	}
	
}