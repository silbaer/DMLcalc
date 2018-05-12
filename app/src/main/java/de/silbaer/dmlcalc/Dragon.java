package de.silbaer.dmlcalc;

// import android.os.Bundle;

import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    private  Integer breedingTime = 0;  // Seconds (Integer)
    private  Integer hatchingTime = 0;  // Seconds (Integer)
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
    public Boolean isEnchatmentBreed() {  return this.EnchatmentBreed;  }
    public Boolean isDailyLogin() {return this.DailyLogin;}

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
//    public String getHatchingTime(){return hatchingTime;}
//    public String getBreedingTime(){
//        return getBreedingTime(false);
//    }

    public Integer getHatchingTime(boolean vip) {
        Integer retval = 0;
        if(hatchingTime == 0 ) {
            if (!DMLcalc.Instance().exceptionalHatchingTimes.containsKey(id)) {
                Float hatchingFactor = 0f;
                if (islegendary()) {
                    hatchingFactor = 1.2f;
                } else if (isdivine()) {
                    hatchingFactor = 1.2f;
                } else {
                    for (Iterator<String> it = this.getElements().iterator(); it.hasNext(); ) {
                        String e = it.next();
                        if (e.equalsIgnoreCase("fire")) {
                            hatchingFactor = hatchingFactor + 1.6f;
                        } else if (e.equalsIgnoreCase("wind")) {
                            hatchingFactor = hatchingFactor + 1.6f;
                        } else if (e.equalsIgnoreCase("earth")) {
                            hatchingFactor = hatchingFactor + 1.6f;
                        } else if (e.equalsIgnoreCase("water")) {
                            hatchingFactor = hatchingFactor + 1.6f;
                        } else if (e.equalsIgnoreCase("plant")) {
                            hatchingFactor = hatchingFactor + 1.2f;
                        } else if (e.equalsIgnoreCase("metal")) {
                            hatchingFactor = hatchingFactor + 1.2f;
                        } else if (e.equalsIgnoreCase("energy")) {
                            hatchingFactor = hatchingFactor + 1.2f;
                        } else if (e.equalsIgnoreCase("void")) {
                            hatchingFactor = hatchingFactor + 1.2f;
                        } else if (e.equalsIgnoreCase("light")) {
                            hatchingFactor = hatchingFactor + 1.1f;
                        } else if (e.equalsIgnoreCase("shadow")) {
                            hatchingFactor = hatchingFactor + 1.1f;
                        }
                    }
                    hatchingFactor = hatchingFactor / this.getElements().size();
                }
                Integer bt = getBreedingTime(false);
                Integer ht1 = Math.round(bt * hatchingFactor);
                Float ht2 = ht1 / 10f / 60f;
                double ht = Math.ceil(ht2);
                hatchingTime = (int) ht * 10 * 60;
                // hatchingTime = ((int) Math.ceil( getBreedingTime(false) * hatchingFactor / 10f)) *10;
            } else {
                hatchingTime = DMLcalc.Instance().exceptionalHatchingTimes.get(id);
            }
        }
        retval = hatchingTime;
        if (vip) {
            retval =  (Math.round(hatchingTime * 0.8f));
        }
        return retval;
    }

    public Integer getBreedingTime(boolean vip){
        Integer retval = 0;
        if(breedingTime == 0 ) {
            if (!DMLcalc.Instance().exceptionalBreadingTimes.containsKey(id)) {
                if (islegendary()) {
                    breedingTime = 48 * 60 * 60;
                } else if (isdivine()) {
                    breedingTime = 96 * 60 * 60;
                } else {
                    if (this.getType().equalsIgnoreCase("U")) {
                        breedingTime = breedingTime + 2 * 60 * 60;
                    } else if (this.getType().equalsIgnoreCase("R")) {
                        breedingTime = breedingTime + 6 * 60 * 60;
                    } else if (this.getType().equalsIgnoreCase("E")) {
                        breedingTime = breedingTime + 8 * 60 * 60;
                    }
                    for (Iterator<String> it = this.getElements().iterator(); it.hasNext(); ) {
                        String e = it.next();
                        if (e.equalsIgnoreCase("fire")) {
                            breedingTime = breedingTime + 2 * 60 * 60;
                        } else if (e.equalsIgnoreCase("wind")) {
                            breedingTime = breedingTime + 2 * 60 * 60;
                        } else if (e.equalsIgnoreCase("earth")) {
                            breedingTime = breedingTime + 2 * 60 * 60;
                        } else if (e.equalsIgnoreCase("water")) {
                            breedingTime = breedingTime + 2 * 60 * 60;
                        } else if (e.equalsIgnoreCase("plant")) {
                            breedingTime = breedingTime + 4 * 60 * 60;
                        } else if (e.equalsIgnoreCase("metal")) {
                            breedingTime = breedingTime + 4 * 60 * 60;
                        } else if (e.equalsIgnoreCase("energy")) {
                            breedingTime = breedingTime + 6 * 60 * 60;
                        } else if (e.equalsIgnoreCase("void")) {
                            breedingTime = breedingTime + 6 * 60 * 60;
                        } else if (e.equalsIgnoreCase("light")) {
                            breedingTime = breedingTime + 8 * 60 * 60;
                        } else if (e.equalsIgnoreCase("shadow")) {
                            breedingTime = breedingTime + 8 * 60 * 60;
                        }
                    }
                }
            } else {
                breedingTime = DMLcalc.Instance().exceptionalBreadingTimes.get(id);
            }
        }
        retval = breedingTime;
        if (vip) {
            retval =  (Math.round(breedingTime * 0.8f));
        }
        return retval;
    }
    public String getBaseAttack() {return attack;}
    public String getBaseHealth() {return health;}
    public String getBaseGold() {return gold;}

    public String getStatText() { return "HP: " + getBaseHealth() + " / Att: " + getBaseAttack() + " / Gold/h: " + getBaseGold(); }

    public String getElementKey() {
        String retval = "";
        int elementCount = DMLcalc.Instance().elements.size();
        String tmp;
        List<String> myElemets = new ArrayList<>();
        if(DMLcalc.Instance().getDDM().equalsIgnoreCase(this.id)){
            myElemets.addAll(DMLcalc.Instance().getDdmElements());
        } else {
            myElemets.addAll(elements);
        }
        for(int i = 0; i < elementCount; i++){
            tmp = DMLcalc.Instance().elements.get(i).id;
            if(myElemets.contains( tmp)){
                retval = retval + tmp;
            }
        }
        return retval;
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
        } else  if(isEnchatmentBreed()){
            retval += "&";
        } else  if(!isBreadable()){
            retval += "+";
        }
        return retval;
    }

    public Dragon(String zeile) {

            parseNewFormat(zeile);

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
          //  breedingTime = ""; // splits[7];
          //  hatchingTime = ""; // splits[8];
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


}
