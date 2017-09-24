package de.silbaer.dmlcalc;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.silbaer.dmlcalc.dummy.DummyContent;

public class dragonlistActivity extends AppCompatActivity
implements dragonListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dradonlist);
    }
    @Override
    public  void  onListFragmentInteraction(Dragon itm){
        //you can leave it empty
    }
}
