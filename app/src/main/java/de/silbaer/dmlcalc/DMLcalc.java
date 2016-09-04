package de.silbaer.dmlcalc;


import android.util.Pair;

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
public class DMLcalc {






    public Map<String,Dragon> dragons = new Hashtable<String,Dragon>();
//    public Dictionary<string, Element> elements = new Dictionary<string,Element>();

    Map<String,Double> odds;

    public DMLcalc(String dragonJS){
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
            if (odds.containsKey(d.type)) {
                sum += odds.get(d.type);
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


    public Map<Pair<Dragon,Dragon>,Double> howToBreed(Dragon son) {
        Map<Pair<Dragon,Dragon>,Double> retval = new HashMap<Pair<Dragon,Dragon>,Double>();
        ArrayList< Dragon> dl = new ArrayList<Dragon>(dragons.values());
        for(int x = 0; x < dl.size()-1; x++){
            for(int y = x+1; y < dl.size();y++){
                if(!dl.get(x).id.equalsIgnoreCase(son.id)
                        && !dl.get(y).id.equalsIgnoreCase(son.id)) {
                    if (son.isChildOf(dl.get(x), dl.get(y))) {
                        List<Dragon> tmp = breed(dl.get(x), dl.get(y));
                        for (Dragon d : tmp) {
                            if (d.id.equalsIgnoreCase(son.id)) {
                                retval.put(new Pair<Dragon, Dragon>(dl.get(x), dl.get(y)), d.odd);
                            }
                        }
                    }
                }
            }
        }
        Map<Pair<Dragon,Dragon>,Double> retval2 = sortMapByValue( retval);
        return retval2;
    }

    public List<Dragon> breed(Dragon mom, Dragon dad) {
        List<Dragon> retval = new ArrayList<Dragon>();
        if (dad != null && mom != null && !mom.id.equals(dad.id)) {
            for (Dragon d : dragons.values()) {
                if (d.isChildOf(mom, dad)) {
                    retval.add(d);
                }
            }
            CalcOdds(retval);
        }
        return retval;
    }

}
