package de.silbaer.dmlcalc;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
//
//    private Drawable getScaledDrawable(int resourceID, int width, int height){
//
//        Resources resources = myContext.getResources();
//
//        // Read your drawable from somewhere
//        Drawable dr = resources.getDrawable(resourceID);
//        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//// Scale it to width x height
//        Drawable d = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true));
//// Set your new, scaled drawable "d"
//        return d;
//    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).getId());
//        holder.mContentView.setText(mValues.get(position).toString());
//        iv.setImageResource(R.drawable.element_earth);
       // holder.mDragonImage.setImageResource(R.drawable.element_boss);
        if (position % 2 == 0) {
            holder.mView.setBackgroundColor(ResourcesCompat.getColor(myContext.getResources(), R.color.colorListEven, null));
        } else {
            holder.mView.setBackgroundColor(ResourcesCompat.getColor(myContext.getResources(), R.color.colorListOdd, null));
        }
//
//        String resName = holder.mItem.getId() + "_icon" ;
//        Resources resources = myContext.getResources();
//        int resourceId = resources.getIdentifier(resName, "drawable", myContext.getPackageName());
//
//
//     //   holder.mDragonImage.setImageResource( resourceId);
//        if(resourceId == 0){
//            resourceId = resources.getIdentifier("unknown_icon", "drawable", myContext.getPackageName());
//        }
//
//
//
//        List<Drawable> scaledLayers = new ArrayList<Drawable>();
//
//        scaledLayers.add(getScaledDrawable(resourceId,500,500));
////        layers[0] = resources.getDrawable(resourceId);
//
//        String type = holder.mItem.getType();
//        if("C".equalsIgnoreCase(type)){
//            resourceId = resources.getIdentifier("classification_corner_common", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,250,250));
////            layers[1] = resources.getDrawable(resourceId);
//        } else if("D".equalsIgnoreCase(type)){
//            resourceId = resources.getIdentifier("classification_corner_divine", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,250,250));
////            layers[1] = resources.getDrawable(resourceId);
//        } else if("E".equalsIgnoreCase(type)){
//            resourceId = resources.getIdentifier("classification_corner_epic", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,250,250));
////            layers[1] = resources.getDrawable(resourceId);
//        } else if("L".equalsIgnoreCase(type)){
//            resourceId = resources.getIdentifier("classification_corner_legendary", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,250,250));
////            layers[1] = resources.getDrawable(resourceId);
//        } else if("R".equalsIgnoreCase(type)){
//            resourceId = resources.getIdentifier("classification_corner_rare", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,250,250));
////            layers[1] = resources.getDrawable(resourceId);
//        } else if("U".equalsIgnoreCase(type)){
//            resourceId = resources.getIdentifier("classification_corner_uncommon", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,250,250));
////            layers[1] = resources.getDrawable(resourceId);
//        } else {
//            resourceId = resources.getIdentifier("classification_corner_empty", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,250,250));
////            layers[1] = resources.getDrawable(resourceId);
//        }
//
//        if(holder.mItem.isVIP()) {
//            resourceId = resources.getIdentifier("vip_dragons_icon", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,200,200));
////            Drawable d = resources.getDrawable(resourceId);
////            layerDrawable.addLayer(d);
////            layerDrawable.setLayerSize(2,200,200);
////            layerDrawable.setLayerInsetLeft(2,300);
////            layerDrawable.setLayerInsetTop(2,300);
//        } else if(holder.mItem.isEnchatmentBreed()){
//            resourceId = resources.getIdentifier("enchanted_breeding_icon", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,200,200));
//        } else if(!holder.mItem.isBreadable()){
//            resourceId = resources.getIdentifier("ltd_icon", "drawable", myContext.getPackageName());
//            scaledLayers.add(getScaledDrawable(resourceId,200,200));
//        }
//
//        LayerDrawable layerDrawable = new LayerDrawable(scaledLayers.toArray(new Drawable[0]));
////        layerDrawable.setLayerSize(0,500,500);
////        layerDrawable.setLayerSize(1,250,250);
////        layerDrawable.setLayerInsetLeft(1,250);
//        layerDrawable.setLayerInset(1,250,0,0,250);
//        if(scaledLayers.size() > 2){
//            layerDrawable.setLayerInset(2,300,300,0,0);
//        }
//


 //       holder.mDragonImage.setImageDrawable(DMLcalc.Instance().getDragonIcon(holder.mItem));
        DMLcalc.Instance().loadDragonIcon(holder.mItem.getId(),holder.mDragonImage);



        holder.mDragonName.setText(holder.mItem.toString().replace("+","").replace("&","").replace("*",""));
        setElement(holder.mElement1,holder.mItem.getElement1());
        setElement(holder.mElement2,holder.mItem.getElement2());
        setElement(holder.mElement3,holder.mItem.getElement3());
        holder.mDragonStats.setText(holder.mItem.getBaseHealth() + " / " + holder.mItem.getBaseAttack() + " / " + holder.mItem.getBaseGold() );


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
