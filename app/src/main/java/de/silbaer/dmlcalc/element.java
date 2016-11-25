package de.silbaer.dmlcalc;

/**
 * Created by silbaer on 25.11.16.
 */
public class element{
    public String id;
    public element(String Id){
        id = Id;
    }


    public String toString() {
        String retval = id;

        try {
            DMLcalc c = DMLcalc.Instance();
            try {
                if (c != null) {
                    retval = c.getStringResourceByName("element_" + id);
                }
            } catch (Exception ex) {
            }
        } catch (Exception eex) {
            String e = eex.toString();
        }
        return retval;
    }
}