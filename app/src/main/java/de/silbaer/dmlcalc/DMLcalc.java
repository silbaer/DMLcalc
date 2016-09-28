package de.silbaer.dmlcalc;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by silbaer on 13.06.16.
 */
public class DMLcalc extends Application {

    public ArrayList<String> elements = new ArrayList<>();

    private static final String PREFS_NAME = "de.silbaer.dmlcal.appsettings";

    private String DDM="";
    private List<String> DDM_Elements;

    private String DDW="";
    private String DDW_Mom="";
    private String DDW_Dad="";

    public Map<String,Dragon> dragons = new Hashtable<String,Dragon>();
//    public Dictionary<string, Element> elements = new Dictionary<string,Element>();

    Map<String,Double> odds;

    public DMLcalc() {
        odds = new Hashtable<String,Double>();
        odds.put("C",48d);
        odds.put("U",21d);
        odds.put("R",15d);
        odds.put("E",10d);
        odds.put("L", 6d);
        DDM_Elements = new ArrayList<>();

        elements.add("fire");
        elements.add("wind");
        elements.add("earth");
        elements.add("water");
        elements.add("plant");
        elements.add("metal");
        elements.add("energy");
        elements.add("void");
        elements.add("light");
        elements.add("shadow");
        elements.add("legendary");



    }


    public void setDDW(String ddw, String mom, String dad){
        DDW = ddw;
        DDW_Dad = dad;
        DDW_Mom = mom;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.DDW),DDW);
        editor.putString(getString(R.string.DDW_mom),DDW_Mom);
        editor.putString(getString(R.string.DDW_dad),DDW_Dad);
        editor.commit();
    }

    public void setDDM(String ddm, String e1, String e2, String e3, String e4){
        DDM = ddm;
        DDM_Elements.clear();
        DDM_Elements.add(e1);
        DDM_Elements.add(e2);
        DDM_Elements.add(e3);
        DDM_Elements.add(e4);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.DDM),DDM);
        editor.putString(getString(R.string.DDM_e1),DDM_Elements.get(0));
        editor.putString(getString(R.string.DDM_e2),DDM_Elements.get(1));
        editor.putString(getString(R.string.DDM_e3),DDM_Elements.get(2));
        editor.putString(getString(R.string.DDM_e4),DDM_Elements.get(3));
        editor.commit();
    }

    public String getDDM() {
        if(DDM.isEmpty()){
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            DDM = prefs.getString(getString(R.string.DDM),"");
        }
        return DDM;
    }
    private void readDdmElementsFromPreferences(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        String tmp = prefs.getString(getString(R.string.DDM_e1),"");
        if(!tmp.isEmpty()){
            DDM_Elements.add(tmp);
        }
        tmp = prefs.getString(getString(R.string.DDM_e2),"");
        if(!tmp.isEmpty()){
            DDM_Elements.add(tmp);
        }
        tmp = prefs.getString(getString(R.string.DDM_e3),"");
        if(!tmp.isEmpty()){
            DDM_Elements.add(tmp);
        }
        tmp = prefs.getString(getString(R.string.DDM_e4),"");
        if(!tmp.isEmpty()){
            DDM_Elements.add(tmp);
        }
        if(DDM_Elements.size() != 4){
            DDM_Elements.clear();
        }
    }
    public String getDDM_e1() {
        if(DDM_Elements .isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() == 4) {
            return DDM_Elements.get(0);
        }
        return "";
    }
    public String getDDM_e2() {
        if(DDM_Elements.isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() == 4) {
        return DDM_Elements.get(1);
        }
        return "";
    }
    public String getDDM_e3() {
        if(DDM_Elements.isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() == 4) {
        return DDM_Elements.get(2);
        }
        return "";
    }
    public String getDDM_e4() {
        if(DDM_Elements.isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() == 4) {
        return DDM_Elements.get(3);
        }
        return "";
    }
    public List<String> getDdmElements() {
        if (DDM_Elements.isEmpty()) {
            readDdmElementsFromPreferences();
        }
        return DDM_Elements;
    }



    public String getDDW() {
        if(DDW.isEmpty()){
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            DDW = prefs.getString(getString(R.string.DDW),"");
        }
        return DDW;
    }
    public String getDDW_mom() {
        if(DDW_Mom.isEmpty()){
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            DDW_Mom = prefs.getString(getString(R.string.DDW_mom),"");
        }
        return DDW_Mom;
    }
    public String getDDW_dad() {
        if(DDW_Dad.isEmpty()){
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            DDW_Dad = prefs.getString(getString(R.string.DDW_dad),"");
        }
        return DDW_Dad;
    }


    @Override
    public void onCreate(){
        super.onCreate();

        AssetManager assetManager = getAssets();

        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("dragon.list"));
//
//        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();

            BufferedReader reader = new BufferedReader(is);
            //reader.readLine();
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
//            while ((line = reader.readLine()) != null) {

//            }
            String dragonJS = sb.toString();


            Dragon d;
            String[] lines = dragonJS.split("[\\n]+");
            for (String jsline : lines) {
                String l = jsline.trim();
                if(!l.isEmpty()){
                    d = new Dragon(l);
                    dragons.put(d.id,d);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DMLcalc(String dragonJS){
        odds = new Hashtable<String,Double>();
        odds.put("C",48d);
        odds.put("U",21d);
        odds.put("R",15d);
        odds.put("E",10d);
        odds.put("L", 6d);


        Dragon d;
        String[] lines = dragonJS.split("[\\n]+");
        for (String line : lines) {
            String l = line.trim();
            if(!l.isEmpty()){
                d = new Dragon(l);
                dragons.put(d.id,d);
            }
        }
    }



    public void CalcOdds( List<Dragon> resultList) {
        double sum = 0;
        for (Dragon d : resultList) {
            if (!d.isUnreleased()
                    && !d.isBoss()
                    //    && !d.isEvent()
                    && !d.isVIP()
                    ) {
                if (odds.containsKey(d.type)) {
                    sum += odds.get(d.type);
                }
            }
        }
        for (Dragon d : resultList) {
            if (odds.containsKey(d.type)) {
                d.odd =  100/sum * odds.get(d.type);
            }
        }
    }


    private  TreeMap<Pair<Dragon,Dragon>,Double> sortMapByValue(Map<Pair<Dragon,Dragon>,Double> map){
        Comparator<Pair<Dragon,Dragon>> comparator = new ValueComparator(map);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<Pair<Dragon,Dragon>,Double> result = new TreeMap<Pair<Dragon,Dragon>,Double>(comparator);
        result.putAll(map);
        return result;

    }
    private class ValueComparator implements Comparator {

        Map map;

        public ValueComparator(Map map){
            this.map = map;
        }
        public int compare(Object keyA, Object keyB){

            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);

            int compare = valueA.compareTo(valueB);;
            if (compare == 0)
                return 1;
            else
                return -compare;



        }
    }


    public List<Pair<Pair<Dragon,Dragon>,Double>> howToBreed(Dragon son) {
        List<Pair<Pair<Dragon,Dragon>,Double>> retval = new ArrayList<>();
        ArrayList< Dragon> dl = new ArrayList<>(dragons.values());
        for(int x = 0; x < dl.size()-1; x++){
            for(int y = x+1; y < dl.size();y++){
                if(!dl.get(x).id.equalsIgnoreCase(son.id)
                        && !dl.get(y).id.equalsIgnoreCase(son.id)) {
                    if (isChild(dl.get(x), dl.get(y),son)) {
                        List<Dragon> tmp = breed(dl.get(x), dl.get(y));
                        for (Dragon d : tmp) {
                            if (d.id.equalsIgnoreCase(son.id)) {
                                retval.add(new Pair<Pair<Dragon,Dragon>,Double>(new Pair<Dragon, Dragon>(dl.get(x), dl.get(y)), d.odd));
                            }
                        }
                    }
                }
            }
        }
        return retval;
    }

    public List<Dragon> breed(Dragon mom, Dragon dad) {
        List<Dragon> retval = new ArrayList<Dragon>();
        if (dad != null && mom != null && !mom.id.equals(dad.id)) {
            for (Dragon d : dragons.values()) {
                if (isChild(mom, dad,d)) {
                    retval.add(d);
                }
            }
            CalcOdds(retval);
        }
        return retval;
    }

    public boolean isChild(Dragon mom, Dragon dad, Dragon child) {
        Boolean retval = false;

        Boolean dotmcontrol = false;
        Boolean mumLegendary = false;
        Boolean dadLegendary = false;

        if (mom.id.equals(dad.id)) {
            // gleiche drachen geht nicht
            return false;
        }

        if(child.isUnreleased()){
             return false;
        }

        if (!child.boss_vip.isEmpty()  && !child.boss_vip.equals("V")) { // nicht VIP, aber Boss oder Event oder unreleased
            // Drogon of the week
            if(child.id.equalsIgnoreCase(getDDW())){
                if(mom.id.equalsIgnoreCase(getDDW_mom()) && dad.id.equalsIgnoreCase(getDDW_dad())
                        || dad.id.equalsIgnoreCase(getDDW_mom()) && mom.id.equalsIgnoreCase(getDDW_dad()) ){
                    return true;
                }
            }
            //if (DateTime.Now() > DOW.begin && DateTime.Now() < DOW.end && this.id == DOW.id) {
            //  if(DOW.momid == mom.id && DOW.dadid == dad.id || DOW.momid == dad.id && DOW.dadid == mom.id ){
            //    return true;
            //  }
            //}


            // Dragon of the month
            if(child.id.equalsIgnoreCase(getDDM())){
                if(mom.isBoss() || mom.isUnreleased() || mom.islegendary()){
                    return false;
                }
                if(dad.isBoss() || dad.isUnreleased() || dad.islegendary()){
                    return false;
                }
                List<String> MomDadElements = new ArrayList<>();
                MomDadElements.addAll(mom.elements);
                MomDadElements.addAll(dad.elements);
                if(getDdmElements().size() > 0) {
                  if(MomDadElements.containsAll(getDdmElements())){
                      return true;
                  }
                }

            }

            //if (DateTime.Now() > DOM.begin && DateTime.Now() < DOM.end && this.id == DOM.id) {
            //List<string> momDadElements = new List<string>();
            //momDadElements.AddRange(mom.elements);
            //momDadElements.AddRange(dad.elements);
            // if(momDadElements.contains(DOM.element1) && momDadElements.contains(DOM.element2) && momDadElements.contains(DOM.element3) && momDadElements.contains(DOM.element4) {
            ////  return true;
            ////}
            //}

            return false;
        }

        /** SPECIAL BREED **/
        //breed with legendary

//        List<string> mumOrDadLegendaryListElements = new List<string>();

        if (mom.islegendary()) {
//            mumOrDadLegendaryListElements.AddRange(mom.elements);
        }
        if (dad.islegendary()) {
//            mumOrDadLegendaryListElements.AddRange(dad.elements);
        }
//        mumOrDadLegendaryListElements.RemoveAll(m => m == "legendary");

        if (mom.islegendary() && dad.islegendary()) {
//            if (this.isBoss() || this.isEvent() || this.islegendary) {
//                return false;
//            } else {
//                if (this.elements.Count == 2 &&
//                        this.id != "sunflower" &&
//                        this.id != "mercury" &&
//                        this.id != "lightning" &&
//                        this.id != "magnet" &&
//                        this.id != "emperor") {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
        }
        if (dad.islegendary() || mom.islegendary()) {
            return false; // Lassen wir mal weg....
//            if (!this.islegendary()) {
//            } else {
//                return false;
//            }
        } else {
            //sunflower, mercury, lightning, magnet, emperor

            if (child.id.equalsIgnoreCase("sunflower") ||
                    child.id.equalsIgnoreCase("mercury") ||
                    child.id.equalsIgnoreCase("lightning") ||
                    child.id.equalsIgnoreCase("magnet") ||
                    child.id.equalsIgnoreCase("emperor")) {

                // Inkompatible Elemente ?
                if (mom.elements.size() == 1 || dad.elements.size() == 1) {
                    return false;
                }
            }
            //siren,pixie,dark machine,vortex,titan,narwhale
            if (child.id.equalsIgnoreCase("siren") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (child.id.equalsIgnoreCase("pixie") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("lightning")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("lightning")))) {
                return true;
            }
            if (child.id.equalsIgnoreCase("dark_machine") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("magnet")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("magnet")))) {
                return true;
            }
            if (child.id.equalsIgnoreCase("vortex") && ((mom.id.equalsIgnoreCase("lightning") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("lightning") && mom.id.equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (child.id.equalsIgnoreCase("titan") && ((mom.id.equalsIgnoreCase("magnet") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("magnet") && mom.id.equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (child.id.equalsIgnoreCase("narwhale") && ((mom.id.equalsIgnoreCase("magnet") && dad.id.equalsIgnoreCase("lightning")) || (dad.id.equalsIgnoreCase("magnet") && mom.id.equalsIgnoreCase("lightning")))) {
                return true;
            }
            if (child.id.equalsIgnoreCase("crystal") && ((mom.id.equalsIgnoreCase("emperor") && dad.id.equalsIgnoreCase("magnet")) || (dad.id.equalsIgnoreCase("emperor") && mom.id.equalsIgnoreCase("magnet")))) {
                return true;
            }
            if (child.elements.size() == 1) {
                //1 elements => mum & dad must have element
                if (mom.elements.contains(child.element1) && dad.elements.contains(child.element1)) {
                    return true;
                } else {
                    // JS ist hier schrott. Immer false
                    return false;
                }
            } else if (child.elements.size() == 2) {
                //2 elements => mum & dad can have his element
                // Auch hier ist das JS kaputt:
                //if ((mumHasFirstElem && dadHasFirstElem && mumHasSecondElem && dadHasSecondElem) || (mumHasFirstElem && dadHasSecondElem) || (mumHasSecondElem && dadHasFirstElem)) {
                //  return true;
                //}
                // Das erste ist ein Sonderfall und wird duch zweiten und dritten Therm aufgefangen

                if ((mom.elements.contains(child.element1) && dad.elements.contains(child.element2)) || (mom.elements.contains(child.element2) && dad.elements.contains(child.element1))) {
                    return true;
                }


            } else if (child.elements.size() == 3) {
                //3 elements => mum and dad must have all the elements
                if (child.islegendary()) {
                    return false;
                }
                Boolean e1 = false;
                Boolean e2 = false;
                Boolean e3 = false;
                Boolean m = false;
                Boolean d = false;
                if (mom.elements.contains(child.element1)) {
                    m = true;
                    e1 = true;
                }
                if (mom.elements.contains(child.element2)) {
                    m = true;
                    e2 = true;
                }
                if (mom.elements.contains(child.element3)) {
                    m = true;
                    e3 = true;
                }
                if (dad.elements.contains(child.element1)) {
                    d = true;
                    e1 = true;
                }
                if (dad.elements.contains(child.element2)) {
                    d = true;
                    e2 = true;
                }
                if (dad.elements.contains(child.element3)) {
                    d = true;
                    e3 = true;
                }

                if (e1 && e2 && e3 && m && d) {
                    return true;
                }
            }
        }
        return retval;    }

}
