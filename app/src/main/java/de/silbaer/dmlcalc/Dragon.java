package de.silbaer.dmlcalc;

// import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silbaer on 13.06.16.
 */
public class Dragon {

    public  String id;
    public  String wikiName;
    public  String image; // Base-URL : http://www.bernapol.eu/
    public  String element1;
    public  String element2;
    public  String element3;
    public  String boss_vip;
    public  String breedingTime;
    public  String hatchingTime;
    public  String type;
    public  String health;
    public  String attack;
    public  String gold;
    public  String lng_pt;
    public  String lng_fr;
    public  String lng_it;
    public  String lng_de;

    public List<String> elements;

    public double odd;

    public Boolean isBoss() { return this.boss_vip.trim().equalsIgnoreCase("B"); }
    public Boolean islegendary() { return this.element1.trim().equalsIgnoreCase("legendary"); }
    public Boolean isEvent() { return this.boss_vip.trim().equalsIgnoreCase("E");  }
    public Boolean isVIP() {  return this.boss_vip.trim().equalsIgnoreCase("V");  }

    public String toString(){ return lng_de;}

    public Dragon(String jsZeile) {

        jsZeile = jsZeile.replace("[","").replace("]","").replace("\"","").trim();

        String[] splits = jsZeile.split(",");
        if (splits.length >= 13) {
            id = splits[0].trim();
            wikiName = splits[1].trim();
            image = splits[2].trim();
            element1 = splits[3].trim();
            element2 = splits[4].trim();
            element3 = splits[5].trim();
            boss_vip = splits[6].trim();
            breedingTime = splits[7].trim();
            hatchingTime = splits[8].trim();
            type = splits[9].trim();
            health = splits[10].trim();
            attack = splits[11].trim();
            gold = splits[12].trim();
        }
        if (splits.length >= 14) {
            lng_pt = splits[13].trim();
        }
        if (splits.length >= 15) {
            lng_fr = splits[14].trim();
        }
        if (splits.length >= 16) {
            lng_it = splits[15].trim();
        }
        if (splits.length >= 17) {
            lng_de = splits[16].trim();
        }
        elements = new ArrayList<String>();
        if (!element1.isEmpty()) {
            elements.add(element1);
        }
        if (!element2.isEmpty()) {
            elements.add(element2);
        }
        if (!element3.isEmpty()) {
            elements.add(element3);
        }

    }

    public Boolean isChildOf(Dragon mom, Dragon dad){
        Boolean retval = false;

        Boolean dotmcontrol = false;
        Boolean mumLegendary = false;
        Boolean dadLegendary = false;

        if (mom.id.equals(dad.id)) {
            // gleiche drachen geht nicht
            return false;
        }

        if(this.boss_vip.equals("UN")){
            // return false;
        }

        if (!this.boss_vip.isEmpty()  && !this.boss_vip.equals("V")) { // nicht VIP, aber Boss oder Event oder unreleased
            // Drogon of the week

            //if (DateTime.Now() > DOW.begin && DateTime.Now() < DOW.end && this.id == DOW.id) {
            //  if(DOW.momid == mom.id && DOW.dadid == dad.id || DOW.momid == dad.id && DOW.dadid == mom.id ){
            //    return true;
            //  }
            //}


            // Dragon of the month

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

            if (this.id.equalsIgnoreCase("sunflower") ||
                    this.id.equalsIgnoreCase("mercury") ||
                    this.id.equalsIgnoreCase("lightning") ||
                    this.id.equalsIgnoreCase("magnet") ||
                    this.id.equalsIgnoreCase("emperor")) {

                // Inkompatible Elemente ?
                if (mom.elements.size() == 1 || dad.elements.size() == 1) {
                    return false;
                }
            }
            //siren,pixie,dark machine,vortex,titan,narwhale
            if (this.id.equalsIgnoreCase("siren") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (this.id.equalsIgnoreCase("pixie") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("lightning")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("lightning")))) {
                return true;
            }
            if (this.id.equalsIgnoreCase("dark_machine") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("magnet")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("magnet")))) {
                return true;
            }
            if (this.id.equalsIgnoreCase("vortex") && ((mom.id.equalsIgnoreCase("lightning") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("lightning") && mom.id.equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (this.id.equalsIgnoreCase("titan") && ((mom.id.equalsIgnoreCase("magnet") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("magnet") && mom.id.equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (this.id.equalsIgnoreCase("narwhale") && ((mom.id.equalsIgnoreCase("magnet") && dad.id.equalsIgnoreCase("lightning")) || (dad.id.equalsIgnoreCase("magnet") && mom.id.equalsIgnoreCase("lightning")))) {
                return true;
            }
            if (this.id.equalsIgnoreCase("crystal") && ((mom.id.equalsIgnoreCase("emperor") && dad.id.equalsIgnoreCase("magnet")) || (dad.id.equalsIgnoreCase("emperor") && mom.id.equalsIgnoreCase("magnet")))) {
                return true;
            }
            if (this.elements.size() == 1) {
                //1 elements => mum & dad must have element
                if (mom.elements.contains(this.element1) && dad.elements.contains(this.element1)) {
                    return true;
                } else {
                    // JS ist hier schrott. Immer false
                    return false;
                }
            } else if (this.elements.size() == 2) {
                //2 elements => mum & dad can have his element
                // Auch hier ist das JS kaputt:
                //if ((mumHasFirstElem && dadHasFirstElem && mumHasSecondElem && dadHasSecondElem) || (mumHasFirstElem && dadHasSecondElem) || (mumHasSecondElem && dadHasFirstElem)) {
                //  return true;
                //}
                // Das erste ist ein Sonderfall und wird duch zweiten und dritten Therm aufgefangen

                if ((mom.elements.contains(this.element1) && dad.elements.contains(this.element2)) || (mom.elements.contains(this.element2) && dad.elements.contains(this.element1))) {
                    return true;
                }


            } else if (this.elements.size() == 3) {
                //3 elements => mum and dad must have all the elements
                if (this.islegendary()) {
                    return false;
                }
                Boolean e1 = false;
                Boolean e2 = false;
                Boolean e3 = false;
                Boolean m = false;
                Boolean d = false;
                if (mom.elements.contains(this.element1)) {
                    m = true;
                    e1 = true;
                }
                if (mom.elements.contains(this.element2)) {
                    m = true;
                    e2 = true;
                }
                if (mom.elements.contains(this.element3)) {
                    m = true;
                    e3 = true;
                }
                if (dad.elements.contains(this.element1)) {
                    d = true;
                    e1 = true;
                }
                if (dad.elements.contains(this.element2)) {
                    d = true;
                    e2 = true;
                }
                if (dad.elements.contains(this.element3)) {
                    d = true;
                    e3 = true;
                }

                if (e1 && e2 && e3 && m && d) {
                    return true;
                }
            }
        }
        return retval;
    }


}
