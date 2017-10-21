package de.silbaer.dmlcalc;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.Fragment;


public class dragonlistActivity extends AppCompatActivity
implements dragonListFragment.OnListFragmentInteractionListener {

    private dragonListFragment frag;
    private EditText inputSearch;

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
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_dradonlist, container, false);
//        return view;
//    }



    @Override
    public  void  onListFragmentInteraction(Dragon itm){
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        IBinder b = v.getWindowToken();
        imm.hideSoftInputFromWindow(b, 0);
        Toast.makeText(getBaseContext(), itm.toString() , Toast.LENGTH_SHORT).show();
//        Context c = getBaseContext();
//        Context c2 = getApplicationContext();
//        dragonListFragment dlf = (dragonListFragment) mList;
//        dlf.setList(DMLcalc.Instance().getDragonsToUse());
    }


}
