package crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * 搜索百度百科
 */
public class BaikeSearching {
	public static void main(String[] args){
	    BaiduCrawler baidu = new BaiduCrawler();
		BaseCrawler base = new BaseCrawler();
		String url = "http://baike.baidu.com/class/1408.html";
		//  http://baike.baidu.com/view/203033.html?fromTaglist
		String url2 = "taglist?tag=%BF%C6%D1%A7%BC%D2";
		//System.out.println(result[0]+" "+result[1]);
		String[] temp = base.getWebPage(url);
		String tag1 = "";
		String tag2 = "";
		String name = "";
		String tag2url = "";
		int num;
		int i,j;
		int MAXPAGE;
		String suffix = "http://baike.baidu.com"; // suffix
		String suffix2 = "&offset=";
		String content[];
		ArrayList<Links> list = new ArrayList<Links>();
		ArrayList<Links> list2 = new ArrayList<Links>();
		Links link;
		Links link2;
		list = baidu.getUrlInBaikeSearch(temp[0],temp[1]);// first level
		for (i=0;i<list.size();i++){
			link = (Links)list.get(i);
			if (!link.getUrl().contains("taglist")){
				tag1 = link.getTopic();// tag1
			}
			else{
				 tag2 = link.getTopic();// tag2
				 tag2url = suffix+link.getUrl()+suffix2;// url&offset=?
				 num = baidu.getBaikeResultNum(link.getTopic());// total number of results
				 if (num>=760){
					 for (j=0;j<=75;j++){
						 tag2url = suffix+link.getUrl()+suffix2+j*10;//&offset=10*j
						 String[] temp2 = base.getWebPage(tag2url);
						 list2 = baidu.getTags(temp2[0], temp2[1]);//arraylist
						 for (int k=0;k<list2.size();k++){
							 link2 = (Links)list2.get(k);
							 //bw.write(link2.getUrl()+" "+tag2+"-"+link2.getTopic());
							 //System.out.println(link2.getUrl()+" "+tag2+"-"+link2.getTopic());
							 content = base.getWebPage("http://baike.baidu.com/"+link2.getUrl());
							 baidu.writetxt(tag1, tag2, link2.getTopic(),content[0],content[1]);
							 try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						 }
					 }
				 }
				 else{// less than 760
					MAXPAGE = (int)Math.ceil((double)num/10);
					for (j=0;j<MAXPAGE;j++){
						tag2url = suffix+link.getUrl()+suffix2+j*10;//&offset=10*j
						 String[] temp2 = base.getWebPage(tag2url);
						 list2 = baidu.getTags(temp2[0], temp2[1]);//arraylist
						 for (int k=0;k<list2.size();k++){
							 link2 = (Links)list2.get(k);
							 //bw.write(link2.getUrl()+" "+tag2+"-"+link2.getTopic());
							 //System.out.println(link2.getUrl()+" "+tag2+"-"+link2.getTopic());
							 content = base.getWebPage("http://baike.baidu.com/"+link2.getUrl());
							 baidu.writetxt(tag1, tag2, link2.getTopic(),content[0],content[1]);
							 try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						 }
					}
				 }
			}
		}	
		//list = baidu.getTags(temp[0], temp[1], "people");
		//System.out.println(list.size());
		/*File file = new File("F://1.txt");
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			for (int i=0; i<list.size();i++){
				link = (Links)list.get(i);
				bw.write(link.getUrl()+" "+link.getTopic());
				bw.write("\r\n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
