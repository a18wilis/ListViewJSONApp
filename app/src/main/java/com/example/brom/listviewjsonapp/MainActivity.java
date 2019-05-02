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
    private String[] mountainNames = {"Matterhorn","Mont Blanc","Denali", "Billingen"}; //Ifall man vill att nya berg ska skapas så får man inmata datan i de olika bergarrayerna
    private String[] mountainLocations = {"Alps","Alps","Alaska", "Sweden"};
    private int[] mountainHeights ={4478,4808,6190, 304};
    // Create ArrayLists from the raw data above and use these lists when populating your ListView.
    private ArrayList<String> listData = new ArrayList<>(Arrays.asList(mountainNames));
    private ArrayList<Mountain> mountainArrayList=new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lägger samman den inkluderade bergdatan och adderar dem till arraylistan om berg.
        for(int i = 0; i < mountainNames.length; i++){
            mountainArrayList.add(new Mountain(mountainNames[i],mountainLocations[i],mountainHeights[i]));
        }


        //Skapar en arrayadapter som lägger ihop ListViewen från list_item_textview och inställningarna från activity_main med datan som ska användas (ArrayAdaptern)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_textview, R.id.list_item_textview, listData);
        ListView my_listview=(ListView)findViewById(R.id.my_listview);
        my_listview.setAdapter(adapter);

        //Om man klickar på ett berg i listan så dyker en toast innehållande information om det berger upp på skärmen
        my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Hämtar informationen som skapas i Mountain-klasses och visar den som en toast
                Toast.makeText(getApplicationContext(), mountainArrayList.get(position).info(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}