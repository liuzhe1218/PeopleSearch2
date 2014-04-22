package crawler;

import java.io.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class BaseCrawler {
	protected HttpClient client;
	BaseCrawler(){
		client=new HttpClient();
	}
	
	public String[] getWebPage(String url){ //这边先过滤所有没有<a>的行？
		String page="";
		String charset="";
		StringBuffer strBuf=new StringBuffer();
		//client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		GetMethod get=new GetMethod(url);
		try {
			client.executeMethod(get);
			//page=get.getResponseBodyAsString();
			System.out.println(get.getResponseCharSet());
			get.getResponseCharSet();
			//System.out.println(page);
			BufferedReader br=new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(),get.getResponseCharSet()));
			String line;
			while((line=br.readLine())!=null){
				strBuf.append(line+"\n");
			}
			page=strBuf.toString();
			charset=get.getResponseCharSet();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		get.releaseConnection();
		String []ret=new String[2];
		ret[0]=page;
		ret[1]=charset;
		return ret;
	}
	
	public static void main(String []args){
		BaseCrawler bc=new BaseCrawler();
		String[] page=bc.getWebPage("http://www.baidu.com/link?url=kioWGJqjJ4zBBpC8yDF8xDh8vibi2lZjEXACr9UONBuuOlprSmglbdYlQHWlpDfDMkj1");
		
		//System.out.println(page);
		try {
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/test2.html"),page[1]));
			bw.write(page[0]);
			bw.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
