package com.example.brom.listviewjsonapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


// Create a new class, Mountain, that can hold your JSON data

// Create a ListView as in "Assignment 1 - Toast and ListView"

// Retrieve data from Internet service using AsyncTask and the included networking code

// Parse the retrieved JSON and update the ListView adapter

// Implement a "refresh" functionality using Android's menu system


public class MainActivity extends AppCompatActivity {
    private String[] mountainNames = {""};  //Strängarray med bergnamn som fylls utifrån
    private String[] mountainLocations = {""}; //Strängarray med bergplatser som fylls utifrån
    private int[] mountainHeights ={4478,4808,6190, 304}; //Heltalsarray med berghöjder som fylls utifrån
    // Create ArrayLists from the raw data above and use these lists when populating your ListView.

    private ArrayList<String> listData;
    private ArrayAdapter<Mountain> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new FetchData().execute();

        //Skapar en arrayadapter för bergdatan
        adapter = new ArrayAdapter<Mountain>(this,R.layout.list_item_textview,R.id.list_item_textview);
        ListView my_listview=(ListView)findViewById(R.id.my_listview);
        my_listview.setAdapter(adapter);

        //Om man klickar på ett berg i listan så dyker en toast innehållande information om det berger upp på skärmen
        my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Hämtar informationen som skapas i Mountain-klasses och visar den som en toast
                Toast.makeText(getApplicationContext(), adapter.getItem(position).info(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ //Skapar funktionalitet till menyknappen
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id == R.id.action_refresh){
            adapter.clear(); //Rensar listviewen
            new FetchData().execute(); //Refreshar listviewen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchData extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Log.d("brom","DataFetched:"+o);
            // This code executes after we have received our data. The String object o holds
            // the un-parsed JSON string or is null if we had an IOException during the fetch.
            try {

                JSONArray williamArr= new JSONArray(o);
                for(int i=0; i < williamArr.length(); i++) {
                    Log.d("brom", "element 0:" + williamArr.get(i).toString());
                    JSONObject william = williamArr.getJSONObject(i);
                    Log.d("brom",  william.getString("name"));
                    Log.d("brom",  william.getString("location"));
                    Log.d("brom",  ""+william.getInt("size"));

                    Mountain mountain = new Mountain(william.getString("name"),william.getString("location"),william.getInt("size") ); //Skapar nytt berg med datan som hämtas från länken
                    Log.d("brom",  mountain.toString());
                    adapter.add(mountain); //Det nya berget läggs till i adaptern
                }
            } catch (JSONException e) {
                Log.e("brom","E:"+e.getMessage());
            }

            // Implement a parsing code that loops through the entire JSON and creates objects
            // of our newly created Mountain class.
        }
    }

}