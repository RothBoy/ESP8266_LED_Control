package de.axeldiewald.ESP8266_LED_Control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Axel on 01.09.2015.
 */
public class HttpGetRequest extends AsyncTask<Void, Void, Void> {

    // declare variables needed
    private String requestReply,ipAddress, portNumber;
    private Context context;
    private AlertDialog alertDialog;
    private String redValue, greenValue, blueValue;
    // Declare Settings
    SharedPreferences sharedPreferences;

    public HttpGetRequest(Context context, String param_redValue, String param_greenValue, String param_blueValue)
    {
        this.context = context;
        // Get Settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        alertDialog = new AlertDialog.Builder(this.context)
                .setTitle("HTTP Response From IP Address:")
                .setCancelable(true)
                .create();

        // Get IP Address and Port Number
        ipAddress = sharedPreferences.getString(SettingsActivity.PREF_IP, "");
        portNumber = sharedPreferences.getString(SettingsActivity.PREF_PORT, "");

        redValue = param_redValue;
        greenValue = param_greenValue;
        blueValue = param_blueValue;
    }

    /**
     * Name: doInBackground
     * Description: Sends the request to the ip address
     * @param voids
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {
        alertDialog.setMessage("Data sent, waiting for reply from server...");
        if(!alertDialog.isShowing())
        {
            alertDialog.show();
        }
        try {
            requestReply = sendRequest(redValue, greenValue, blueValue, ipAddress, portNumber);
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
        alertDialog.setMessage(requestReply);
        if(!alertDialog.isShowing())
        {
            alertDialog.show(); // show dialog
        }
    }

    /**
     * Name: onPreExecute
     * Description: This function is executed before the HTTP request is sent to ip address.
     * The function will set the dialog's message and display the dialog.
     */
    @Override
    protected void onPreExecute() {
        alertDialog.setMessage("Sending data to server, please wait...");
        if(!alertDialog.isShowing())
        {
            alertDialog.show();
        }
    }

    public String sendRequest(String redValue, String greenValue, String blueValue,
                              String ipAddress, String portNumber) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        String myUrl = "http://" + ipAddress + ":" + portNumber + "/?field1=" + redValue + "&field2=" + greenValue + "&field3=" + blueValue;

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