package de.silbaer.dmlcalc;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by silbaer on 22.10.17.
 */

public class AutoColpleteDragonAdapter extends ArrayAdapter<Dragon> implements Filterable {

    private final String MY_DEBUG_TAG = "AutoColpleteDragonAdapter";
    private ArrayList<Dragon> items;
    private ArrayList<Dragon> itemsAll;
    private ArrayList<Dragon> suggestions;
    private int viewResourceId;
    private Context myContext;

    public AutoColpleteDragonAdapter(Context context, int viewResourceId, ArrayList<Dragon> items)  {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<Dragon>) items.clone();
        this.suggestions = new ArrayList<Dragon>();
        this.viewResourceId = viewResourceId;
        myContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(viewResourceId, null);
        }
        Dragon dragon = items.get(position);
        if (dragon != null) {
              ImageView mDragonImage;
              TextView mDragonName;
              ImageView mElement1;
              ImageView mElement2;
              ImageView mElement3;
              TextView mDragonStats;

            mDragonImage = (ImageView) view.findViewById(R.id.dragonPic);
            mDragonName = (TextView) view.findViewById(R.id.textDragonName);
            mElement1 = (ImageView) view.findViewById(R.id.ele1);
            mElement2 = (ImageView) view.findViewById(R.id.ele2);
            mElement3 = (ImageView) view.findViewById(R.id.ele3);
            mDragonStats = (TextView) view.findViewById(R.id.textStats);


            if (mDragonName != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                mDragonName.setText(dragon.toString());
            }
            String resName = dragon.getId() + "_icon" ;
            Resources resources = myContext.getResources();
            final int resourceId = resources.getIdentifier(resName, "drawable", myContext.getPackageName());
            mDragonImage.setImageResource( resourceId);

            setElement(mElement1,dragon.getElement1());
            setElement(mElement2,dragon.getElement2());
            setElement(mElement3,dragon.getElement3());
            mDragonStats.setText(dragon.getBaseHealth() + " / " + dragon.getBaseAttack() + " / " + dragon.getBaseGold() );

        }
        return view;
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
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Dragon)(resultValue)).toString();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Dragon dragon : itemsAll) {
                    if(dragon.toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(dragon);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Dragon> filteredList = (ArrayList<Dragon>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Dragon d : filteredList) {
                    add(d);
                }
                notifyDataSetChanged();
            }
        }
    };

}
