package de.silbaer.dmlcalc;

// import android.os.Bundle;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by silbaer on 13.06.16.
 */
public class Dragon {

    private  String id;
    private  String wikiName;
    private  String image; // Base-URL : http://www.bernapol.eu/
    private  String element1;
    private  String element2;
    private  String element3;
    private  String boss_vip;
    private  String breedingTime;  // Seconds (Integer)
    private  String hatchingTime;  // Seconds (Integer)
    private  String type; // C,U,R,E,L,B,D
    private  String health;
    private  String attack;
    private  String gold;
    private  String lng_pt;
    private  String lng_fr;
    private  String lng_it;
    private  String lng_de;
    private List<String> elements;
//    private double odd;  // Odd for breading. Calculated every breed

    // New extended properties from

    private boolean LimitedTime;
    private boolean ClanShop;
    private boolean Vip;
    private boolean Dungeon;
    private boolean Seasonal;
    private boolean DailyQuestPuzzle;
    private boolean FriendShipTotem;
    private boolean Referal;
    private boolean DailyLogin;
    private int DailyLoginCount;
    private boolean EnchatmentBreed;
    private boolean EnchatmentLeague;
    private boolean Arena;
    private boolean Unreleased;
    private boolean Breadable;

    private String Cost;
    private String CostType;




    public Boolean isBoss() { return this.type.trim().equalsIgnoreCase("B"); }
    public Boolean islegendary() { return this.type.trim().equalsIgnoreCase("L"); }
    public Boolean isdivine() { return this.type.trim().equalsIgnoreCase("D"); }
    public Boolean isBreadable() { return (this.Breadable || this.Vip) && !EnchatmentBreed;  }
    public Boolean isVIP() {  return this.Vip;  }
    public Boolean isUnreleased() {  return this.Unreleased;  }

    private Boolean _isBoss() { return this.boss_vip.trim().equalsIgnoreCase("B"); }
    private Boolean _islegendary() { return this.element1.trim().equalsIgnoreCase("legendary"); }
    private Boolean _isEvent() { return this.boss_vip.trim().equalsIgnoreCase("E");  }
    private Boolean _isVIP() {  return this.boss_vip.trim().equalsIgnoreCase("V");  }
    private Boolean _isUnreleased() {  return this.boss_vip.trim().equalsIgnoreCase("UN");  }

    public String getId() {return id;}
    public String getType() {return type.trim();}
//    public double getOdd() {return odd;}
//    public void setOdd(double Odd) {odd=Odd;}
    public List<String> getElements() {return elements;}
    public String getElement1() {return element1;}
    public String getElement2() {return element2;}
    public String getElement3() {return element3;}
    public String getHatchingTime(){return hatchingTime;}
    public String getBreedingTime(){
        if("0".equalsIgnoreCase(breedingTime)) {
            if(!"fireball".equalsIgnoreCase(this.getId())){
                Integer bt = 0;
                if(islegendary()) {
                     bt = 48*60*60;
                } else {
                    if(this.getType().equalsIgnoreCase("U")){
                        bt = bt + 2*60*60;
                    } else if(this.getType().equalsIgnoreCase("R")){
                        bt = bt + 6*60*60;
                    } else if(this.getType().equalsIgnoreCase("E")){
                        bt = bt + 8*60*60;
                    }
                    for (Iterator<String> it = this.getElements().iterator(); it.hasNext();){
                        String e = it.next();
                        if(e.equalsIgnoreCase("fire")){
                            bt = bt + 2*60*60;
                        } else if(e.equalsIgnoreCase("wind")){
                            bt = bt + 2*60*60;
                        } else if(e.equalsIgnoreCase("earth")){
                            bt = bt + 2*60*60;
                        } else if(e.equalsIgnoreCase("water")){
                            bt = bt + 2*60*60;
                        } else if(e.equalsIgnoreCase("plant")){
                            bt = bt + 4*60*60;
                        } else if(e.equalsIgnoreCase("metal")){
                            bt = bt + 4*60*60;
                        } else if(e.equalsIgnoreCase("energy")){
                            bt = bt + 6*60*60;
                        } else if(e.equalsIgnoreCase("void")){
                            bt = bt + 6*60*60;
                        } else if(e.equalsIgnoreCase("light")){
                            bt = bt + 8*60*60;
                        } else if(e.equalsIgnoreCase("shadow")){
                            bt = bt + 8*60*60;
                        }
                    }
                }
                breedingTime = bt.toString();
            }
        }
        return breedingTime;
    }

    public String toString(){

        String retval = id;

        try {
            DMLcalc c = DMLcalc.Instance();
            try {
                if (c != null) {
                    String tmp = c.getStringResourceByName("dragon_" + id);
                    if(!tmp.equals("dragon_" + id)) {
                   //     Log.d("DragonResourde",tmp);
                        retval = tmp;
                    }
                }
            } catch (Exception ex) {
                String e = ex.toString();
            }
        } catch (Exception eex) {
            String e = eex.toString();
        }
       // return retval;


        if(isVIP()){
            retval += "*";
        } else  if(!isBreadable()){
            retval += "+";
        }
        return retval;
    }

    public Dragon(String zeile, boolean newFormat) {
        if(newFormat){
            parseNewFormat(zeile);
        } else {
          parseOldFormat(zeile);
        }
    }

    private void parseNewFormat(String zeile) {
        String[] splits = zeile.split(";");
        if (splits.length >= 26) {
            id = splits[0].trim();
//            icon = splits[1].trim();
            String tmp = splits[2].trim();
            type = tmp.substring(0,1);
            tmp = splits[3].trim();
            String[] tmpSplits = tmp.split("/");
            elements = new ArrayList<String>();
            for (String e: tmpSplits) {
                elements.add(e.toLowerCase());
            }
            if(elements.size()>0) {
                element1 = elements.get(0);
            }
            if(elements.size()>1) {
                element2 = elements.get(1);
            }
            if(elements.size()>2) {
                element3 = elements.get(2);
            }
            attack = splits[4];
            health = splits[5];
            gold = splits[6];
            breedingTime = splits[7];
            hatchingTime = splits[8];
            Cost = splits[9];
            CostType = splits[10];
            LimitedTime = Boolean.parseBoolean(splits[11]);
            ClanShop = Boolean.parseBoolean(splits[12]);
            Vip = Boolean.parseBoolean(splits[13]);
            Dungeon = Boolean.parseBoolean(splits[14]);
            Seasonal = Boolean.parseBoolean(splits[15]);
            DailyQuestPuzzle = Boolean.parseBoolean(splits[16]);
            FriendShipTotem = Boolean.parseBoolean(splits[17]);
            Referal = Boolean.parseBoolean(splits[18]);
            DailyLogin = Boolean.parseBoolean(splits[19]);
            DailyLoginCount = Integer.parseInt( splits[20]);
            EnchatmentBreed = Boolean.parseBoolean(splits[21]);
            EnchatmentLeague =Boolean.parseBoolean( splits[22]);
            Arena = Boolean.parseBoolean(splits[23]);
            Unreleased = Boolean.parseBoolean(splits[24]);
            Breadable = Boolean.parseBoolean(splits[25]);
        }
    }

    private void  parseOldFormat(String jsZeile) {

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

        LimitedTime = _isEvent();
        Vip = _isVIP();
        Unreleased = _isUnreleased();


    }
//
//    private Boolean isChildOf(Dragon mom, Dragon dad){
//        Boolean retval = false;
//
//        Boolean dotmcontrol = false;
//        Boolean mumLegendary = false;
//        Boolean dadLegendary = false;
//
//        if (mom.id.equals(dad.id)) {
//            // gleiche drachen geht nicht
//            return false;
//        }
//
//        if(this.boss_vip.equals("UN")){
//            // return false;
//        }
//
//        if (!this.boss_vip.isEmpty()  && !this.boss_vip.equals("V")) { // nicht VIP, aber Boss oder Event oder unreleased
//            // Drogon of the week
//
//            //if (DateTime.Now() > DOW.begin && DateTime.Now() < DOW.end && this.id == DOW.id) {
//            //  if(DOW.momid == mom.id && DOW.dadid == dad.id || DOW.momid == dad.id && DOW.dadid == mom.id ){
//            //    return true;
//            //  }
//            //}
//
//
//            // Dragon of the month
//
//            //if (DateTime.Now() > DOM.begin && DateTime.Now() < DOM.end && this.id == DOM.id) {
//            //List<string> momDadElements = new List<string>();
//            //momDadElements.AddRange(mom.elements);
//            //momDadElements.AddRange(dad.elements);
//            // if(momDadElements.contains(DOM.element1) && momDadElements.contains(DOM.element2) && momDadElements.contains(DOM.element3) && momDadElements.contains(DOM.element4) {
//            ////  return true;
//            ////}
//            //}
//
//            return false;
//        }
//
//        /** SPECIAL BREED **/
//        //breed with legendary
//
////        List<string> mumOrDadLegendaryListElements = new List<string>();
//
//        if (mom.islegendary()) {
////            mumOrDadLegendaryListElements.AddRange(mom.elements);
//        }
//        if (dad.islegendary()) {
////            mumOrDadLegendaryListElements.AddRange(dad.elements);
//        }
////        mumOrDadLegendaryListElements.RemoveAll(m => m == "legendary");
//
//        if (mom.islegendary() && dad.islegendary()) {
////            if (this.isBoss() || this.isEvent() || this.islegendary) {
////                return false;
////            } else {
////                if (this.elements.Count == 2 &&
////                        this.id != "sunflower" &&
////                        this.id != "mercury" &&
////                        this.id != "lightning" &&
////                        this.id != "magnet" &&
////                        this.id != "emperor") {
////                    return true;
////                } else {
////                    return false;
////                }
////            }
//        }
//        if (dad.islegendary() || mom.islegendary()) {
//            return false; // Lassen wir mal weg....
////            if (!this.islegendary()) {
////            } else {
////                return false;
////            }
//        } else {
//            //sunflower, mercury, lightning, magnet, emperor
//
//            if (this.id.equalsIgnoreCase("sunflower") ||
//                    this.id.equalsIgnoreCase("mercury") ||
//                    this.id.equalsIgnoreCase("lightning") ||
//                    this.id.equalsIgnoreCase("magnet") ||
//                    this.id.equalsIgnoreCase("emperor")) {
//
//                // Inkompatible Elemente ?
//                if (mom.elements.size() == 1 || dad.elements.size() == 1) {
//                    return false;
//                }
//            }
//            //siren,pixie,dark machine,vortex,titan,narwhale
//            if (this.id.equalsIgnoreCase("siren") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("mercury")))) {
//                return true;
//            }
//            if (this.id.equalsIgnoreCase("pixie") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("lightning")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("lightning")))) {
//                return true;
//            }
//            if (this.id.equalsIgnoreCase("dark_machine") && ((mom.id.equalsIgnoreCase("sunflower") && dad.id.equalsIgnoreCase("magnet")) || (dad.id.equalsIgnoreCase("sunflower") && mom.id.equalsIgnoreCase("magnet")))) {
//                return true;
//            }
//            if (this.id.equalsIgnoreCase("vortex") && ((mom.id.equalsIgnoreCase("lightning") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("lightning") && mom.id.equalsIgnoreCase("mercury")))) {
//                return true;
//            }
//            if (this.id.equalsIgnoreCase("titan") && ((mom.id.equalsIgnoreCase("magnet") && dad.id.equalsIgnoreCase("mercury")) || (dad.id.equalsIgnoreCase("magnet") && mom.id.equalsIgnoreCase("mercury")))) {
//                return true;
//            }
//            if (this.id.equalsIgnoreCase("narwhale") && ((mom.id.equalsIgnoreCase("magnet") && dad.id.equalsIgnoreCase("lightning")) || (dad.id.equalsIgnoreCase("magnet") && mom.id.equalsIgnoreCase("lightning")))) {
//                return true;
//            }
//            if (this.id.equalsIgnoreCase("crystal") && ((mom.id.equalsIgnoreCase("emperor") && dad.id.equalsIgnoreCase("magnet")) || (dad.id.equalsIgnoreCase("emperor") && mom.id.equalsIgnoreCase("magnet")))) {
//                return true;
//            }
//            if (this.elements.size() == 1) {
//                //1 elements => mum & dad must have element
//                if (mom.elements.contains(this.element1) && dad.elements.contains(this.element1)) {
//                    return true;
//                } else {
//                    // JS ist hier schrott. Immer false
//                    return false;
//                }
//            } else if (this.elements.size() == 2) {
//                //2 elements => mum & dad can have his element
//                // Auch hier ist das JS kaputt:
//                //if ((mumHasFirstElem && dadHasFirstElem && mumHasSecondElem && dadHasSecondElem) || (mumHasFirstElem && dadHasSecondElem) || (mumHasSecondElem && dadHasFirstElem)) {
//                //  return true;
//                //}
//                // Das erste ist ein Sonderfall und wird duch zweiten und dritten Therm aufgefangen
//
//                if ((mom.elements.contains(this.element1) && dad.elements.contains(this.element2)) || (mom.elements.contains(this.element2) && dad.elements.contains(this.element1))) {
//                    return true;
//                }
//
//
//            } else if (this.elements.size() == 3) {
//                //3 elements => mum and dad must have all the elements
//                if (this.islegendary()) {
//                    return false;
//                }
//                Boolean e1 = false;
//                Boolean e2 = false;
//                Boolean e3 = false;
//                Boolean m = false;
//                Boolean d = false;
//                if (mom.elements.contains(this.element1)) {
//                    m = true;
//                    e1 = true;
//                }
//                if (mom.elements.contains(this.element2)) {
//                    m = true;
//                    e2 = true;
//                }
//                if (mom.elements.contains(this.element3)) {
//                    m = true;
//                    e3 = true;
//                }
//                if (dad.elements.contains(this.element1)) {
//                    d = true;
//                    e1 = true;
//                }
//                if (dad.elements.contains(this.element2)) {
//                    d = true;
//                    e2 = true;
//                }
//                if (dad.elements.contains(this.element3)) {
//                    d = true;
//                    e3 = true;
//                }
//
//                if (e1 && e2 && e3 && m && d) {
//                    return true;
//                }
//            }
//        }
//        return retval;
//    }
//

}
