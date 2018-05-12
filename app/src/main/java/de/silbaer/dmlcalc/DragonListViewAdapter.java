package de.silbaer.dmlcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.silbaer.dmlcalc.dragonListFragment.OnListFragmentInteractionListener;


public class DragonListViewAdapter extends RecyclerView.Adapter<DragonListViewAdapter.ViewHolder> {

    private  List<Dragon> mValues;
    private final OnListFragmentInteractionListener mListener;
    private  Context myContext;

    Boolean vipTimes = false;
    private   String PREFS_NAME;


    public DragonListViewAdapter(List<Dragon> items, OnListFragmentInteractionListener listener ) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dragon, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        PREFS_NAME =  myContext.getResources().getString(R.string.PREFS_NAME);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(myContext);
        vipTimes = sharedPref.getBoolean("pref_timedisplay",false);
        holder.mItem = mValues.get(position);


        int picSize = DMLcalc.convertSpToPixels(72,myContext);;
        TextView tv;
        int i24sp = DMLcalc.convertSpToPixels(24,myContext);;;
        tv = (TextView) holder.mView.findViewById(R.id.textStats);
        if(!DMLcalc.Instance().ShowStatsInList()) {
            tv.setHeight(0);
            picSize = picSize - i24sp;
        } else {
            tv.setHeight(i24sp);
        }
        tv = (TextView) holder.mView.findViewById(R.id.textStats2);
        if(!DMLcalc.Instance().ShowTimesInList()) {
            tv.setHeight(0);
            picSize = picSize - i24sp;
        } else {
            tv.setHeight(i24sp);
        }
        ImageView iv = (ImageView) holder.mView.findViewById(R.id.dragonPic);

        iv.setMaxHeight(picSize);
        iv.setMinimumHeight(picSize);
        iv.setMaxWidth(picSize);
        iv.setMinimumWidth(picSize);



//        holder.mIdView.setText(mValues.get(position).getId());
//        holder.mContentView.setText(mValues.get(position).toString());
//        iv.setImageResource(R.drawable.element_earth);
       // holder.mDragonImage.setImageResource(R.drawable.element_boss);
        if (position % 2 == 0) {
            holder.mView.setBackgroundColor(ResourcesCompat.getColor(myContext.getResources(), R.color.colorListEven, null));
        } else {
            holder.mView.setBackgroundColor(ResourcesCompat.getColor(myContext.getResources(), R.color.colorListOdd, null));
        }

        DMLcalc.Instance().loadDragonIcon(holder.mItem.getId(),holder.mDragonImage);


        holder.mDragonName.setText(holder.mItem.toString().replace("+","").replace("&","").replace("*",""));
        setElement(holder.mElement1,holder.mItem.getElement1());
        setElement(holder.mElement2,holder.mItem.getElement2());
        setElement(holder.mElement3,holder.mItem.getElement3());
        holder.mDragonStats.setText(holder.mItem.getBaseHealth() + " / " + holder.mItem.getBaseAttack() + " / " + holder.mItem.getBaseGold() );
        holder.mDragonStats2.setText(getDragonText(holder.mItem));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private String getDragonText(Dragon d) {
        StringBuilder sb = new StringBuilder();
        int tBreed ;
        int tHatch ;
        int bDay;
        int bHour;
        int bMin;
        int bSec;
        int hDay;
        int hHour;
        int hMin;
        int hSec;
        try {
//            sb.append(d.toString());
//            sb.append(" (");
            sb.append("B: ");
            sb.append(DMLcalc.Instance().getTimeString(d.getBreedingTime(vipTimes)));

        } catch (Exception e) {
            sb.append("N/A");
            e.printStackTrace();
        }
        try {
            sb.append(" / H: ");

            sb.append(DMLcalc.Instance().getTimeString(d.getHatchingTime(vipTimes)));


        } catch (Exception e) {
            sb.append("N/A");
            e.printStackTrace();
        }
//        sb.append(")");


        return sb.toString();
    }



    private void setElement(ImageView iv, String element){
        if(element == null){
            iv.setImageDrawable(null);
            return;
        }
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
    public int getItemCount() {
        return mValues.size();
    }

    public void updateList(List<Dragon> list){
        mValues = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public Dragon mItem;
        public final ImageView mDragonImage;
        public final TextView mDragonName;
        public final ImageView mElement1;
        public final ImageView mElement2;
        public final ImageView mElement3;
        public final TextView mDragonStats;
        public final TextView mDragonStats2;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDragonImage = (ImageView) view.findViewById(R.id.dragonPic);
            mDragonName = (TextView) view.findViewById(R.id.textDragonName);
            mElement1 = (ImageView) view.findViewById(R.id.ele1);
            mElement2 = (ImageView) view.findViewById(R.id.ele2);
            mElement3 = (ImageView) view.findViewById(R.id.ele3);
            mDragonStats = (TextView) view.findViewById(R.id.textStats);
            mDragonStats2 = (TextView) view.findViewById(R.id.textStats2);
        }

 //       @Override
 //       public String toString() {
 //           return super.toString() + " '" + mContentView.getText() + "'";
 //       }
    }
}
