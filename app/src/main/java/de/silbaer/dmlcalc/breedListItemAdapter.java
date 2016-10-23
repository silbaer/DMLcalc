package de.silbaer.dmlcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by silbaer on 04.09.16.
 */
public class breedListItemAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private final LayoutInflater mInflater;
    private List<Dragon> items;
    private final Context context;
    Boolean vipTimes = false;
    private   String PREFS_NAME;

    public class DragonOddComparator implements Comparator<Dragon> {
        @Override
        public int compare(Dragon o1, Dragon o2) {
            if(o1.odd < o2.odd){
                return 1;
            } else if(o1.odd > o2.odd){
              return -1;
            } else {
            return 0;
            }

        }
    }


    public breedListItemAdapter (Context context, List<Dragon> dragons) {
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
        LinearLayout itemView = (LinearLayout) mInflater.inflate(R.layout.breed_list_item, parent, false);
        bindView(itemView, position);
        return itemView;
    }

    private void bindView(LinearLayout view, int position) {
        Dragon d = (Dragon) getItem(position);
        view.setId((int) getItemId(position));
        TextView dragonTextView = (TextView) view.findViewById(R.id.breedListDargon);
        TextView oddTextView = (TextView) view.findViewById(R.id.breedListOdd);
        String dt = getDragonText(d);
        dragonTextView.setText(dt);
        oddTextView.setText(String.format("%.1f%%",d.odd));
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
            sb.append(d.toString());
            sb.append(" (");
            sb.append("B: ");
             tBreed = Integer.parseInt(d.breedingTime);
            if(vipTimes){
                tBreed = tBreed * 80/100;
            }


            bDay = tBreed / (60 * 60 * 24);
            bHour = (tBreed - bDay * (60 * 60 * 24)) / (60 * 60);
            bMin = (tBreed - bDay * (60 * 60 * 24) - bHour * (60 * 60)) / (60);
            bSec = tBreed - bDay * (60 * 60 * 24) - bHour * (60 * 60) - bMin * 60;




            if (bDay > 0) {
                sb.append(bDay + "d, ");
            }
            if (bHour > 0) {
                sb.append(bHour + "h, ");
            }
            if (bMin > 0) {
                sb.append(bMin + "m, ");
            }
            if (bSec > 0) {
                sb.append(bSec + "s, ");
            }
            sb.setLength(sb.length() - 2);
        } catch (Exception e) {
            sb.append("N/A");
            e.printStackTrace();
        }
        try {
            sb.append(" / H: ");
            tHatch = Integer.parseInt(d.hatchingTime);
            if(vipTimes){
                tHatch = tHatch * 80/100;
            }


            hDay = tHatch / (60 * 60 * 24);
            hHour = (tHatch - hDay * (60 * 60 * 24)) / (60 * 60);
            hMin = (tHatch - hDay * (60 * 60 * 24) - hHour * (60 * 60)) / (60);
            hSec = tHatch - hDay * (60 * 60 * 24) - hHour * (60 * 60) - hMin * 60;


            if (hDay > 0) {
                sb.append(hDay + "d, ");
            }
            if (hHour > 0) {
                sb.append(hHour + "h, ");
            }
            if (hMin > 0) {
                sb.append(hMin + "m, ");
            }
            if (hSec > 0) {
                sb.append(hSec + "s, ");
            }
            sb.setLength(sb.length() - 2);

        } catch (Exception e) {
            sb.append("N/A");
            e.printStackTrace();
        }
        sb.append(")");


        return sb.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
//            int tBreed = Integer.parseInt(items.get(position).breedingTime);
//            int tHatch = Integer.parseInt(items.get(position).hatchingTime);
//            int bDay;
//            int bHour;
//            int bMin;
//            int bSec;
//            int hDay;
//            int hHour;
//            int hMin;
//            int hSec;
//
//            bDay = tBreed / (60*60*24);
//            bHour = (tBreed - bDay*(60*60*24)) / (60*60);
//            bMin = (tBreed - bDay*(60*60*24) - bHour*(60*60)) / (60);
//            bSec =  tBreed - bDay*(60*60*24) - bHour*(60*60) - bMin*60;
//
//            hDay = tHatch / (60*60*24);
//            hHour = (tHatch - hDay*(60*60*24)) / (60*60);
//            hMin = (tHatch - hDay*(60*60*24) - hHour*(60*60)) / (60);
//            hSec =  tHatch - hDay*(60*60*24) - hHour*(60*60) - hMin*60;
//
//            StringBuilder sb = new StringBuilder("B: ");
//            if(bDay > 0){
//                sb.append(bDay + " Tag" + (bDay > 1?"e, ":", "));
//            }
//            if(bHour > 0){
//                sb.append(bHour + " Stunde" + (bHour > 1?"n, ":", "));
//            }
//            if(bMin > 0){
//                sb.append(bMin + " Minute" + (bMin > 1?"n, ":", "));
//            }
//            if(bSec > 0){
//                sb.append(bSec + " Sekunde" + (bSec > 1?"n, ":", "));
//            }
//            sb.setLength(sb.length()-2);
//
//            sb.append(" / H: ");
//            if(hDay > 0){
//                sb.append(hDay + " Tag" + (hDay > 1?"e, ":", "));
//            }
//            if(hHour > 0){
//                sb.append(hHour + " Stunde" + (hHour > 1?"n, ":", "));
//            }
//            if(hMin > 0){
//                sb.append(hMin + " Minute" + (hMin > 1?"n, ":", "));
//            }
//            if(hSec > 0){
//                sb.append(hSec + " Sekunde" + (hSec > 1?"n, ":", "));
//            }
//            sb.setLength(sb.length()-2);

//            Toast.makeText(context, sb.toString(),
//                    Toast.LENGTH_LONG).show();
            Dragon d = items.get(position);

            if(context instanceof MainActivity){
                ((MainActivity)context).displayHowToResult(d);
            }

        } catch (Exception e) {
           // e.printStackTrace();
        }
    }
}
