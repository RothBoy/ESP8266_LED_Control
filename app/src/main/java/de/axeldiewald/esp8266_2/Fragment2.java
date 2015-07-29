package de.axeldiewald.esp8266_2;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment implements  View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    // declare buttons,TextViews and SeekBars
    private Button ButtonSend;
    private TextView textViewRedValue, textViewGreenValue, textViewBlueValue;
    private SeekBar seekBarRedValue, seekBarGreenValue, seekBarBlueValue;
    private TextView spaceColor;
    // declare Settings
    SharedPreferences sharedPreferences;

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        // Get Settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        // assign buttons & OnClickListener
        ButtonSend = (Button) view.findViewById(R.id.buttonSend);
        ButtonSend.setOnClickListener(this);
        // assign TextViews
        textViewRedValue =(TextView)view.findViewById(R.id.textViewCurRedValue);
        textViewGreenValue =(TextView)view.findViewById(R.id.textViewCurGreenValue);
        textViewBlueValue =(TextView)view.findViewById(R.id.textViewCurBlueValue);
        // assign SeekBars & OnSeekBarChangeListeners
        seekBarRedValue = (SeekBar)view.findViewById(R.id.seekBarRed);
        seekBarGreenValue = (SeekBar)view.findViewById(R.id.seekBarGreen);
        seekBarBlueValue = (SeekBar)view.findViewById(R.id.seekBarBlue);
        seekBarRedValue.setOnSeekBarChangeListener(this);
        seekBarGreenValue.setOnSeekBarChangeListener(this);
        seekBarBlueValue.setOnSeekBarChangeListener(this);
        // assign Space
        spaceColor = (TextView)view.findViewById(R.id.spaceShowColor);
        return view;
    }

    @Override
    public void onClick(View view){
        // get RGB Values
        String redValue = String.valueOf(seekBarRedValue.getProgress()).trim();
        String greenValue = String.valueOf(seekBarGreenValue.getProgress()).trim();;
        String blueValue = String.valueOf(seekBarBlueValue.getProgress()).trim();
        // get the ip address
        String ipAddress = sharedPreferences.getString(SettingsActivity.PREF_IP, "");
        // get the port number
        String portNumber = sharedPreferences.getString(SettingsActivity.PREF_PORT, "");


        // execute HTTP request
        if(ipAddress.length()>0 && portNumber.length()>0) {
            new HttpRequestAsyncTask(
                    view.getContext(), redValue, greenValue, blueValue, ipAddress, portNumber, "pin"
            ).execute();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textViewRedValue.setText(String.valueOf(seekBarRedValue.getProgress()));
        textViewGreenValue.setText(String.valueOf(seekBarGreenValue.getProgress()));
        textViewBlueValue.setText(String.valueOf(seekBarBlueValue.getProgress()));
        // set Preview Color Field
        int red_c = seekBarRedValue.getProgress()*256*256;
        int green_c = seekBarGreenValue.getProgress()*256;
        int blue_c = seekBarBlueValue.getProgress();
        int color_c = 255*256*256*256+red_c+green_c+blue_c;
        spaceColor.setBackgroundColor(color_c);

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

    private class HttpRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        // declare variables needed
        private String requestReply,ipAddress, portNumber;
        private Context context;
        private AlertDialog alertDialog;
        private String parameter;
        private String redValue, greenValue, blueValue;

        /**
         * Description: The asyncTask class constructor. Assigns the values used in its other methods.
         * @param context the application context, needed to create the dialog
         * @param blueValue the pin number to toggle
         * @param ipAddress the ip address to send the request to
         * @param portNumber the port number of the ip address
         */
        public HttpRequestAsyncTask(Context context, String redValue, String greenValue,
                                    String blueValue, String ipAddress, String portNumber,
                                    String parameter)
        {
            this.context = context;

            alertDialog = new AlertDialog.Builder(this.context)
                    .setTitle("HTTP Response From IP Address:")
                    .setCancelable(true)
                    .create();

            this.ipAddress = ipAddress;
            this.redValue = redValue;
            this.greenValue = greenValue;
            this.blueValue = blueValue;
            this.portNumber = portNumber;
            this.parameter = parameter;
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

    }



}
