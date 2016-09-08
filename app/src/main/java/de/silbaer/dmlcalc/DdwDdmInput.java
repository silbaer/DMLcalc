package de.silbaer.dmlcalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class DdwDdmInput extends AppCompatActivity {

    public AutoCompleteTextView textDad;
    public AutoCompleteTextView textMom;
    public AutoCompleteTextView textDDW;
    DMLcalc dml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddw_ddm_input);



        //  dml = new DMLcalc("");
        textDad = (AutoCompleteTextView) findViewById(R.id.autoCompleteDad);
        textMom = (AutoCompleteTextView) findViewById(R.id.autoCompleteMom);
        textDDW = (AutoCompleteTextView) findViewById(R.id.autoCompleteDDW);

        dml = (DMLcalc) getApplicationContext();

        ArrayAdapter<Dragon> ad = new ArrayAdapter<Dragon>(this, R.layout.support_simple_spinner_dropdown_item);
        ad.addAll(dml.dragons.values());
        textDad.setAdapter(ad);
        textDad.setThreshold(1);

        textMom.setAdapter(ad);
        textMom.setThreshold(1);

        textDDW.setAdapter(ad);
        textDDW.setThreshold(1);

        textDDW.setOnItemClickListener(onDDWItemClick);
        textMom.setOnItemClickListener(onDDWMomItemClick);
        textDad.setOnItemClickListener(onDDWDadItemClick);


        textDad.requestFocus();
    }


    private AdapterView.OnItemClickListener onDDWItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getBaseContext(), "DDW Dragon:" + arg0.getItemAtPosition(arg2),
                    Toast.LENGTH_LONG).show();

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            dml.DDW = d.id;



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

            dml.DDW_Mom = d.id;




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

            dml.DDW_Dad = d.id;



        }
    };
}
