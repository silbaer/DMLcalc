package de.silbaer.dmlcalc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity implements DMLcalc.howToResponse, DMLcalc.breedingResponse {

    DMLcalc dml;

//    Boolean vipDragons;
    static final int RQS_Open_Settings = 1;  // The request code



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
            Intent modifySettings=new Intent(MainActivity.this,SettingsActivity.class);
            startActivityForResult(modifySettings,RQS_Open_Settings);

            return true;
        }

        if (id == R.id.action_ddw) {
            startActivity(new Intent(this,ddwInputActivity.class));
            return true;
        }
        if (id == R.id.action_ddm) {
            startActivity(new Intent(this,ddmInputActivity.class));
            return true;
        }

        if (id == R.id.action_ClearCache) {
            DMLcalc.Instance().clearCache();
            Toast.makeText(getBaseContext(), "Cache cleared",
                    Toast.LENGTH_LONG).show();
        }

        if (id == R.id.action_info) {

   //         new AlertDialog.Builder(this).setTitle(R.string.action_info).setMessage("Version " + BuildConfig.VERSION_NAME + "\nAnregungen/Kritik/Wünsche an \nsilbaer@gmail.com" ).setNeutralButton("OK", null).show();

            InfoDialogFragment newFragment = new InfoDialogFragment();
            newFragment.show(getSupportFragmentManager(), "Info");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RQS_Open_Settings) {
            updateArrayAdapter();
        }
    }

    private void updateArrayAdapter(){
        ArrayAdapter<Dragon> breed = new ArrayAdapter<Dragon>(this, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<Dragon> howTo = new ArrayAdapter<Dragon>(this, R.layout.support_simple_spinner_dropdown_item);
        //      dml.dragons.values()
        howTo.addAll(dml.getDragonsToShow());
        breed.addAll(dml.getDragonsToUse());



        //   spinnerDad.setAdapter(adapter);
        // spinnerMom.setAdapter(adapter);

        textDad.setAdapter(breed);
        textDad.setThreshold(1);

        textMom.setAdapter(breed);
        textMom.setThreshold(1);

        textChild.setAdapter(howTo);
        textChild.setThreshold(1);


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

            dml = DMLcalc.Instance();

            updateArrayAdapter();

            textDad.setOnItemClickListener(onDadItemClick);
            textMom.setOnItemClickListener(onMomItemClick);
            textChild.setOnItemClickListener(onChildItemClick);
            textDad.requestFocus();


            TextView tv = (TextView) findViewById(R.id.textViewDDMLabel);
            int h = tv.getLayoutParams().height;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(h, h);



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

    public void onDDMClick(View v) {
        String ds = dml.getDDM();
        if(!ds.isEmpty()){
            Dragon d = dml.dragons.get(ds);
            if(d != null){
                displayHowToResult(d);
            }
        }
    }
    public void onDDWClick(View v) {
        String ds = dml.getDDW();
        if(!ds.isEmpty()){
            Dragon d = dml.dragons.get(ds);
            if(d != null){
                displayHowToResult(d);
            }
        }
    }

    private void displayHowToResult(){

        dml.howToBreed(this,child);
    }

    public void howToResult(List<Pair<Pair<Dragon,Dragon>,Double>> result){
        ListView l = (ListView) findViewById(R.id.listView);
        howToItemAdapter a = new howToItemAdapter(this, result);
        l.setAdapter(a);
        l.setOnItemClickListener(a);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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

    public void displayHowToResult(Dragon child) {
        try {
            this.child = child;
            textChild.setText(child.toString());
            ArrayAdapter<Dragon> ad = (ArrayAdapter) textChild.getAdapter();
            int i = ad.getPosition(child);
            textChild.setListSelection(i);
            displayHowToResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayBreedResult(Dragon mom, Dragon dad) {
        try {
            this.mom = mom;
            this.dad = dad;
            textMom.setText(mom.toString());
            textDad.setText(dad.toString());
            ArrayAdapter<Dragon> ad = (ArrayAdapter) textDad.getAdapter();
            int i = ad.getPosition(dad);
            textDad.setListSelection(i);
            ad = (ArrayAdapter) textMom.getAdapter();
            i = ad.getPosition(dad);
            textMom.setListSelection(i);
            displayBreedResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void breedingResult(List<Pair<Dragon,Double>> result){
        ListView l = (ListView) findViewById(R.id.listView);
        breedListItemAdapter a = new breedListItemAdapter(this,result);
        l.setAdapter(a);
        l.setOnItemClickListener(a);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void displayBreedResult(){

        dml.breed(this,dad, mom);

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
        displayBreedResult(mom,dad);
    }
    public void onHowToClick(View v) {

        displayHowToResult(child);
    }


    @Override
    protected void onPause() {
        super.onPause();
        DMLcalc.Instance().saveCache();
    }

    @Override
    public void onResume() {
        super.onResume();


        String tmp = dml.getDDW();
        if(!tmp.isEmpty()){
            TextView tv = (TextView) findViewById(R.id.textViewDDW);
            tv.setText(String.format("%s",dml.dragons.get(tmp).toString()));
        }
        tmp = dml.getDDM();
        if(!tmp.isEmpty()){
            TextView tv = (TextView) findViewById(R.id.textViewDDM);
            tv.setText(String.format("%s",dml.dragons.get(tmp).toString()));

            ImageView iv = (ImageView) findViewById(R.id.iv_ddm_e1);
            tmp = dml.getDDM_e1();
            setElement(iv,tmp);
            iv = (ImageView) findViewById(R.id.iv_ddm_e2);
            tmp = dml.getDDM_e2();
            setElement(iv,tmp);
            iv = (ImageView) findViewById(R.id.iv_ddm_e3);
            tmp = dml.getDDM_e3();
            setElement(iv,tmp);
            iv = (ImageView) findViewById(R.id.iv_ddm_e4);
            tmp = dml.getDDM_e4();
            setElement(iv,tmp);
        }
    }




    private void setElement(ImageView iv, String element){
        if(element.equalsIgnoreCase("earth")){
            iv.setImageResource(R.drawable.element_earth);
        }
        if(element.equalsIgnoreCase("energy")){
            iv.setImageResource(R.drawable.element_energy);
        }
        if(element.equalsIgnoreCase("fire")){
            iv.setImageResource(R.drawable.element_fire);
        }
        if(element.equalsIgnoreCase("legendary")){
            iv.setImageResource(R.drawable.element_legendary);
        }
        if(element.equalsIgnoreCase("light")){
            iv.setImageResource(R.drawable.element_light);
        }
        if(element.equalsIgnoreCase("metal")){
            iv.setImageResource(R.drawable.element_metal);
        }
        if(element.equalsIgnoreCase("plant")){
            iv.setImageResource(R.drawable.element_plant);
        }
        if(element.equalsIgnoreCase("shadow")){
            iv.setImageResource(R.drawable.element_shadow);
        }
        if(element.equalsIgnoreCase("void")){
            iv.setImageResource(R.drawable.element_void);
        }
        if(element.equalsIgnoreCase("water")){
            iv.setImageResource(R.drawable.element_water);
        }
        if(element.equalsIgnoreCase("wind")){
            iv.setImageResource(R.drawable.element_wind);
        }
        if(element.equalsIgnoreCase("divine")){
            iv.setImageResource(R.drawable.element_divine);
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
