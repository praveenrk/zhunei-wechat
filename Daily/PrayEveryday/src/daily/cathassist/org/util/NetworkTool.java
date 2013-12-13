package daily.cathassist.org.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class NetworkTool {
	public static String getContent(String httpUrl) {
		String result = null;
		try {
			HttpGet httpget = new HttpGet(httpUrl);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();		
			InputStream is = null;
			is = entity.getContent();			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (Exception e) {
			Log.e("log_tag",
					"Error converting result " + e.toString());
		}
		return result;
	}
}
