package com.microsoft.cognitiveservices.speech.samples.quickstart.backgroundservice;

import android.os.AsyncTask;
import android.util.Log;


import com.microsoft.cognitiveservices.speech.samples.quickstart.listener.OnBackgroundAsyncFinishListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class BackgroundHTTPPostAsync extends AsyncTask<String, Void, String> {

	// Response code for identify which function called this service.

	String jsonObject;
	OnBackgroundAsyncFinishListener onBckgrdAsyncFinishListener;
	String url;
	int responseCode;

	public BackgroundHTTPPostAsync(String jsonObject, String url,
								   OnBackgroundAsyncFinishListener onBckgrdAsyncFinishListener,
			int responseCode) {
		this.jsonObject = jsonObject;
		this.url = url;
		this.onBckgrdAsyncFinishListener = onBckgrdAsyncFinishListener;
		this.responseCode = responseCode;
	}

	@Override
	protected String doInBackground(String... params) {
		StringBuilder s = new StringBuilder();
		try {

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(url);
			postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Content-type", "application/json");
			postRequest.setHeader("Password", "");
			postRequest.setHeader("Username", "");
			postRequest.setHeader("Text", "what is your cvv number");

			postRequest.setEntity(new StringEntity(jsonObject));
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String sResponse;

			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			System.out.println("Response: " + s);
		} catch (Exception e) {
			// handle exception here
			Log.e(e.getClass().getName(), e.getMessage());
		}
		return s.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.e("Result", "" + result);

		onBckgrdAsyncFinishListener.onBackgroundFinish(result,
				responseCode);
	}

}
