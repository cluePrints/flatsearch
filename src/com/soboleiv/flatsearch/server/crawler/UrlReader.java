package com.soboleiv.flatsearch.server.crawler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.http.util.ByteArrayBuffer;

import com.google.common.io.Files;

public class UrlReader {
	public static final Charset CHARSET = Charset.forName("Windows-1251");

	private static boolean isDebugEnabled = false;
	
	public String readUrlContent(String urlStr) {
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			PrintWriter wr = new PrintWriter(conn.getOutputStream());
			wr.append("addSearch%5Bdt_id%5D=10&addSearch%5Bd_id%5D=0&addSearch%5Bt_id%5D=0&addSearch%5Bprice_min%5D=&addSearch%5Bprice_max%5D=&addSearch%5Bcurrency%5D=usd&addSearch%5BpriceFor%5D=0&addSearch%5Bfields%5D%5B384%5D%5Bmin%5D=0&addSearch%5Bfields%5D%5B384%5D%5Bmax%5D=0&addSearch%5Bfields%5D%5B388%5D%5Bmin%5D=&addSearch%5Bfields%5D%5B388%5D%5Bmax%5D=&addSearch%5Bfields%5D%5B390%5D%5Bmin%5D=&addSearch%5Bfields%5D%5B390%5D%5Bmax%5D=&addSearch%5Bphone%5D=&addSearch%5Bdays%5D=&fsbmtfReg=%CF%EE%E8%F1%EA");
			wr.flush();
			wr.close();
			InputStream stream = conn.getInputStream();

			String content = toString(stream);

			debugLogFetchedFile(urlStr, content);
			return content;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static UrlReader cacheForDays(int days) {
		return new CachedUrlReader(new UrlReader());
	}
	
	private String toString(InputStream stream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);

		ByteArrayBuffer baf = new ByteArrayBuffer(5000);
		int current = 0;
		while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		}
		return new String(baf.toByteArray(), CHARSET);
	}
	
	private void debugLogFetchedFile(String url, String res)
			throws IOException {
		if (isDebugEnabled) {
			String name = "1.html";
			System.out.println("Dumping to log: " + name);
			Files.write(res, new File(name), CHARSET);
		}
	}

}
