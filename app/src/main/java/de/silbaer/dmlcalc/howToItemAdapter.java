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
        dragonTextView.setText(p.first.first.toString() + " & " + p.first.second.toString());
        oddTextView.setText(String.format("%.1f%%",p.second));
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
