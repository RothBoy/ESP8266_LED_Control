package de.axeldiewald.ESP8266_LED_Control;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    // declare variables needed
    private String requestReply,ipAddress, portNumber;
    private Context context;
    private AlertDialog alertDialog;
    private String parameter;
    private String redValue, greenValue, blueValue;

    public HttpRequestAsyncTask(Context context, String param_redValue, String param_greenValue,
                                String param_blueValue, String ipAddress, String portNumber,
                                String parameter)
    {
        this.context = context;

        alertDialog = new AlertDialog.Builder(this.context)
                .setTitle("HTTP Response From IP Address:")
                .setCancelable(true)
                .create();

        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        // color Strings must be 3 characters long
        String[] colorValues = {param_redValue, param_greenValue, param_blueValue};
        int i = 0;
        for (String colorValue_name : colorValues) {
            switch(colorValue_name.length()){
                case 2:
                    colorValue_name = "0" + colorValue_name;
                    break;
                case 1:
                    colorValue_name = "00" + colorValue_name;
                    break;
                default:
                    break;
            }
            colorValues[i] = colorValue_name;
            i += 1;
        }
        this.redValue = colorValues[0];
        this.greenValue = colorValues[1];
        this.blueValue = colorValues[2];
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
        requestReply = sendRequest(redValue, greenValue, blueValue, ipAddress, portNumber);
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
                              String ipAddress, String portNumber) {
        String serverResponse = "ERROR";

        try {

            HttpClient httpclient = new DefaultHttpClient(); // create an HTTP client
            // define the URL e.g. http://myIpaddress:myport/?pin=13 (to toggle pin 13 for example)

//            URI website = new URI("http://"+ipAddress+":"+portNumber+"/?"+parameterName+"="+parameterValue);
            //            URI website = new URI("https://api.thingspeak.com/update?key=5LGUCK3V08VW75PZ&field1="+redValue+"&field2="+greenValue+"&field3="+blueValue);
            URI website = new URI("http://"+ipAddress+":"+portNumber+"/?field1="+redValue+"&field2="+greenValue+"&field3="+blueValue);
//            URI website = new URI("https://api.thingspeak.com/update?key=5LGUCK3V08VW75PZ&field1=10");

            HttpGet getRequest = new HttpGet(); // create an HTTP GET object
            getRequest.setURI(website); // set the URL of the GET request
            HttpResponse response = httpclient.execute(getRequest); // execute the request
            // get the ip address server's reply
            InputStream content = null;
            content = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            serverResponse = in.readLine();
            // Close the connection
            content.close();
        } catch (ClientProtocolException e) {
            // HTTP error
            serverResponse = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            // IO error
            serverResponse = e.getMessage();
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // URL syntax error
            serverResponse = e.getMessage();
            e.printStackTrace();
        }
        // return the server's reply/response text
        return serverResponse;
    }

}



