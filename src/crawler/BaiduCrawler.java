package crawler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class BaiduCrawler extends BaseCrawler{
	
	/*
	 * author:czj
	 * function:search in baidu websearch
	 * return: str[0],the page searched,str[1],the charset
	 */
	public String[] WebSearch(String query){
		String[] str=new String[2];
		String url;
		try {
			url = "http://www.baidu.com/s?wd="+URLEncoder.encode(query,"UTF-8");
			str=getWebPage(url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	/*
	 * author:czj
	 * function:get the url of the search result in the baidu web search
	 * 
	 */
	public ArrayList<String> getUrlInWebSearch(String page,String charset){
		ArrayList<String> list=new ArrayList<String>();
		Parser parser=Parser.createParser(page, charset);
		try {
			NodeFilter filter1=new TagNameFilter("td");
					//new AndFilter(new TagNameFilter("h3"),new HasAttributeFilter("href"));
			NodeList nodes = parser.extractAllNodesThatMatch(filter1);
			for(int i=0;i<nodes.size();i++){
				//System.out.println(i+"\n"+nodes.elementAt(i).toString());
				Node node=nodes.elementAt(i);
				Node node2=node.getChildren().elementAt(0);
				//System.out.println(node2.toString());
				String str=node2.toString();
				int index1=str.indexOf("Link to : ");
				int index2=str.indexOf("; titled : ");
				int index3=str.indexOf("; begins at :");
				String url=str.substring(index1+("Link to : ").length(), index2);
				String title=str.substring(index2+("; titled : ").length(),index3);
				//title+url?
				System.out.println(url);
				System.out.println(title);
				list.add(url);
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public String[] baikeSearch(String query,int offset){//某一页的所有内容以及编码
		String []str=new String[2];
		String url;
		try {
			url = "http://baike.baidu.com/taglist?tag="+URLEncoder.encode(query,"GBK")+"&offset="+offset;
			str=getWebPage(url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	public ArrayList<Links> getTags(String page,String charset){//根据不同的tag,找不不同的人物以及对应的url,某个offset下的页面
		ArrayList<Links> list = new ArrayList<Links>();
		Links link;
		String regex = "<a href=\"/view/.*?</a>";
		String url = "";
		String name = "";
		try{
			page = new String(page.getBytes(charset),"GBK");
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(page);
		while (match.find()){
			//System.out.println(match.group());
			link = new Links();
			url = match.group().replaceAll("<a href=\"/", "").replaceAll("\" target=\"_blank\">.*?</a>","");
			//System.out.println(url);
			Matcher title = Pattern.compile(">.*?</a>").matcher(match.group());//找人物名字
			while(title.find()){
				name = title.group().replaceAll(">", "").replaceAll("</a", "");
				//System.out.println(name);
			}
			link.setUrl(url);
			link.setTopic(name);
			list.add(link);
		}
		return list;
	}
	public ArrayList<Links> getUrlInBaikeSearch(String page, String charset){
		Links link;
		ArrayList<Links> list = new ArrayList<Links>();
		String regex1 = "<a.*?/a>|<div class=\"dirtit\">.*?</div>";
		//<div class="dirtit">社会科学</div>
		String url = "";//链接
		String content = "";//第三极节点的标题
		String topictitle ="";//2级标题节点名称
		String topicnum = ""; //2级标题特征数字串
		try {
			page=new String(page.getBytes(charset),"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern pattern = Pattern.compile(regex1);
		Matcher match = pattern.matcher(page);	
		while(match.find()){
			url = match.group();
			if (!(url.contains("name")&&url.contains("id"))&&!url.contains("taglist")&&!url.contains("dirtit")){//过滤没用的链接元素
			}
			else{
				if (url.contains("name")&&url.contains("id")){//抓第二级节点
					topicnum = match.group().replaceAll("<a name=\".*? id=\"","");
					topicnum = topicnum.replaceAll("\"></a>","");
					System.out.println(topicnum);
				}
				else if (url.contains("dirtit")){//第二节节点标题
					topictitle = match.group().replace("<div class=\"dirtit\">", "");
					topictitle = topictitle.replace("</div>", "");
					//System.out.println(topictitle);
					link = new Links();
					link.setTopic(topictitle);
					link.setUrl(topicnum);
					list.add(link);
					topictitle = "";
					topicnum = "";
				}
				else{ //抓taglist
					link = new Links();
					url = match.group().replaceAll("<a href=\"", "");
					url = url.replaceAll("\"target=\"_blank\">.*?</a>","");
					//System.out.println(url);
					link.setUrl(url);
					Matcher title = Pattern.compile(">.*?</a>").matcher(match.group());
					while(title.find()){
						content = title.group().replaceAll("href=|>","").replaceAll("</a","");
						//System.out.println("the title of "+match.group()+" is: "+content);	
				    }
					link.setTopic(content);
					list.add(link);
				}
			}
		}
		return list;
	}
	public void writetxt(String tag1, String tag2,String name, String content, String charset){//写网页内容到文件中
		String filename = "F://people//";
		filename +=tag1+"-"+tag2+"-"+name+".html";// F://people//tag1-tag2-name.txt
		System.out.println(filename);
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),charset));
			bw.write(content);
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
		/*File file = new File(filename);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(content);
			bw.close();
			System.out.println(tag1+"-"+tag2+"-"+name+" is ok");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public int getBaikeResultNum(String query){//返回某一分类的条目数,判断是否大于760
		int num=0;
		try {
			String str[]=baikeSearch(query,0);
			String page=str[0];
			String charset=str[1];
			page=new String(page.getBytes(charset),"GBK");
			Pattern p=Pattern.compile("</span><span style=\"font-size:14px;color:#666;\">&nbsp;\\(共([0-9]+)个\\)</span></div>");
			Matcher m=p.matcher(page);
			if(m.find()){
				System.out.println("find");
				num=Integer.parseInt(m.group(1));
			}
			//System.out.println(page);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	/*public static void main(String args[]){
		String query="张杰";
		BaiduCrawler baidu=new BaiduCrawler();
		//String ret[]=baidu.baikeSearch("科学家", 0);
		int num = baidu.getBaikeResultNum("科学家");
		System.out.println(num);
		BufferedWriter bw;
		try {
			baidu.getUrlInBaikeSearch(ret[0], ret[1]);
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/test.html"),ret[1]));
			bw.write(ret[0]);
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
	}*/
}