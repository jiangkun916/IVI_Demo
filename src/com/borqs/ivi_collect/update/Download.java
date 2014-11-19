package com.borqs.ivi_collect.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

public class Download extends IntentService {

	private final static String TAG = "Download";
	String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/download_cache";
	String url = "http://192.168.1.123/oa/apk/action.apk";
	File file;
	public Download() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					File file = new File(downloadPath);
					if (!file.exists())
						file.mkdir();
					HttpGet httpGet = new HttpGet(url);
					HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						
						InputStream is = httpResponse.getEntity().getContent();
						FileOutputStream fos = new FileOutputStream(downloadPath + "/action.apk");
						byte[] buffer = new byte[8192];
						int count = 0;
						while ((count = is.read(buffer)) != -1) {
							fos.write(buffer, 0, count);
						}
						fos.close();
						is.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

    private final String URL_STRING = "http://gdown.baidu.com/data/wisegame/b7d7e4efd8199dea/tianyiyuedu_310.apk";
    
    
	private File downFile(final String httpUrl) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(httpUrl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					// connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					FileOutputStream fileOutputStream = null;
					if (connection.getResponseCode() == 200) {
						InputStream inputStream = connection.getInputStream();

						if (inputStream != null) {
							file = getFile(httpUrl);
							fileOutputStream = new FileOutputStream(file);
							byte[] buffer = new byte[1024];
							int length = 0;

							while ((length = inputStream.read(buffer)) != -1) {
								fileOutputStream.write(buffer, 0, length);
							}
							fileOutputStream.close();
							fileOutputStream.flush();
						}
						inputStream.close();
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return file;
	}

	/**
	 * Create a file based on url pass over
	 * 
	 */
	private File getFile(String url) {
		File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getFilePath(url));
		return files;
	}

	/**
	 * Cut out behind url apk file name
	 * 
	 * @param url
	 * @return
	 */
	private String getFilePath(String url) {
		return url.substring(url.lastIndexOf("/"), url.length());
	}
	
	

}
