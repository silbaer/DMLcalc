package de.silbaer.dmlcalc;

import android.content.Context;
import android.util.Pair;
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
import java.util.Map;

/**
 * Created by silbaer on 04.09.16.
 */
public class howToItemAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private final LayoutInflater mInflater;
    private List<Pair<Pair<Dragon,Dragon>,Double>> items;
    private final Context context;

    public class DragonPairOddComparator implements Comparator<Pair<Pair<Dragon,Dragon>,Double>> {
        @Override
        public int compare(Pair<Pair<Dragon,Dragon>,Double> o1, Pair<Pair<Dragon,Dragon>,Double> o2) {
            if(o1.second  < o2.second){
                return 1;
            } else if(o1.second  > o2.second){
              return -1;
            } else {
             return 0;
            }

        }
    }


    public howToItemAdapter(Context context, List<Pair<Pair<Dragon,Dragon>,Double>> pairs) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = pairs;
        Collections.sort(items, new DragonPairOddComparator());
        this.context = context;
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

        LinearLayout itemView;

        itemView = (LinearLayout) convertView;
        if(itemView == null) {
            itemView = (LinearLayout) mInflater.inflate(R.layout.breed_list_item, parent, false);
        }
        bindView(itemView, position);
        return itemView;
    }

    private void bindView(LinearLayout view, int position) {
        Pair<Pair<Dragon,Dragon>,Double> p = (Pair<Pair<Dragon,Dragon>,Double>) getItem(position);
        view.setId((int) getItemId(position));
        TextView dragonTextView = (TextView) view.findViewById(R.id.breedListDargon);
        TextView oddTextView = (TextView) view.findViewById(R.id.breedListOdd);
        dragonTextView.setText(p.first.first.lng_de + " & " + p.first.second.lng_de);
        oddTextView.setText(String.format("%.1f%%",p.second));
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
//
//            Toast.makeText(context, sb.toString(),
//                    Toast.LENGTH_LONG).show();
            Pair<Pair<Dragon,Dragon>,Double> p = (Pair<Pair<Dragon,Dragon>,Double>) getItem(position);
   //         ((MainActivity)context).displayBreedResult(p.first.first,p.first.second);

            if(context instanceof MainActivity){
                ((MainActivity)context).displayBreedResult(p.first.first,p.first.second);
            }

        } catch (Exception e) {
           // e.printStackTrace();
        }
    }
}
