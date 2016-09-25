package de.silbaer.dmlcalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

public class DdwDdmInput extends AppCompatActivity {

    public AutoCompleteTextView textDad;
    public AutoCompleteTextView textMom;
    public AutoCompleteTextView textDDW;
    public AutoCompleteTextView textDDM;

    public AutoCompleteTextView textE1;
    public AutoCompleteTextView textE2;
    public AutoCompleteTextView textE3;
    public AutoCompleteTextView textE4;

    public ImageView ddw_e1;
    DMLcalc dml;

    private String DDW;
    private String DDW_mom;
    private String DDW_dad;

    private String DDM;
    private String DDM_e1;
    private String DDM_e2;
    private String DDM_e3;
    private String DDM_e4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddw_ddm_input);



        //  dml = new DMLcalc("");
        textDad = (AutoCompleteTextView) findViewById(R.id.autoCompleteDad);
        textMom = (AutoCompleteTextView) findViewById(R.id.autoCompleteMom);
        textDDW = (AutoCompleteTextView) findViewById(R.id.autoCompleteDDW);
        textDDM = (AutoCompleteTextView) findViewById(R.id.autoCompleteDDM);

        textE1 = (AutoCompleteTextView) findViewById(R.id.ac_ddmElement1);
        textE2 = (AutoCompleteTextView) findViewById(R.id.ac_ddmElement2);
        textE3 = (AutoCompleteTextView) findViewById(R.id.ac_ddmElement3);
        textE4 = (AutoCompleteTextView) findViewById(R.id.ac_ddmElement4);

        dml = (DMLcalc) getApplicationContext();

        ArrayAdapter<Dragon> ad = new ArrayAdapter<Dragon>(this, R.layout.support_simple_spinner_dropdown_item);
        ad.addAll(dml.dragons.values());
        textDad.setAdapter(ad);
        textDad.setThreshold(1);

        textMom.setAdapter(ad);
        textMom.setThreshold(1);

        textDDW.setAdapter(ad);
        textDDW.setThreshold(1);

        textDDM.setAdapter(ad);
        textDDM.setThreshold(1);

        textDDW.setOnItemClickListener(onDDWItemClick);
        textMom.setOnItemClickListener(onDDWMomItemClick);
        textDad.setOnItemClickListener(onDDWDadItemClick);
        textDDM.setOnItemClickListener(onDDMItemClick);

        DDW = dml.getDDW();
        DDW_dad = dml.getDDW_dad();
        DDW_mom = dml.getDDW_mom();

        Dragon d;

        d = dml.dragons.get(DDW);
        if(d != null) {
            textDDW.setText(d.lng_de);
        }

        d = dml.dragons.get(DDW_mom);
        if(d != null) {
            textMom.setText(d.lng_de);
        }

        d = dml.dragons.get(DDW_dad);
        if(d != null) {
            textDad.setText(d.lng_de);
        }

        ArrayAdapter<String> ade = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);

        ade.addAll(dml.elements);
        textE1.setAdapter(ade);
        textE1.setThreshold(1);
        textE1.setOnItemClickListener(onElement1ItemClick);

        textE2.setAdapter(ade);
        textE2.setThreshold(1);
        textE2.setOnItemClickListener(onElement2ItemClick);

        textE3.setAdapter(ade);
        textE3.setThreshold(1);
        textE3.setOnItemClickListener(onElement3ItemClick);

        textE4.setAdapter(ade);
        textE4.setThreshold(1);
        textE4.setOnItemClickListener(onElement4ItemClick);

        DDM = dml.getDDM();
        DDM_e1 = dml.getDDM_e1();
        DDM_e2 = dml.getDDM_e2();
        DDM_e3 = dml.getDDM_e3();
        DDM_e4 = dml.getDDM_e4();

        d = dml.dragons.get(DDM);
        if(d != null) {
            textDDM.setText(d.lng_de);
        }
        textE1.setText(DDM_e1);
        textE2.setText(DDM_e2);
        textE3.setText(DDM_e3);
        textE4.setText(DDM_e4);

    }



    private AdapterView.OnItemClickListener onElement1ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            DDM_e1 = (String) arg0.getItemAtPosition(arg2);


            dml.setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };
    private AdapterView.OnItemClickListener onElement2ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            DDM_e2 = (String) arg0.getItemAtPosition(arg2);
            dml.setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };
    private AdapterView.OnItemClickListener onElement3ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            DDM_e3 = (String) arg0.getItemAtPosition(arg2);
            dml.setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };
    private AdapterView.OnItemClickListener onElement4ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            DDM_e4 = (String) arg0.getItemAtPosition(arg2);
            dml.setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };



    private AdapterView.OnItemClickListener onDDMItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "DDM Dragon:" + arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDM = d.id;

            dml.setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };



    private AdapterView.OnItemClickListener onDDWItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "DDW Dragon:" + arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDW = d.id;
            dml.setDDW(DDW,DDW_mom,DDW_dad);

        }
    };
    private AdapterView.OnItemClickListener onDDWMomItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "DDW Mom:" + arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDW_mom = d.id;
            dml.setDDW(DDW,DDW_mom,DDW_dad);
        }
    };
    private AdapterView.OnItemClickListener onDDWDadItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "DDW Dad:" + arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDW_dad = d.id;
            dml.setDDW(DDW,DDW_mom,DDW_dad);
        }
    };
}
