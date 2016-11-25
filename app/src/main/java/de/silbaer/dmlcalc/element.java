package de.silbaer.dmlcalc;

/**
 * Created by silbaer on 25.11.16.
 */
public class element{
    public String id;
    public element(String Id){
        id = Id;
    }


    public String toString(){
        String retval = id;

        try {
            /*

            if (id == "fire") {
                retval = DMLcalc.getContext().getString(R.string.txt_fire);
            } else if (id == "wind") {
                retval = DMLcalc.getContext().getString(R.string.txt_wind);
            } else if (id == "earth") {
                retval = DMLcalc.getContext().getString(R.string.txt_earth);
            } else if (id == "water") {
                retval = DMLcalc.getContext().getString(R.string.txt_water);
            } else if (id == "plant") {
                retval = DMLcalc.getContext().getString(R.string.txt_plant);
            } else if (id == "metal") {
                retval = DMLcalc.getContext().getString(R.string.txt_metal);
            } else if (id == "energy") {
                retval = DMLcalc.getContext().getString(R.string.txt_energy);
            } else if (id == "void") {
                retval = DMLcalc.getContext().getString(R.string.txt_void);
            } else if (id == "light") {
                retval = DMLcalc.getContext().getString(R.string.txt_light);
            } else if (id == "shadow") {
                retval = DMLcalc.getContext().getString(R.string.txt_shadow);
            } else if (id == "legendary") {
                retval = DMLcalc.getContext().getString(R.string.txt_legendary);
            } else {

*/

                DMLcalc c = DMLcalc.Instance();
                try {
                    if (c != null) {
                        retval = c.getStringResourceByName("txt_" + id);
                    }
                } catch (Exception ex) {
                }
     //       }
        } catch (Exception eex) {
            String e = eex.toString();
        }
        return retval;
    }
}