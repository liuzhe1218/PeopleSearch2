package crawler;

import java.net.URLDecoder;
import sun.misc.*;

import org.apache.commons.codec.binary.Base64;

public class Test {
	public static void main(String []args){
		//http://www.baidu.com/link?url=77JKGJqjJ4zBBpC8yDF8xDhjrTri46BkC8Q_b5oSNd85MUB9VmcpaRBw0mzQzGaMOUjXN9rpcFZwQZBm4qvEKTa-S7cK-6AO-1Ly-XhideKe0fGUWTVVS9y5Zu5qj7TtvvKGJO_JhiQy
		//String url="77JKGJqjJ4zBBpC8yDF8xDhjrTri46BkC8Q_b5oSNd85MUB9VmcpaRBw0mzQzGaMOUjXN9rpcFZwQZBm4qvEKTa-S7cK-6AO-1Ly-XhideKe0fGUWTVVS9y5Zu5qj7TtvvKGJO_JhiQy";
		//String str=new String(Base64.decodeBase64(url.getBytes()));
		int num = (int)Math.ceil(5.80);
		System.out.print(num);
	}
}
