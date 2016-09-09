package de.silbaer.dmlcalc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DMLcalc dml;
    private static final int RQS_OPEN_IMAGE = 1;


    public AutoCompleteTextView textDad;
    public AutoCompleteTextView textMom;
    public AutoCompleteTextView textChild;
    Dragon mom = null;
    Dragon dad = null;
    Dragon child = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //    txtContent = (TextView) findViewById(R.id.textView);


        //  dml = new DMLcalc("");
        textDad = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextDad);
        textMom = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextMom);
        textChild = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextChild);




        try {

            dml = (DMLcalc) getApplicationContext();

            ArrayAdapter<Dragon> breed = new ArrayAdapter<Dragon>(this, R.layout.support_simple_spinner_dropdown_item);
            ArrayAdapter<Dragon> howTo = new ArrayAdapter<Dragon>(this, R.layout.support_simple_spinner_dropdown_item);
            //      dml.dragons.values()
            howTo.addAll(dml.dragons.values());
            breed.addAll(dml.dragons.values());
            for(int i = breed.getCount()-1; i >= 0; i--){
                Dragon d = breed.getItem(i);
                if(d.isBoss() || d.islegendary()){
                    breed.remove(d);
                }
            }

         //   spinnerDad.setAdapter(adapter);
           // spinnerMom.setAdapter(adapter);

            textDad.setAdapter(breed);
            textDad.setThreshold(1);

            textMom.setAdapter(breed);
            textMom.setThreshold(1);

            textChild.setAdapter(howTo);
            textChild.setThreshold(1);

            //         textMom.setOnItemSelectedListener(this);
     //       textDad.setOnItemSelectedListener(this);

            textDad.setOnItemClickListener(onDadItemClick);
            textMom.setOnItemClickListener(onMomItemClick);
            textChild.setOnItemClickListener(onChildItemClick);
            textDad.requestFocus();

            ImageView iv = (ImageView) findViewById(R.id.iv_ddm_e1);
            iv.setImageResource(R.drawable.element_earth);

            iv = (ImageView) findViewById(R.id.iv_ddm_e2);
            iv.setImageResource(R.drawable.element_energy);

            iv = (ImageView) findViewById(R.id.iv_ddm_e3);
            iv.setImageResource(R.drawable.element_shadow);

            iv = (ImageView) findViewById(R.id.iv_ddm_e4);
            iv.setImageResource(R.drawable.element_plant);

            ((TextView)findViewById(R.id.textViewDDM)).setText("MyDragon");


        } catch (Exception e) {
            e.printStackTrace();
        }


      //n  ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);




        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private AdapterView.OnItemClickListener onChildItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "Child Dragon:" + arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            child = (Dragon) arg0.getItemAtPosition(arg2);

            displayHowToResult();


        }
    };

    public void onDDWClick(View v){
        Toast.makeText(getBaseContext(), "onDDWClick",
                Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(this, DdwDdmInput.class);
        startActivity(myIntent);
    }

    private void displayHowToResult(){
        List<Pair<Pair<Dragon,Dragon>,Double>> htb = dml.howToBreed(child);


//        StringBuilder sb = new StringBuilder();
//        for (Dragon d : result) {
//            sb.append(d.lng_de + " " + String.format("%.1f",d.odd) + "\n");
//        }
//        if (!(sb.length() > 0)) {
//            sb.append("No result");
//        }
//        txtContent.setText(sb.toString());

        ListView l = (ListView) findViewById(R.id.listView);
        howToItemAdapter a = new howToItemAdapter(this,htb);
        l.setAdapter(a);
        l.setOnItemClickListener(a);

    }

    private AdapterView.OnItemClickListener onDadItemClick = new AdapterView.OnItemClickListener(){

        public void  onItemClick (AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "Dad Dragon:"+arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            dad = (Dragon) arg0.getItemAtPosition(arg2);
            if(dad != null && mom != null) {
                displayBreedResult();
            }
        }
    };

    public void displayBreedResult(Dragon mom, Dragon dad){
        this.mom = mom;
        this.dad = dad;
        textDad.setText(dad.lng_de);
        textMom.setText(mom.lng_de);
        displayBreedResult();
    }

    private void displayBreedResult(){
        List<Dragon> result = new ArrayList<Dragon>();

        result = dml.breed(dad, mom);
//        StringBuilder sb = new StringBuilder();
//        for (Dragon d : result) {
//            sb.append(d.lng_de + " " + String.format("%.1f",d.odd) + "\n");
//        }
//        if (!(sb.length() > 0)) {
//            sb.append("No result");
//        }
//        txtContent.setText(sb.toString());

        ListView l = (ListView) findViewById(R.id.listView);
        breedListItemAdapter a = new breedListItemAdapter(this,result);
        l.setAdapter(a);
        l.setOnItemClickListener(a);

    }

    private AdapterView.OnItemClickListener onMomItemClick = new AdapterView.OnItemClickListener(){

        public void  onItemClick (AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "Mom  Dragon:" + arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            mom = (Dragon) arg0.getItemAtPosition(arg2);
            if (dad != null && mom != null) {
                displayBreedResult();
            }
        }
    };



    public void onBreedClick(View v) {
        textDad.setText(dad.lng_de);
        textMom.setText(mom.lng_de);
        displayBreedResult();
    }
    public void onHowToClick(View v) {
        textChild.setText(child.lng_de);
        displayHowToResult();
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, RQS_OPEN_IMAGE);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://de.silbaer.dmlcalc/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);

       String ddw = dml.getDDW();
        if(!ddw.isEmpty()){
            TextView tv = (TextView) findViewById(R.id.textViewDDW);
            tv.setText(String.format("%s",dml.dragons.get(ddw).lng_de));
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://de.silbaer.dmlcalc/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
