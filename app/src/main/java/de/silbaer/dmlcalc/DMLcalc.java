package de.silbaer.dmlcalc;


import android.app.Application;
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

    public String DDM;
    public List<String> DDM_Elements;

    public String DDW;
    public String DDW_Mom;
    public String DDW_Dad;

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
        DDM_Elements = new ArrayList<>();

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
            if(child.id.equalsIgnoreCase(DDW)){
                if(mom.id.equalsIgnoreCase(DDW_Mom) && dad.id.equalsIgnoreCase(DDW_Dad)
                        || dad.id.equalsIgnoreCase(DDW_Mom) && mom.id.equalsIgnoreCase(DDW_Dad) ){
                    return true;
                }
            }
            //if (DateTime.Now() > DOW.begin && DateTime.Now() < DOW.end && this.id == DOW.id) {
            //  if(DOW.momid == mom.id && DOW.dadid == dad.id || DOW.momid == dad.id && DOW.dadid == mom.id ){
            //    return true;
            //  }
            //}


            // Dragon of the month
            if(child.id.equalsIgnoreCase(DDM)){
                List<String> MomDadElements = new ArrayList<>();
                MomDadElements.addAll(mom.elements);
                MomDadElements.addAll(dad.elements);
                if(DDM_Elements.size() > 0) {
                  if(MomDadElements.containsAll(DDM_Elements)){
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
