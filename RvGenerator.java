import java.util.Random;

public class RvGenerator 
{
	double Seed;

	public RvGenerator()
	{	//constructor
		super();
		this.Seed = 1111.0;
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
		Random rand = new Random();
		return(rand.nextDouble());
	}
	
	public double expRv(double lambda)
	{		//function to generate exponential random variables 
		
		double exp;
	    exp = ((-1.0) / lambda) * Math.log(uniRv());
	    return(exp);
		
	}

}
