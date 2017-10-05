package de.silbaer.dmlcalc;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.support.v4.app.Fragment;


public class dragonlistActivity extends AppCompatActivity
implements dragonListFragment.OnListFragmentInteractionListener {

    private dragonListFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dradonlist);


        dragonListFragment frag = new dragonListFragment();
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dragonListFragment, frag);
        fragmentTransaction.commit();
    }
    @Override
    public  void  onListFragmentInteraction(Dragon itm){
        Toast.makeText(getBaseContext(), itm.toString() , Toast.LENGTH_SHORT).show();
//        Context c = getBaseContext();
//        Context c2 = getApplicationContext();
//        dragonListFragment dlf = (dragonListFragment) mList;
//        dlf.setList(DMLcalc.Instance().getDragonsToUse());
    }


}
