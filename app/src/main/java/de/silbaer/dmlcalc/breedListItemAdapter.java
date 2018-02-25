package de.silbaer.dmlcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by silbaer on 04.09.16.
 */
public class breedListItemAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private final LayoutInflater mInflater;
    private List<Pair<Dragon,Double>> items;
    private final Context context;
    Boolean vipTimes = false;
    private   String PREFS_NAME;

    public class DragonOddComparator implements Comparator<Pair<Dragon,Double>> {
        @Override
        public int compare(Pair<Dragon,Double> o1, Pair<Dragon,Double> o2) {
            if(o1.second < o2.second){
                return 1;
            } else if(o1.second > o2.second){
              return -1;
            } else {
            return 0;
            }

        }
    }


    public breedListItemAdapter (Context context, List<Pair<Dragon,Double>> dragons) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = dragons;
        Collections.sort(items, new DragonOddComparator());
        this.context = context;
        PREFS_NAME =  context.getResources().getString(R.string.PREFS_NAME);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        vipTimes = sharedPref.getBoolean("pref_timedisplay",false);

    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout itemView = (LinearLayout) mInflater.inflate(R.layout.list_breedresult, parent, false);
        bindView(itemView, position);
        return itemView;
    }

    private void bindView(LinearLayout view, int position) {
        Pair<Dragon,Double> d = (Pair<Dragon,Double>) getItem(position);
        view.setId((int) getItemId(position));

        if (position % 2 == 0) {
            view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorListEven, null));
        } else {
            view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorListOdd, null));
        }


        TextView dragonTextView = (TextView) view.findViewById(R.id.textDragonName);
        TextView timeTextView = (TextView) view.findViewById(R.id.textTimes);

        TextView oddTextView = (TextView) view.findViewById(R.id.textOdd);

        String dt = getDragonText(d.first);
        dragonTextView.setText(d.first.toString());
        timeTextView.setText(dt);
        oddTextView.setText(String.format("%.1f%%",d.second));

        String tmp;
        ImageView iv = (ImageView) view.findViewById(R.id.ele1);
        tmp = d.first.getElement1();
        setElement(iv,tmp);
        iv = (ImageView) view.findViewById(R.id.ele2);
        tmp = d.first.getElement2();
        setElement(iv,tmp);
        iv = (ImageView) view.findViewById(R.id.ele3);
        tmp = d.first.getElement3();
        setElement(iv,tmp);

        ImageView mDragonImage = (ImageView) view.findViewById(R.id.dragonPic);
//        String resName = d.first.getId() + "_icon" ;
//        Resources resources = context.getResources();
//        final int resourceId = resources.getIdentifier(resName, "drawable", context.getPackageName());
//        mDragonImage.setImageResource( resourceId);

//        mDragonImage.setImageDrawable(DMLcalc.Instance().getDragonIcon(d.first));
        DMLcalc.Instance().loadDragonIcon(d.first.getId(),mDragonImage);


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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {

            Pair<Dragon,Double> d = items.get(position);

            if(context instanceof MainActivity){
                ((MainActivity)context).displayHowToResult(d.first);
            }

        } catch (Exception e) {
           // e.printStackTrace();
        }
    }
}
