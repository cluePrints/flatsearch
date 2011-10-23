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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public class UrlReader {
	private static Logger log = LoggerFactory.getLogger(Crawler.class);

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

			Charset charset = detectCharset(conn);
			String content = toString(stream, charset);

			return content;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static Charset detectCharset(URLConnection conn) {
		String contentTypeStr = conn.getHeaderField("Content-Type");
		String charsetToken = "charset=";
		int startingPos = contentTypeStr.indexOf(charsetToken)
				+ charsetToken.length();
		String encoding = contentTypeStr.substring(startingPos);
		log.debug("Detected character set: " + encoding);
		Charset charset = Charset.forName(encoding);
		return charset;
	}

	public static UrlReader cacheForDays(int days) {
		return new CachedUrlReader(new UrlReader());
	}

	private String toString(InputStream stream, Charset charset) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);

		ByteArrayBuffer baf = new ByteArrayBuffer(5000);
		int current = 0;
		while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		}
		return new String(baf.toByteArray(), charset);
	}
}
