import java.util.ArrayList;


public class DiceCoefficient 
{	
	public static double getdiceCoefficientSimilarity(String s1, String s2)
	{
		ArrayList<String> s1_arr = new ArrayList<>();
		ArrayList<String> s2_arr = new ArrayList<>();
		
		int nt=0, nx=0, ny=0;
		
		for(int i=0; i<(s1.length()-1); i++)
		{
			s1_arr.add(s1.substring(i, i+2));
		}
		
		for(int i=0; i<(s2.length()-1); i++)
		{
			s2_arr.add(s2.substring(i, i+2));
		}
		
		for(int i=0; i<s1_arr.size(); i++)
		{
			if(s2_arr.contains(s1_arr.get(i)))
			{
				nt++;
			}
		}
		
		nx = s1_arr.size();
		ny = s2_arr.size();
		
		double ans = (double)(2*nt*1.0)/(double)(nx+ny);
		
		return ans;
	}
}
