package de.axeldiewald.ESP8266_LED_Control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import de.axeldiewald.ESP8266_LED_Control.activity.SettingsActivity;

public class HttpGetRequest extends AsyncTask<Void, Void, Void> {

    // declare variables needed
    private String requestReply,ipAddress, portNumber;
    private Context context;
    private AlertDialog alertDialog;
    private Toast toast;
    private int[] values;
    private String path;
    // Declare Settings
    SharedPreferences sharedPreferences;

    public HttpGetRequest(Context context, int[] pArgs, String pPath)
    {
        this.context = context;
        // Get Settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                context.getApplicationContext());
        toast = Toast.makeText(this.context, "", Toast.LENGTH_SHORT);
        // Get IP Address and Port Number
        ipAddress = sharedPreferences.getString(SettingsActivity.PREF_IP, "");
        portNumber = sharedPreferences.getString(SettingsActivity.PREF_PORT, "");

        values = pArgs;
        path = pPath;
    }

    /**
     * Name: doInBackground
     * Description: Sends the request to the ip address
     * @param voids
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {
        toast.setText("Data sent");
        toast.show();
        try {
            requestReply = sendRequest();
        } catch (IOException e) {
            System.out.println("Unable to retrieve web page. URL may be invalid."+"Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Name: onPostExecute
     * Description: This function is executed after the HTTP request returns from the ip address.
     * The function sets the dialog's message with the reply text from the server and display the dialog
     * if it's not displayed already (in case it was closed by accident);
     * @param aVoid void parameter
     */
    @Override
    protected void onPostExecute(Void aVoid) {
            toast.setText(requestReply);
            toast.show();
    }

    /**
     * Name: onPreExecute
     * Description: This function is executed before the HTTP request is sent to ip address.
     * The function will set the dialog's message and display the dialog.
     */
    @Override
    protected void onPreExecute() {
            toast.setText("Sending Data");
            toast.show();
    }

    public String sendRequest() throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        // Build the URL
        int i = 1;
        String myUrl = "http://" + ipAddress + "/" + path + "/?";
        for (int value: values){
            myUrl += "value" + String.valueOf(i) + "=" + String.valueOf(value).trim();
            if (i != values.length){
                myUrl += "&";
            }
            i++;
        }

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}