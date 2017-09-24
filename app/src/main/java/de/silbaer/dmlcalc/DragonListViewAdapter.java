package de.silbaer.dmlcalc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.silbaer.dmlcalc.dragonListFragment.OnListFragmentInteractionListener;
import de.silbaer.dmlcalc.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DragonListViewAdapter extends RecyclerView.Adapter<DragonListViewAdapter.ViewHolder> {

    private final List<Dragon> mValues;
 //   private final OnListFragmentInteractionListener mListener;

    public DragonListViewAdapter(List<Dragon> items) {
        mValues = items;
      //  mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dragon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).getId());
//        holder.mContentView.setText(mValues.get(position).toString());
//        iv.setImageResource(R.drawable.element_earth);
        holder.mDragonImage.setImageResource(R.drawable.element_boss);
        holder.mDragonName.setText(holder.mItem.toString());
        setElement(holder.mElement1,holder.mItem.getElement1());
        setElement(holder.mElement2,holder.mItem.getElement2());
        setElement(holder.mElement3,holder.mItem.getElement3());
        holder.mDragonStats.setText(holder.mItem.getBaseHealth() + " / " + holder.mItem.getBaseAttack() + " / " + holder.mItem.getBaseGold() );


//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public Dragon mItem;
        public final ImageView mDragonImage;
        public final TextView mDragonName;
        public final ImageView mElement1;
        public final ImageView mElement2;
        public final ImageView mElement3;
        public final TextView mDragonStats;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDragonImage = (ImageView) view.findViewById(R.id.dragonPic);
            mDragonName = (TextView) view.findViewById(R.id.textDragonName);
            mElement1 = (ImageView) view.findViewById(R.id.ele1);
            mElement2 = (ImageView) view.findViewById(R.id.ele2);
            mElement3 = (ImageView) view.findViewById(R.id.ele3);
            mDragonStats = (TextView) view.findViewById(R.id.textStats);
        }

 //       @Override
 //       public String toString() {
 //           return super.toString() + " '" + mContentView.getText() + "'";
 //       }
    }
}
