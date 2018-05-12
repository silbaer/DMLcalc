package de.silbaer.dmlcalc;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class dragonlistActivity extends AppCompatActivity
implements dragonListFragment.OnListFragmentInteractionListener {

    private dragonListFragment frag;
    private EditText inputSearch;
    private List<String> elementFilter;
    private Map<String,ImageView> filterViews;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dragonlist_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent modifySettings=new Intent(dragonlistActivity.this,SettingsActivity.class);
            startActivityForResult(modifySettings,1);

            return true;
        }

//        if (id == R.id.action_ddw) {
//            startActivity(new Intent(this,ddwInputActivity.class));
//            return true;
//        }
//        if (id == R.id.action_ddm) {
//            startActivity(new Intent(this,ddmInputActivity.class));
//            return true;
//        }
//        if (id == R.id.action_dragonlist) {
//            startActivity(new Intent(this,dragonlistActivity.class));
//            return true;
//        }
//
//        if (id == R.id.action_ClearCache) {
//            DMLcalc.Instance().clearCache();
//            Toast.makeText(getBaseContext(), "Cache cleared",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        if (id == R.id.action_info) {
//
//            //         new AlertDialog.Builder(this).setTitle(R.string.action_info).setMessage("Version " + BuildConfig.VERSION_NAME + "\nAnregungen/Kritik/Wünsche an \nsilbaer@gmail.com" ).setNeutralButton("OK", null).show();
//
//            InfoDialogFragment newFragment = new InfoDialogFragment();
//            newFragment.show(getSupportFragmentManager(), "Info");
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart(){
        super.onStart();
//        InputMethodManager imm = (InputMethodManager) getSystemService(
//                INPUT_METHOD_SERVICE);
//        View v = getCurrentFocus();
//        IBinder b = v.getWindowToken();
//        imm.hideSoftInputFromWindow(b, 0);
        Window w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dradonlist);

        frag = new dragonListFragment();
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dragonListFragment, frag);
        fragmentTransaction.commit();

        inputSearch = (EditText) findViewById(R.id.nameFilter);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                frag.filterByName(s.toString());
            }
        });
        filterViews = new Hashtable<>() ;
        filterViews.put("fire", (ImageView) findViewById(R.id.FireFilterImageView));
        filterViews.put("wind", (ImageView) findViewById(R.id.WindFilterImageView));
        filterViews.put("water", (ImageView) findViewById(R.id.WaterFilterImageView));
        filterViews.put("earth", (ImageView) findViewById(R.id.EarthFilterImageView));
        filterViews.put("plant", (ImageView) findViewById(R.id.PlantFilterImageView));
        filterViews.put("metal", (ImageView) findViewById(R.id.MetalFilterImageView));
        filterViews.put("energy", (ImageView) findViewById(R.id.EnergyFilterImageView));
        filterViews.put("void", (ImageView) findViewById(R.id.VoidFilterImageView));
        filterViews.put("light", (ImageView) findViewById(R.id.LightFilterImageView));
        filterViews.put("shadow", (ImageView) findViewById(R.id.ShadowFilterImageView));
        filterViews.put("legendary", (ImageView) findViewById(R.id.LegendaryFilterImageView));
        filterViews.put("divine", (ImageView) findViewById(R.id.DivineFilterImageView));
        elementFilter =new ArrayList<>();


    }


    private void setFilter(String e){
        if(elementFilter.contains(e)){
            elementFilter.remove(e);
            filterViews.get(e).setImageResource(DMLcalc.Instance().getDrawableIdentifierByName("element_"+e+"_dis"));
            frag.filterRemoveElement(e);
        } else {
            elementFilter.add(e);
            filterViews.get(e).setImageResource(DMLcalc.Instance().getDrawableIdentifierByName("element_"+e));
            frag.filterAddElement(e);
        }
    }

    public void onClickFire(View v){
        setFilter("fire");
    }

    public void onClickWind(View v){
        setFilter("wind");
    }

    public void onClickWater(View v){
        setFilter("water");
    }

    public void onClickEarth(View v){
        setFilter("earth");
    }

    public void onClickPlant(View v){
        setFilter("plant");
    }

    public void onClickMetal(View v){
        setFilter("metal");
    }

    public void onClickEnergy(View v){
        setFilter("energy");
    }

    public void onClickVoid(View v){
        setFilter("void");
    }

    public void onClickLight(View v){
        setFilter("light");
    }

    public void onClickShadow(View v){
        setFilter("shadow");
    }

    public void onClickLegendary(View v){
        setFilter("legendary");
    }

    public void onClickDivine(View v){
        setFilter("divine");
    }



    @Override
    public  void  onListFragmentInteraction(Dragon itm){
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        IBinder b = v.getWindowToken();
        imm.hideSoftInputFromWindow(b, 0);
        Toast.makeText(getBaseContext(), itm.toString() , Toast.LENGTH_SHORT).show();
        if(DMLcalc.Instance().isBreedable(itm)) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("child", itm.getId());
            startActivity(i);
        }
//        Context c = getBaseContext();
//        Context c2 = getApplicationContext();
//        dragonListFragment dlf = (dragonListFragment) mList;
//        dlf.setList(DMLcalc.Instance().getDragonsToUse());
    }


}
