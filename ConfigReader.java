import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
public class ConfigReader 
{

	private int mNumOfNodes = 0;
	double MTT = 0;
	double ICT = 0;
	private List<String[]> mNodeConfig = new ArrayList<>();

	ConfigReader (String mConfigFile) {
		BufferedReader mFileReader;

		// System.out.println("ConfigReader : "+mConfigFile);

		try {
			String mLine;
			int index = 0;
			mFileReader =  new BufferedReader(new FileReader(mConfigFile));
			while((mLine = mFileReader.readLine()) != null) {
				// System.out.println("ConfigReader : Line "+index+ " : "+mLine);
				index++;

				String[] mColomns = mLine.trim().split(" ");
				if (mColomns.length == 1) 
				{
					mNumOfNodes = Integer.parseInt(mColomns[0]);
				}
				else if(mColomns.length == 2)
				{
					MTT = Integer.parseInt(mColomns[0]);
					ICT = Integer.parseInt(mColomns[1]);
				}
				else 
				{
					mNodeConfig.add(mColomns);
				}
			}
			mLine = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		mFileReader = null;
	}

	public int getNodeCount() 
	{
		return mNumOfNodes;
	}
	
	public double getMTT() 
	{
		return MTT;
	}
	
	public double getICT() 
	{
		return ICT;
	}

	public String[] getNodeConfig(int mNodeIndex) {
		return mNodeConfig.get(mNodeIndex);
	}
}


