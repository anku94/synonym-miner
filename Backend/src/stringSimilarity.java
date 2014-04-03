import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class stringSimilarity 
{
	static final String products_file_path = TestGoogleSea.products_file_path;
	static HashSet<String> canonicalNames = new HashSet<String>();
	static String setQuery="";
	static Set<String> productNames = new LinkedHashSet<String>();

	public static LinkedHashSet<String> getComplement(String s2, String s1) //s1 Chhoti query, s2 User Query;
	{
		String []arr_s1 = s1.split(" ");
		String []arr_s2 = s2.split(" ");

		LinkedHashSet<String> hs_arr_s1 = new LinkedHashSet<String>();
		LinkedHashSet<String> hs_dash = new LinkedHashSet<String>();

		for(int i=0; i<arr_s1.length; i++)
		{
			hs_arr_s1.add(arr_s1[i]);
		}
		for(int i=0; i<arr_s2.length; i++)
		{
			if(!hs_arr_s1.contains(arr_s2[i]))
			{
				hs_dash.add(arr_s2[i]);
			}
		}
		return hs_dash;
	}

	public static boolean similarity(String query, String UserQuery) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(new File(products_file_path)));
		String line = "";
		while((line=br.readLine())!=null)
		{	

			line=line.toLowerCase();
			Levenshtein l = new Levenshtein(query,line);
			int leven = l.getSimilarity();

			double levenValue = (leven*1.0)/line.length();
			double jaccard = JaccardSimilarity.jaccardSimilarity(query,line);
			double dice = DiceCoefficient.getdiceCoefficientSimilarity(query,line);

			if(line.toLowerCase().contains(query.toLowerCase()))
			{
				canonicalNames.add(line);
			}
			else if(levenValue <= 0.95 && jaccard > 0.10 && dice > 0.20)
			{		
				canonicalNames.add(line);
			}
			
			//DamerauLevenshtein d = new DamerauLevenshtein(s1, s2);
			//System.out.println("Damareu Levenshtein: (0:Exact match)  "+d.getSimilarity());
			//JaroWinkler j = new JaroWinkler();
			//System.out.println("JaroWinkler: (0:NoSimilarity, 1:exact match)  "+j.compare(s1, s2));		
			//Ngram n = new Ngram();
			//System.out.println("N-gram: (0:no Match,1:Match) "+n.getSimilarity(s1, s2, 10));
			//Markov m = new Markov();
			//m.getSimilarity(a);
		}
		br.close();
		
		Iterator<String> it = canonicalNames.iterator();
		boolean returnValue=false;
		while(it.hasNext())
		{
			String str = returnMatchValue(UserQuery, query, it.next(), 3);
			if(str != null)
			{
				System.out.println("CANONICAL NAME: "+str);
				productNames.add(str);
				returnValue = true;
			}
		}
		return returnValue;
	}

	public static String returnMatchValue(String UserQuery, String tau_e, String d, int words_between_query)
	{
		tau_e = tau_e.trim();
		String []arr_tau_e = tau_e.split(" ");
		int length = arr_tau_e.length;
		String wordsFortau;
		int index=0,i,j,forInquery;
		int flag=length;
		for(i=0;i<length;i++)
		{
			index=0;
			index = d.indexOf(arr_tau_e[i], index);
			flag=length;
			while(index!=-1)
			{
				flag=length;
				wordsFortau = getWords(index, d, words_between_query);

				for(j=0;j<length;j++)
				{
					if(j==i)
						continue;
					else
					{
						forInquery = wordsFortau.indexOf(arr_tau_e[j]);
						if(forInquery == -1)
						{
							flag--;
							break;
						}
					}
				}
				if(flag >= (length/2 - 1))
				{
					LinkedHashSet<String> lhs = getComplement(UserQuery, tau_e);

					Iterator<String> it = lhs.iterator();
					int count = 0;
					while(it.hasNext())
					{
						if(wordsFortau.contains(it.next()))
							count++;
					}

					if(count >= (lhs.size()-1))
						return d;
					else
						return null;
				}
				index++;			
				index = d.indexOf(arr_tau_e[i], index);
			}	
		}
		return null;
	}

	public static String getWords(int index, String doc, int p)
	{
		try {
			String str="";
			int i,words=0;
			int index1=-1;
			int index2=-1;
			for(i=index;i>=0;i--)
			{
				if(i<doc.length())
				{
					if(i >= 0 && doc.charAt(i)==' ' && doc.charAt(i-1)!=' ')
						words++;
					if(words==p)
					{	
						index1=i+1;
						break;
					}
				}
			}
			if(i==-1)
			{
				index1=0;
			}
			
			words=0;

			for(i=index;i<doc.length();i++)
			{
				if(i < doc.length() && (doc.charAt(i)==' ') && (doc.charAt(i+1)!=' '))
					words++;
				if(words==p+1)
				{
					index2=i;
					break;
				}
			}
			if(i==doc.length())
				index2=doc.length();
			str=doc.substring(index1, index2-1);
			return str;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}

	}
}
