import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

class myComp implements Comparator<Integer>
{
	@Override
	public int compare(Integer n1, Integer n2) 
	{
		if(n1 < n2)
		{
			return 1;
		} else {
			return -1;
		}
	}
}

public class TestGoogleSea 
{
	static Set<String> set = new HashSet<String>();
	static String folderPath;
	static String products_file_path;
	public static final int k = 10;
	public static final int cut_number = 7;
	static int length1;
	static int count1;
	static String str1;
	static String token1[];
	static HashSet<String> hs = new HashSet<String>(Arrays.asList("able","about","across","after","all","almost","also","am",
			"among","an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","dear","did","do",
			"does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how",
			"however","i","if","in","into","is","it","its","just","least","let","like","likely","may","me","might","most","must",
			"my","neither","no","nor","not","of","off","often","on","only","or","other","our","own","rather","said","say","says",
			"she","should","since","so","some","than","that","the","their","them","then","there","these","they","this","tis","to",
			"too","twas","us","wants","was","we","were","what","when","where","which","while","who","whom","why","will","with",
			"would","yet","you","your","census","retrieved"));

	static HashMap<String, Integer> pwindow = new HashMap<String,Integer>();

	public static void main(String[] args) throws Exception
	{
		Set<String> temp_set = new HashSet<String>();

		String query;
		String configFilePath = args[0]; 
		Scanner sc1 = new Scanner(System.in);

		BufferedReader brr = new BufferedReader(new FileReader(new File(configFilePath)));
		folderPath = brr.readLine();
		products_file_path = brr.readLine();
		brr.close();

		System.out.println("Enter your query: ");
		query = sc1.nextLine();
		query = query.toLowerCase();	
		sc1.close();

		placeInset(query);

		Iterator<String> temp_it = set.iterator();
		while(temp_it.hasNext())
		{
			temp_set.add(temp_it.next());
		}

		TreeSet<String> sortedSet2 = new TreeSet<String>(set);
		System.out.println("The sorted list is:");
		System.out.println(sortedSet2);
		Iterator<String> it = temp_set.iterator();

		while(it.hasNext())
		{
			String queryToBeSearched = it.next();
			queryToBeSearched = queryToBeSearched.trim();
			if(set.contains(queryToBeSearched))
			{
				if(TestGoogleSea.getcorrelation(queryToBeSearched, query, 4) <= 0.5)
				{
					removeSubset(queryToBeSearched);
					set.remove(queryToBeSearched);
					System.out.println(queryToBeSearched+" SUBSET REMOVED,  SET CONTENTS: "+set);
				}
				else
				{
					removeSuperset(queryToBeSearched);
					System.out.println(queryToBeSearched+ " SUPERSET REMOVED, SET CONTENTS: "+set);
				}
			}
		}
		TreeSet<String> sortedSet = new TreeSet<String>(set);
		System.out.println("\nID Token Sets: ");
		System.out.println(sortedSet);
		System.out.println();

		Set<String> temp_set1 = new HashSet<String>();

		Iterator<String> it1 = sortedSet.iterator();

		while(it1.hasNext())
		{
			temp_set1.add(it1.next());
		}

		Iterator<String> it_set = sortedSet.iterator();
		while(it_set.hasNext())
		{
			String mention = it_set.next();
			if(stringSimilarity.similarity(mention, query)==true)
			{
				System.out.println("Mention: " + mention);
			}
			else
			{
				System.out.println("NOT A Mention: " + mention);
				temp_set1.remove(mention);
			}
		}
		Iterator<String> it2 = temp_set1.iterator();
		set.clear();
		while(it2.hasNext())
		{
			set.add(it2.next());
		}
		pWindowContext();
	}

	public static void pWindowContext() throws Exception
	{
		Iterator<String> temp_it;
		temp_it = set.iterator();
		boolean flag = false;	

		Map<String, Integer> temp = new HashMap<String, Integer>();


		while(temp_it.hasNext())
		{
			String setValue=temp_it.next();
			BufferedReader br;

			File directory = new File(folderPath + setValue + "/");
			File[] fList = directory.listFiles();
			if(fList != null)
			{
				for (File file : fList)
				{
					if (file.isFile())
					{
						br = new BufferedReader(new FileReader(folderPath + setValue + "/" + file.getName()));

						String line="";
						String documentTobeRead="";
						while((line=br.readLine())!=null)
						{
							documentTobeRead = documentTobeRead + line;
						}
						br.close();
						Iterator<String> stopWords = hs.iterator();
						while(stopWords.hasNext())
						{
							documentTobeRead=documentTobeRead.replaceAll((" "+stopWords.next()+" ").toString(), " ");
						}

						String tempStr = returnallString(documentTobeRead, setValue, 3);

						if(tempStr == null)
							continue;

						String token[] = tempStr.split("@");

						int i,j;
						for(i=0;i<token.length;i++)
						{
							token[i] = cutString(setValue,token[i], cut_number);
							String spaceToken [] = token[i].split(" ");
							if(token[i] == " " || token[i]=="")
								continue;
							token[i] = token[i].trim();
							//System.out.println("completeString= "+token[i]);

							for(j=0;j<spaceToken.length;j++)
							{
								spaceToken[j] = spaceToken[j].trim();
								if(spaceToken[j]!="" && spaceToken[j]!=null && spaceToken[j]!=" ")
								{
									//System.out.println("completeString= "+token[i]);
									//	if(!setValue.contains(spaceToken[j]))
									{
										if(pwindow.containsKey(spaceToken[j]))
										{
											int count = pwindow.get(spaceToken[j]);
											count = count + 1;
											pwindow.put(spaceToken[j], count);

										}
										else
										{
											pwindow.put(spaceToken[j], 1);
										}
									}
								}
							}
						}
					}
				}
			}

			if(flag == false)
			{
				flag = true;

				for(Map.Entry<String, Integer> e : pwindow.entrySet())
				{
					temp.put(e.getKey(), e.getValue());
				}
				pwindow.clear();
			}
			else
			{

				Map<String, Integer> result = new HashMap<String, Integer>();
				for(Map.Entry<String, Integer> e : pwindow.entrySet())
				{
					String p_key = e.getKey();
					int p_value = e.getValue();
					for(Map.Entry<String, Integer> t : temp.entrySet())
					{
						String t_key = t.getKey();
						int t_value = t.getValue();
						if(p_key.equalsIgnoreCase(t_key))
						{
							result.put(p_key, p_value + t_value);
							break;
						}
					}
				}
				temp.clear();
				for(Map.Entry<String, Integer> e : result.entrySet())
				{
					temp.put(e.getKey(), e.getValue());
				}
				pwindow.clear();
			}
		}

		Map<Integer, String> reverse_map = new TreeMap<Integer, String>(new myComp());

		for(Map.Entry<String, Integer> e : temp.entrySet())
		{
			String str = e.getKey().trim();
			if(str.compareTo("") == 0)
				continue;
			reverse_map.put(e.getValue(), str);
		}

		/*for(Map.Entry<Integer, String> e : reverse_map.entrySet())
		{
			System.out.println("Key: "+e.getKey());
			System.out.println("Value: "+e.getValue());
		}
*/
		Set<String> synonym_words = new HashSet<String>();
		int i=0;
		int productValue[] = new int[stringSimilarity.productNames.size()+1];
		int maxValue=0;
		for(Map.Entry<Integer, String> e : reverse_map.entrySet())
		{
			int count = e.getKey();
			String word = e.getValue();
			if(word != null && i<k)
			{
				i++;
				synonym_words.add(e.getValue());
				//System.out.println("Key  : "+count);
				//System.out.println("Value: "+word);
				int count_product=0;
				Iterator<String> itr = stringSimilarity.productNames.iterator();
				while(itr.hasNext())
				{
					String str_product = itr.next();
					if(str_product.toLowerCase().contains(word.toLowerCase()))
					{
						productValue[count_product]++;
						if(maxValue < productValue[count_product])
							maxValue= productValue[count_product];
					}
					count_product++;
				}
			}
		}

		//System.out.println("Maxval: "+maxValue);

		Iterator<String> itr = stringSimilarity.productNames.iterator();
		int ii=0;
		while(itr.hasNext())
		{
			String str=itr.next();
			if(productValue[ii] != maxValue)
			{
				ii++;
				continue;
			}
			System.out.println("Canonical name with evidence: "+str);
			ii++;
		}
	}

	public static String cutString(String tau_e,String longStr,int windowSize)
	{
		String token[] =  tau_e.split(" ");
		int min = Integer.MAX_VALUE;
		int max = -1,index;
		for(int i=0;i<token.length;i++)
		{
			index = longStr.lastIndexOf(token[i]);
			if(index > max)
			{
				max = index; 
			}
			if(index < min)
			{
				min = index;
			}
		}
		if(max == -1 || min==Integer.MAX_VALUE )
			return "";
		int index1=0,index2=0;
		int count=0;
		int i;
		for(i=min;i>=0;i--)
		{
			if(i>0 && longStr.charAt(i)==' ' )
			{
				if(longStr.charAt(i-1)!=' ')
					count++;
			}
			if(count == windowSize+1)
			{
				index1 = i; 
				break;
			}
		}
		if(i==0)
		{
			index1 = 0;
		}
		count =0 ;
		for(i=max;i<longStr.length();i++)
		{

			if(i+1<longStr.length() && longStr.charAt(i)==' '  )
			{
				if(longStr.charAt(i+1)!= ' ')
					count++;
			}
			if(count == windowSize +1 )
			{
				index2 = i;
				break;
			}
		}
		if(i==longStr.length())
			index2 = i;
		return longStr.substring(index1, index2);
	}

	public static String returnallString(String d, String tau_e, int words_between_query)
	{
		String []arr_tau_e = tau_e.split(" ");
		int length = arr_tau_e.length;
		String wordsFortau;
		int index=0,i,j,forInquery;
		boolean flag=false;
		String returnString="";
		for(i=0;i<length;i++)
		{
			index=0;
			index = d.indexOf(arr_tau_e[i], index);
			flag=false;
			while(index!=-1)
			{
				flag=false;
				wordsFortau = getWords(index, d, words_between_query*(length+2));
				for(j=0;j<length;j++)
				{
					if(j==i)
						continue;
					else
					{
						forInquery = wordsFortau.indexOf(arr_tau_e[j]);
						if(forInquery == -1)
						{	
							flag=true;
							break;
						}
					}
				}
				if(flag==false)
				{
					int len = wordsFortau.length();
					String str="";
					int start=0;
					while(start <= len)
					{
						str= str+'@'+' ';
						start += 2;
					}
					d=d.replace(wordsFortau, str);
					returnString = returnString + wordsFortau +"@";
				}
				index++;
				index = d.indexOf(arr_tau_e[i], index);
			}
		}
		if(returnString != "")
			return returnString;
		return null;
	}

	public static void removeSuperset(String query)
	{
		Iterator<String> it = set.iterator();
		String str="";
		int i,j,count;
		Set<String> setForRemove = new HashSet<String>();
		String tokenForQuery[] = query.split(" ");
		int length=query.length();
		while(it.hasNext())
		{
			str=it.next().toString();
			count=0;
			if(str.length() > length)
			{
				String tokenForStr[] = str.split(" ");
				for(i=0;i<tokenForQuery.length;i++)
				{
					for(j=0;j<tokenForStr.length;j++)
					{
						if(tokenForStr[j].compareToIgnoreCase(tokenForQuery[i])==0)
						{
							count++;
							break;
						}
					}
					if(j==tokenForStr.length)
						break;
				}
				if(count==tokenForQuery.length)
				{
					setForRemove.add(str);
				}
			}
		}
		Iterator<String> iter = setForRemove.iterator();
		while(iter.hasNext())
		{
			String val=iter.next();
			set.remove(val);
		}
	}

	public static void removeSubset(String query)
	{
		Iterator<String> it = set.iterator();
		String str="";
		int i,j,count;
		Set<String> setForRemove = new HashSet<String>();
		String tokenForQuery[] = query.split(" ");
		int length=query.length();
		while(it.hasNext())
		{
			str=it.next().toString();
			if(str.length() < length)
			{
				String tokenForStr[] = str.split(" ");
				count=0;
				for(i=0;i<tokenForStr.length;i++)
				{
					for(j=0;j<tokenForQuery.length;j++)
					{
						if(tokenForStr[i].compareToIgnoreCase(tokenForQuery[j])==0)
						{	count++;
						break;
						}
					}
					if(j==tokenForStr.length)
						break;
				}
				if(count==tokenForStr.length)
				{
					setForRemove.add(str);
				}
			}
		}
		Iterator<String> iter = setForRemove.iterator();
		while(iter.hasNext())
		{
			String val=iter.next();
			set.remove(val);
		}
	}

	public static void addInSet(int check,int value,String query)
	{
		if(value==count1)
		{
			set.add(query.trim());
			return;
		}
		for(int i=0;i<length1;i++)
		{
			if(i<=check)
				continue;
			addInSet(i,value+1,query+" "+token1[i]);
		}
	}

	public static void placeInset(String query)
	{
		query = query.trim();
		set.add(query);
		str1=query;
		int len=0,i;
		token1 = query.split(" ");
		for(i=0;i<token1.length;i++)
		{
			if(token1[i] != "")
			{	
				len++;
				set.add(token1[i]);
			}
		}
		length1=len;
		for(i=2;i<len;i++)
		{
			count1=i;
			addInSet(-1,0,"");
		}
	}

	public static void searchGoogle(String query, int no_search_results, String origQuery) throws Exception
	{
		try 
		{
			System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
			System.setProperty("http.proxyPort", "8080");

			System.setProperty("https.proxyHost","proxy.iiit.ac.in") ; 
			System.setProperty("https.proxyPort", "8080") ; 

			int count=0;

			for(int j=0; j<no_search_results; j=j+4)
			{
				String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+(j)+"&q=";
				String charset = "UTF-8";

				URL url = new URL(address + URLEncoder.encode(query, charset));
				Reader reader = new InputStreamReader(url.openStream(), charset);
				GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

				Thread.sleep(2000);

				if(results != null)
				{
					int total = results.getResponseData().getResults().size();
					for(int i=0; i<total; i++)
					{
						try 
						{
							String googleurl = results.getResponseData().getResults().get(i).getUrl();
							System.out.println("URL: " + googleurl + "\n");
							count++;
							getData(googleurl, count, origQuery, query);
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					System.out.println("\n\n NULL RESULT SENT BY GOOGLE \n\n");
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static void getData(String webPage, int fileNo, String origQuery, String query) throws Exception
	{	
		String result = "";

		if(webPage.contains("https"))
		{
			String https_url = webPage;
			URL url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

			if(con!=null)
			{			
				InputStream is = con.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) 
				{
					sb.append(charArray, 0, numCharsRead);
				}
				result = sb.toString();
			}
		}
		else
		{
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) 
			{
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		}

		Document doc = Jsoup.parse(result);
		String text = doc.body().text();
		String title = doc.title();

		text = text.replaceAll("[^A-Za-z0-9 ]", " ");
		title = title.replaceAll("[^A-Za-z0-9 ]", " ");

		String normalquery = query.replaceAll("[^A-Za-z0-9 ]", " ");

		File tauDir = new File(folderPath + normalquery);
		if (!tauDir.exists()) 
		{
			System.out.println("creating directory: " + tauDir + "\n");
			tauDir.mkdir();  
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(folderPath+normalquery+"/Body_"+fileNo+".txt")));
		bw.write("TITLE:"+title.toLowerCase()+ "$#" + System.lineSeparator() +text.toLowerCase());
		bw.close();
	}

	/* Function for computing whether corresponding substring of 
	 * entity (tau_e) is valid with respect to entity (e) in 
	 * corresponding document string (d). Here p is the window context
	 */
	public static int g(String tau_e, String e, String d, int p)
	{
		String []arr_tau_e = tau_e.split(" ");
		String []arr_e = e.split(" ");

		LinkedHashSet<String> hs_tau_e = new LinkedHashSet<String>();
		LinkedHashSet<String> hs_dash_tau_e = new LinkedHashSet<String>();
		LinkedHashSet<String> hs_e = new LinkedHashSet<String>();

		for(int i=0; i<arr_tau_e.length; i++)
		{
			hs_tau_e.add(arr_tau_e[i]);
		}
		for(int i=0; i<arr_e.length; i++)
		{
			hs_e.add(arr_e[i]);

			if(!hs_tau_e.contains(arr_e[i]))
			{
				hs_dash_tau_e.add(arr_e[i]);
			}
		}
		String windowedWord = checkDocument(d, tau_e, 4);

		if(windowedWord == null)
			return 0;

		Iterator<String> it = hs_dash_tau_e.iterator();

		int count=0;

		while(it.hasNext())
		{
			if(windowedWord.contains(it.next()))
			{
				count++;
			}
		}
		if(count >= hs_dash_tau_e.size())
		{
			return 1;
		}
		return 0;
	}


	public static String checkDocument(String d, String tau_e, int words_between_query)
	{
		tau_e = tau_e.trim();
		String []arr_tau_e = tau_e.split(" ");
		int length = arr_tau_e.length;
		String wordsFortau;
		int index=0,i,j,forInquery;
		boolean flag=false;

		for(i=0;i<length;i++)
		{
			index=0;
			index = d.indexOf(arr_tau_e[i], index);
			flag=false;

			while(index!=-1)
			{
				flag=false;
				wordsFortau = getWords(index, d, words_between_query*length);

				for(j=0;j<length;j++)
				{
					if(j==i)
						continue;
					else
					{
						forInquery = wordsFortau.indexOf(arr_tau_e[j]);
						if(forInquery == -1)
						{
							flag=true;
							break;
						}
					}
				}
				if(flag==false)
				{
					return wordsFortau;
				}
				index++;
				index = d.indexOf(arr_tau_e[i], index);
			}
		}
		return null;
	}

	public static String getWords(int index, String doc, int p)
	{
		try 
		{
			String str="";
			int i,words=0;
			int index1=-1;
			int index2=-1;
			for(i=index;i>=0;i--)
			{
				if(i >= 0 && doc.charAt(i)==' ' && doc.charAt(i-1)!=' ')
					words++;
				if(words==p)
				{	index1=i+1;
				break;
				}
			}
			if(i==-1)
				index1=0;
			words=0;

			for(i=index;i<doc.length();i++)
			{
				if((i+1) < doc.length() && (doc.charAt(i)==' ') && (doc.charAt(i+1)!=' '))
					words++;
				if(words==p+1)
				{
					index2=i;
					break;
				}
			}
			if(i==doc.length())
				index2=doc.length();
			str=doc.substring(index1, index2);
			return str;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public static float getcorrelation(String tau_e, String e, int no_search_results) throws Exception
	{
		String normal_tau_e = tau_e.replaceAll("[^A-Za-z0-9 ]", " ");
		File directory1 = new File(folderPath + normal_tau_e);

		if(!directory1.exists())
		{
			searchGoogle(tau_e, no_search_results, e);
		}
		else
		{
			System.out.println("Skipped google search.");
		}

		int num_sum = 0;
		int den_sum = 0;

		File directory = new File(folderPath + normal_tau_e + "/");
		File[] fList = directory.listFiles();
		if(fList != null)
		{
			for (File file : fList)
			{
				if (file.isFile())
				{
					String text = new String(Files.readAllBytes(Paths.get(folderPath + normal_tau_e + "/" + file.getName())), StandardCharsets.UTF_8);
					if(checkDocument(text, tau_e, 4) != null)
					{
						den_sum ++;
					}
					num_sum += TestGoogleSea.g(tau_e, e, text, 4);
				}
			}
		}
		else
			System.out.println("No single file created !!!");

		float corr = (float) ((num_sum*1.0)/(den_sum*1.0));

		return corr;
	}
}

class GoogleResults
{
	private ResponseData responseData;
	public ResponseData getResponseData() { return responseData; }
	public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
	public String toString() { return "ResponseData[" + responseData + "]"; }

	static class ResponseData {
		private List<Result> results;
		public List<Result> getResults() { return results; }
		public void setResults(List<Result> results) { this.results = results; }
		public String toString() { return "Results[" + results + "]"; }
	}

	static class Result {
		private String url;
		private String title;
		public String getUrl() { return url; }
		public String getTitle() { return title; }
		public void setUrl(String url) { this.url = url; }
		public void setTitle(String title) { this.title = title; }
		public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
	}
}