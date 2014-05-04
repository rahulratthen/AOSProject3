import java.util.Random;

public class RvGenerator 
{
	Random rand;

	public RvGenerator()
	{	//constructor
		super();
		
		rand = new Random();
	}
	
	/*
 public double uniRv()
	 
	{				
		
		double k = 16807.0;
	    double m = 2.147483647e9;
	    double rv;

	    Seed=(k*Seed)% m;	
	    rv=Seed/m;
	    return(rv);
	}
*/	
	public double uniRv()
	{	
		
		return(rand.nextDouble());
	}
	
	public double expRv(double lambda)
	{		//function to generate exponential random variables 
		
		double exp;
		double u = Math.random();
	    //exp = (double)((-1.0) / lambda) * Math.log(1-uniRv());
	    exp = -Math.log(1-u)*lambda;
		return(exp);
		
	}

}
