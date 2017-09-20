package de.silbaer.dmlcalc;

import android.content.Context;
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
            itemView = (LinearLayout) mInflater.inflate(R.layout.list_howtoresult, parent, false);
//
//            if (position % 2 == 0) {
//                Log.d("Position","Pos: " + position + " => BLUE");
//                  itemView.setBackgroundColor(Color.BLUE);
//            } else {
//                Log.d("Position","Pos: " + position + " => GRAY");
//                itemView.setBackgroundColor(Color.LTGRAY);
//            }
        }

        bindView(itemView, position);
        return itemView;
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


    private void bindView(LinearLayout view, int position) {
        if (position % 2 == 0) {

            view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorListEven, null));

        } else {
//            Log.d("Position","Pos: " + position + " => GRAY");
            view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorListOdd, null));
        }
        Pair<Pair<Dragon,Dragon>,Double> p = (Pair<Pair<Dragon,Dragon>,Double>) getItem(position);
        view.setId((int) getItemId(position));


        Pair<Dragon,Double> d = (Pair<Dragon,Double>) getItem(position);
        view.setId((int) getItemId(position));
        TextView dragonTextView1 = (TextView) view.findViewById(R.id.textDragonName1);

        TextView dragonTextView2 = (TextView) view.findViewById(R.id.textDragonName2);

        TextView oddTextView = (TextView) view.findViewById(R.id.textOdd);

        dragonTextView1.setText(p.first.first.toString());
        dragonTextView2.setText(p.first.second.toString());
        oddTextView.setText(String.format("%.1f%%",p.second));

        String tmp;
        ImageView iv = (ImageView) view.findViewById(R.id.ele11);
        tmp = p.first.first.getElement1();
        setElement(iv,tmp);
        iv = (ImageView) view.findViewById(R.id.ele12);
        tmp = p.first.first.getElement2();
        setElement(iv,tmp);
        iv = (ImageView) view.findViewById(R.id.ele13);
        tmp = p.first.first.getElement3();
        setElement(iv,tmp);

        iv = (ImageView) view.findViewById(R.id.ele21);
        tmp = p.first.second.getElement1();
        setElement(iv,tmp);
        iv = (ImageView) view.findViewById(R.id.ele22);
        tmp = p.first.second.getElement2();
        setElement(iv,tmp);
        iv = (ImageView) view.findViewById(R.id.ele23);
        tmp = p.first.second.getElement3();
        setElement(iv,tmp);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
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
