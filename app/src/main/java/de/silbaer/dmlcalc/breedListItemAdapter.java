package de.silbaer.dmlcalc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
//        LinearLayout itemView = (LinearLayout) mInflater.inflate(R.layout.breed_list_item, parent, false);
        RelativeLayout itemView = (RelativeLayout) mInflater.inflate(R.layout.relativ_breed_list_item, parent, false);
        bindView(itemView, position);
        return itemView;
    }

    private void bindView(RelativeLayout view, int position) {
        Dragon d = (Dragon) getItem(position);
        view.setId((int) getItemId(position));
        TextView dragonTextView = (TextView) view.findViewById(R.id.relativBreedListIntemDragon);
        TextView oddTextView = (TextView) view.findViewById(R.id.relativBreedListIntemOdd);
        dragonTextView.setText(d.lng_de);
        oddTextView.setText(String.format("%.1f%%",d.odd));

        ImageView iv = (ImageView) view.findViewById(R.id.iv_relativBreedListIntem_e1);
        if(d.element1.equalsIgnoreCase("earth")){
            iv.setImageResource(R.drawable.element_earth);
        }
        if(d.element1.equalsIgnoreCase("energy")){
            iv.setImageResource(R.drawable.element_energy);
        }
        if(d.element1.equalsIgnoreCase("fire")){
            iv.setImageResource(R.drawable.element_fire);
        }
        if(d.element1.equalsIgnoreCase("legendary")){
            iv.setImageResource(R.drawable.element_legendary);
        }
        if(d.element1.equalsIgnoreCase("light")){
            iv.setImageResource(R.drawable.element_light);
        }
        if(d.element1.equalsIgnoreCase("metal")){
            iv.setImageResource(R.drawable.element_metal);
        }
        if(d.element1.equalsIgnoreCase("plant")){
            iv.setImageResource(R.drawable.element_plant);
        }
        if(d.element1.equalsIgnoreCase("shadow")){
            iv.setImageResource(R.drawable.element_shadow);
        }
        if(d.element1.equalsIgnoreCase("void")){
            iv.setImageResource(R.drawable.element_void);
        }
        if(d.element1.equalsIgnoreCase("water")){
            iv.setImageResource(R.drawable.element_water);
        }
        if(d.element1.equalsIgnoreCase("wind")){
            iv.setImageResource(R.drawable.element_wind);
        }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            int tBreed = Integer.parseInt(items.get(position).breedingTime);
            int tHatch = Integer.parseInt(items.get(position).hatchingTime);
            int bDay;
            int bHour;
            int bMin;
            int bSec;
            int hDay;
            int hHour;
            int hMin;
            int hSec;

            bDay = tBreed / (60*60*24);
            bHour = (tBreed - bDay*(60*60*24)) / (60*60);
            bMin = (tBreed - bDay*(60*60*24) - bHour*(60*60)) / (60);
            bSec =  tBreed - bDay*(60*60*24) - bHour*(60*60) - bMin*60;

            hDay = tHatch / (60*60*24);
            hHour = (tHatch - hDay*(60*60*24)) / (60*60);
            hMin = (tHatch - hDay*(60*60*24) - hHour*(60*60)) / (60);
            hSec =  tHatch - hDay*(60*60*24) - hHour*(60*60) - hMin*60;

            StringBuilder sb = new StringBuilder("B: ");
            if(bDay > 0){
                sb.append(bDay + " Tag" + (bDay > 1?"e, ":", "));
            }
            if(bHour > 0){
                sb.append(bHour + " Stunde" + (bHour > 1?"n, ":", "));
            }
            if(bMin > 0){
                sb.append(bMin + " Minute" + (bMin > 1?"n, ":", "));
            }
            if(bSec > 0){
                sb.append(bSec + " Sekunde" + (bSec > 1?"n, ":", "));
            }
            sb.setLength(sb.length()-2);

            sb.append(" / H: ");
            if(hDay > 0){
                sb.append(hDay + " Tag" + (hDay > 1?"e, ":", "));
            }
            if(hHour > 0){
                sb.append(hHour + " Stunde" + (hHour > 1?"n, ":", "));
            }
            if(hMin > 0){
                sb.append(hMin + " Minute" + (hMin > 1?"n, ":", "));
            }
            if(hSec > 0){
                sb.append(hSec + " Sekunde" + (hSec > 1?"n, ":", "));
            }
            sb.setLength(sb.length()-2);

            Toast.makeText(context, sb.toString(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
           // e.printStackTrace();
        }
    }
}
