package de.silbaer.dmlcalc;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.ArraySet;
import android.util.Pair;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by silbaer on 13.06.16.
 */
public class DMLcalc extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{

    public class SpecialBreed{
        public String momId;
        public String dadId;
        public String childId;
        public boolean isEnchanted;

        public SpecialBreed(String dadID, String momID, String childID, boolean isEnchanted){
            this.momId = momID;
            this.dadId = dadID;
            this.childId = childID;
            this.isEnchanted = isEnchanted;
        }

        public boolean checkMomDadChild(Dragon mom, Dragon dad, Dragon child) {
            if(child.getId().equalsIgnoreCase(childId)){
                return checkMomDad(mom,dad);
            }
            return false;
        }

        public boolean checkMomDad(Dragon mom, Dragon dad) {

            if(mom.getId().equalsIgnoreCase(momId) && dad.getId().equalsIgnoreCase(dadId)
                    || dad.getId().equalsIgnoreCase(momId) && mom.getId().equalsIgnoreCase(dadId) ){
                return true;
            }

            return false;
        }
    }

    public String getStringResourceByName(String aString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        if(resId == 0) {
            return aString;

        }
        return getString(resId);
    }

    public int getDrawableIdentifierByName(String aString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(aString, "drawable", packageName);

        return resId;
    }

    public static Application getApplication() {
        return _instance;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }


    // Singleton
    private static DMLcalc _instance;
    public static DMLcalc Instance() {return _instance;}


    private SharedPreferences sharedPref;
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        vipDragons = sharedPreferences.getBoolean("pref_vipdragons",false);
  //      enchantDragons = sharedPreferences.getBoolean("pref_enchantbreed",false);
        this.clearCache();
    }

    public interface breedingResponse{
        void breedingResult(List<Pair<Dragon,Double>> result);
    }
    public interface howToResponse{
        void howToResult(List<Pair<Pair<Dragon,Dragon>,Double>> result);
    }


    public ArrayList<element> elements = new ArrayList<>();

    private   String PREFS_NAME ;

    private String DDM= null;
    private List<String> DDM_Elements;

    private String DDW=null;
    private String DDW_Mom=null;
    private String DDW_Dad=null;

    private Boolean vipDragons;
    private Boolean enchantDragons;

    private List<SpecialBreed> specialBreeds = null;

    public Map<String,Dragon> dragons = new Hashtable<String,Dragon>();
//    public Dictionary<string, Element> elements = new Dictionary<string,Element>();

    Map<String,Double> odds;

 //   private Hashtable<String,Object> _howToCache;
    private Hashtable<String,Object> _breedCache;

    private Hashtable<String,ArrayList<Dragon>> dragonsByElementkey;
    private Hashtable<String,ArrayList<Dragon>> breedresultsByElementkey;

    public DMLcalc() {


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
        this.clearCache();
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
        this.clearCache();
    }

    public String getDDM() {
        if(DDM == null){
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
       //     DDM_Elements.clear();
        }
    }
    public String getDDM_e1() {
        if(DDM_Elements .isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() > 0) {
            return DDM_Elements.get(0);
        }
        return "";
    }
    public String getDDM_e2() {
        if(DDM_Elements.isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() >1 ) {
        return DDM_Elements.get(1);
        }
        return "";
    }
    public String getDDM_e3() {
        if(DDM_Elements.isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() > 2) {
        return DDM_Elements.get(2);
        }
        return "";
    }
    public String getDDM_e4() {
        if(DDM_Elements.isEmpty()){
            readDdmElementsFromPreferences();
        }
        if(DDM_Elements.size() > 3) {
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
        if(DDW == null){
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            DDW = prefs.getString(getString(R.string.DDW),"");
        }
        return DDW;
    }
    public String getDDW_mom() {
        if(DDW_Mom == null){
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            DDW_Mom = prefs.getString(getString(R.string.DDW_mom),"");
        }
        return DDW_Mom;
    }
    public String getDDW_dad() {
        if(DDW_Dad == null){
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            DDW_Dad = prefs.getString(getString(R.string.DDW_dad),"");
        }
        return DDW_Dad;
    }

    public ArrayList<Dragon> getDragons(boolean withVIP, boolean withEnchanted, boolean withDDWparents, boolean withDdwDdm, boolean filterBreedable) {
        ArrayList<Dragon> retval = new ArrayList<Dragon>();
        retval.addAll(dragons.values());
        for(int i = retval.size()-1; i >= 0; i--){
            Dragon d = retval.get(i);
            if(d.isBoss() || d.isUnreleased()){
                retval.remove(d);
                continue;
            }
            if(filterBreedable && !d.isBreadable()){
                if(withDDWparents && d.getId().equalsIgnoreCase(getDDW_dad())){
                    continue;
                }
                if(withDDWparents && d.getId().equalsIgnoreCase(getDDW_mom())){
                    continue;
                }
                if(withDdwDdm && d.getId().equalsIgnoreCase(getDDW())){
                    continue;
                }
                if(withDdwDdm && d.getId().equalsIgnoreCase(getDDM())){
                    continue;
                }
                if(withVIP  &&  d.isVIP()){
                    continue;
                }
                if(withEnchanted  &&  d.isEnchatmentBreed()){
                    continue;
                }
                retval.remove(d);
            }
            if(!withVIP  &&  d.isVIP()){
                retval.remove(d);
            }
            if(!withEnchanted  &&  d.isEnchatmentBreed()){
                retval.remove(d);
            }
        }
        return retval;
    }

    public ArrayList<Dragon> getDragons4HowtoCombo() {

        return getDragons(vipDragons,enchantDragons,false,true,true);
        // Abweichend von unten immer VIP

//        ArrayList<Dragon> retval = new ArrayList<Dragon>();
//        retval.addAll(dragons.values());
//        for(int i = retval.size()-1; i >= 0; i--){
//            Dragon d = retval.get(i);
//            if((d.isBoss() || d.islegendary() || d.isdivine() || d.isUnreleased()) && !d.getId().equalsIgnoreCase(getDDW_dad()) && !d.getId().equalsIgnoreCase(getDDW_mom())){
//                retval.remove(d);
//            }
//            if(!vipDragons  &&  d.isVIP()){
//                retval.remove(d);
//            }
//        }
//        return retval;

    }
    public ArrayList<Dragon> getDragons4BreedCombo() {

        return getDragons(true,true,true,false,false);



    }

    public ArrayList<Dragon> getDragons() {
        return getDragons(true,true,true,true,false);
//        ArrayList<Dragon> retval = new ArrayList<Dragon>();
//        retval.addAll(dragons.values());
//        return retval;
    }

    private void updateBreedResults() {
        breedresultsByElementkey = new Hashtable<>();

        for (Dragon d : dragons.values()) {

            if(d.isBreadable() || (!vipDragons && d.isVIP()) || d.getId().equalsIgnoreCase(getDDM())  ) {
                String elementKey = d.getElementKey();
                if (!breedresultsByElementkey.containsKey(elementKey)) {
                    breedresultsByElementkey.put(elementKey, new ArrayList<Dragon>());
                }
                breedresultsByElementkey.get(elementKey).add(d);
            }
        }

    }

//    public ArrayList<Dragon> getDragonsToShow() {
//        ArrayList<Dragon> retval = new ArrayList<Dragon>();
//        retval.addAll(dragons.values());
//        for(int i = retval.size()-1; i >= 0; i--){
//            Dragon d = retval.get(i);
//            if(d.isBoss() || d.isUnreleased()){
//                retval.remove(d);
//            }
//            if(!vipDragons  &&  d.isVIP()){
//                retval.remove(d);
//            }
//        }
//        return retval;
//    }

    public void clearCache(){
        _breedCache.clear();
    }
    public void saveCache(){


//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(_breedCache);
//        editor.putString("breedCache",json);
//        editor.commit();

    }
    public void loadCache(){
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = prefs.getString("breedCache","");
//        _breedCache = gson.fromJson(json,Hashtable.class);
//        if(_breedCache == null){
//            _breedCache = new Hashtable<>();
//        }
//        w_db = mDbHelper.getWritableDatabase();
//        r_db = mDbHelper.getReadableDatabase();
        _breedCache = new Hashtable<>();
    }

//    BreedCacheDbHelper mDbHelper;
//    SQLiteDatabase w_db;
//    SQLiteDatabase r_db;

    private Object getFromDb(String key) {
        Object retval = null;
//        String value="";
//      String[] projection = {
//              BreedCacheContract.BreedCacheEntry._ID,
//              BreedCacheContract.BreedCacheEntry.COLUMN_NAME_KEY,
//              BreedCacheContract.BreedCacheEntry.COLUMN_NAME_VALUE
//      };
//        String selection = BreedCacheContract.BreedCacheEntry.COLUMN_NAME_KEY + " = ?";
//        String[] selectionArgs = { key };
//        String sortOrder = BreedCacheContract.BreedCacheEntry.COLUMN_NAME_KEY + " DESC";
//        Cursor cursor = r_db.query(
//                BreedCacheContract.BreedCacheEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder
//        );
//
//        if(cursor.moveToNext()){
//        value = cursor.getString(
//                cursor.getColumnIndexOrThrow(BreedCacheContract.BreedCacheEntry.COLUMN_NAME_VALUE));
//        }
//        cursor.close();
//        Gson gson = new Gson();
//        retval = gson.fromJson(value,List.class);
        if(_breedCache.containsKey(key)){
            retval = _breedCache.get(key);
        }

      return retval;
    }

    private void saveInDb(String key, Object value){
        // Gets the data repository in write mode
 //       SQLiteDatabase db = mDbHelper.getWritableDatabase();
//
//// Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//        values.put(BreedCacheContract.BreedCacheEntry.COLUMN_NAME_KEY,key);
//        Gson gson = new Gson();
//        String json = gson.toJson(value);
//
//        values.put(BreedCacheContract.BreedCacheEntry.COLUMN_NAME_VALUE, json);
//
//// Insert the new row, returning the primary key value of the new row
//        long newRowId = w_db.insert(BreedCacheContract.BreedCacheEntry.TABLE_NAME, null, values);
        _breedCache.put(key,value);
    }

    @Override
    public void onCreate(){
        PREFS_NAME =  getResources().getString(R.string.PREFS_NAME);
        super.onCreate();
        _instance = this;
        odds = new Hashtable<String,Double>();
        odds.put("C",48d);
        odds.put("U",21d);
        odds.put("R",15d);
        odds.put("E",10d);
        odds.put("L", 6d);
        odds.put("D", 6d);
        DDM_Elements = new ArrayList<>();

        elements.add(new element("fire"));
        elements.add(new element("wind"));
        elements.add(new element("earth"));
        elements.add(new element("water"));
        elements.add(new element("plant"));
        elements.add(new element("metal"));
        elements.add(new element("energy"));
        elements.add(new element("void"));
        elements.add(new element("light"));
        elements.add(new element("shadow"));
        elements.add(new element("legendary"));
        elements.add(new element("divine"));

        //_breedCache = new Hashtable<> ();
        // loadCache();

    //    mDbHelper = new BreedCacheDbHelper(getContext());
        loadCache();



        AssetManager assetManager = getAssets();

        try {
//            InputStreamReader is = new InputStreamReader(getAssets().open("dragon.list"));
            InputStreamReader is = new InputStreamReader(getAssets().open("newDragon.list"));
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

            dragonsByElementkey = new Hashtable<>();

            Dragon d;
            String[] lines = dragonJS.split("[\\n]+");
            for (String jsline : lines) {
                String l = jsline.trim();
                if(!l.isEmpty()){
                    d = new Dragon(l,true);
                    dragons.put(d.getId(),d);
                    String elementKey = d.getElementKey();
                    if(!dragonsByElementkey.containsKey(elementKey)){
                        dragonsByElementkey.put(elementKey,new ArrayList<Dragon>());
                    }
                    dragonsByElementkey.get(elementKey).add(d);
                }
            }
            Context context =   getBaseContext();
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPref.registerOnSharedPreferenceChangeListener(this);
            vipDragons = sharedPref.getBoolean("pref_vipdragons",false);
            enchantDragons = true; //sharedPref.getBoolean("pref_enchantbreed",false);

            specialBreeds = new ArrayList<>();
            // Breadable legendary

            specialBreeds.add(new SpecialBreed( "emperor","magnet","crystal",false));
            specialBreeds.add(new SpecialBreed("magnet","sunflower","dark_machine",false));
            specialBreeds.add(new SpecialBreed("lightning","magnet","narwhale",false));
            specialBreeds.add(new SpecialBreed("lightning","sunflower","pixie",false));
            specialBreeds.add(new SpecialBreed("mercury","sunflower","siren",false));
            specialBreeds.add(new SpecialBreed("magnet","mercury","titan",false));
            specialBreeds.add(new SpecialBreed("lightning","mercury","vortex",false));
            // enchantend breed
            specialBreeds.add(new SpecialBreed("bee","tree","amber",true));
            specialBreeds.add(new SpecialBreed("clay","toxic","ant",true));
            specialBreeds.add(new SpecialBreed("venom","orange","beetle",true));
            specialBreeds.add(new SpecialBreed("agave","cloud","bloom",true));
            specialBreeds.add(new SpecialBreed("seed","clownfish","box",true));
            specialBreeds.add(new SpecialBreed("cockatoo","dark_mech","briar",true));
            specialBreeds.add(new SpecialBreed("golden_crow","mist","cockatoo",true));
            specialBreeds.add(new SpecialBreed("fossil","frosty","crumbly",true));
            specialBreeds.add(new SpecialBreed("alien","armored","dark_mech",true));
            specialBreeds.add(new SpecialBreed("rocker","magnet","disco_ball",true));
            specialBreeds.add(new SpecialBreed("brick","lightfish","fossil",true));
            specialBreeds.add(new SpecialBreed("yeti","frostbite","frosty",true));
            specialBreeds.add(new SpecialBreed("rogue","banana","gorilla",true));
            specialBreeds.add(new SpecialBreed("lava","blueflame","hellfire",true));
            specialBreeds.add(new SpecialBreed("elemental","witch","hypoestes",true));
            specialBreeds.add(new SpecialBreed("brick","tribal","leopard",true));
            specialBreeds.add(new SpecialBreed("geiger","ice","origami",true));
            specialBreeds.add(new SpecialBreed("sunflower","tribal","owl",true));
            specialBreeds.add(new SpecialBreed("disco_ball","superhero","plushie",true));
            specialBreeds.add(new SpecialBreed("melon","tree","sea_turtle",true));
            specialBreeds.add(new SpecialBreed("jelly","tiger","superhero",true));



        } catch (IOException e) {
            e.printStackTrace();
        }



        updateBreedResults();


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
                d = new Dragon(l,true);
                dragons.put(d.getId(),d);
            }
        }
    }



    public List<Pair<Dragon,Double>> CalcOdds( List<Dragon> resultList) {
        double sum = 0;
        List<Pair<Dragon,Double>> retval = new ArrayList<>();
        for (Dragon d : resultList) {
            if (!d.isUnreleased()
                    && !d.isBoss()
                    //    && !d.isEvent()
                    && !d.isVIP()
                    ) {
                if (odds.containsKey(d.getType())) {
                    sum += odds.get(d.getType());
                }
            }
        }
        for (Dragon d : resultList) {
            if (odds.containsKey(d.getType())) {
                retval.add(new Pair<>(d,100/sum * odds.get(d.getType())));
         //       d.setOdd(  100/sum * odds.get(d.getType()));
            }
        }
        return retval;
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

    public void howToBreed(howToResponse resultDelegate, Dragon son){
        howToAsyncTask task = new howToAsyncTask(resultDelegate);
        task.execute(son);
    }
    private class howToAsyncTask extends AsyncTask<Dragon,Void, List<Pair<Pair<Dragon,Dragon>,Double>> >{
        private howToResponse delegate = null;
        ProgressDialog ringProgressDialog ;

        public howToAsyncTask(howToResponse asyncResponse){
            delegate = asyncResponse;
        }

        @Override
        protected void onPreExecute(){
            ringProgressDialog = ProgressDialog.show( (Context)delegate , getString(R.string.txt_howProgressTitle),getString(R.string.txt_howProgressMsg),true);
            ringProgressDialog.setCancelable(false);
        }

        @Override
        protected List<Pair<Pair<Dragon,Dragon>,Double>> doInBackground(Dragon... params){
            return _howToBreed(params[0]);
        }

        @Override
        protected void onPostExecute(List<Pair<Pair<Dragon,Dragon>,Double>> result){
            ringProgressDialog.dismiss();
            if(delegate != null){
                delegate.howToResult(result);
            }
        }

    }

    public void breed(breedingResponse resultDelegate, Dragon mom, Dragon dad){
        breedAsyncTask task = new breedAsyncTask(resultDelegate);
        task.execute(mom,dad);
    }

    // http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a

    private class breedAsyncTask extends AsyncTask<Dragon,Void, List<Pair<Dragon,Double>> >{
        private breedingResponse delegate = null;
        ProgressDialog ringProgressDialog ;

        public breedAsyncTask(breedingResponse asyncResponse){
            delegate = asyncResponse;
        }

        @Override
        protected void onPreExecute(){
            ringProgressDialog = ProgressDialog.show( (Context)delegate , getString(R.string.txt_breedProgressTitle),getString(R.string.txt_breedProgressMsg),true);
            ringProgressDialog.setCancelable(false);
        }

        @Override
        protected List<Pair<Dragon,Double>> doInBackground(Dragon... params){
            return _breed(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(List<Pair<Dragon,Double>> result){
            ringProgressDialog.dismiss();
            if(delegate != null){
                delegate.breedingResult(result);
            }
        }

    }

    private List<Pair<Pair<Dragon,Dragon>,Double>> _howToBreed(Dragon son) {
        List<Pair<Pair<Dragon,Dragon>,Double>> retval;

        retval = (List<Pair<Pair<Dragon,Dragon>,Double>>) getFromDb(son.getId());

        if(retval == null){
            retval = new ArrayList<>();
//        ArrayList< Dragon> dl = new ArrayList<>(dragons.values());
            ArrayList<Dragon> dl = getDragons(vipDragons,true,true,true,false);
            for (int x = dl.size() - 1; x >= 0 ; x--) {
                if(!canBeDad(dl.get(x),son)){
                    dl.remove(x);
                }
            }
            for (int x = 0; x < dl.size() - 1; x++) {

                for (int y = x + 1; y < dl.size(); y++) {
                    if (!dl.get(x).getId().equalsIgnoreCase(son.getId())
                            && !dl.get(y).getId().equalsIgnoreCase(son.getId())) {
                        if (isChild(dl.get(x), dl.get(y), son)) {
                            List<Pair<Dragon,Double>> tmp = _breed(dl.get(x), dl.get(y),true);
                            for (Pair<Dragon,Double> dp : tmp) {
                                if (dp.first.getId().equalsIgnoreCase(son.getId())) {
                                    retval.add(new Pair<Pair<Dragon, Dragon>, Double>(new Pair<Dragon, Dragon>(dl.get(x), dl.get(y)), dp.second));
                                }
                            }
                        }
                    }
                }
            }
            saveInDb(son.getId(),retval);
        }
        return retval;
    }
    private boolean canBeDad(Dragon dad, Dragon son) {
       // boolean retval = false;
        if(dad.getId().equalsIgnoreCase( getDDW_mom()) || dad.getId().equalsIgnoreCase(getDDW_dad()) ) {
            return true;
          //  retval = true;
        }
        if(getDDM().equalsIgnoreCase(son.getId())) {
            for (String e : getDdmElements()) {
                if (dad.getElements().contains(e)) {
                    //     retval = true;
                    return true;
                }
            }
        } else {
            for (String e : son.getElements()) {
                if (dad.getElements().contains(e)) {
                    // retval = true;
                    return true;
                }
            }
        }
        String sonID = son.getId();
        for (SpecialBreed b: specialBreeds) {
            if(b.childId.equalsIgnoreCase(sonID)){
                return true;
            }
        }

        //return retval;
        return false;
    }

    private  List<Pair<Dragon,Double>> _breed(Dragon mom, Dragon dad) {
        return _breed(mom,dad,enchantDragons);
    }

    private  List<Pair<Dragon,Double>> _breed(Dragon mom, Dragon dad, boolean withEnchantment) {
        List<Pair<Dragon,Double>> retval;
        List<Pair<Dragon,Double>> retval2;
        List<Dragon> dragList;
        String key = mom.getId() + dad.getId();
        retval = (List<Pair<Dragon,Double>>) getFromDb(key);

      //  retval2 = _breed2( mom,  dad);
      //  Date start = new Date();

        if (retval == null) {
            retval = _breed2( mom,  dad,withEnchantment);

//            dragList = new ArrayList<Dragon>();
//            if (dad != null && mom != null && !mom.getId().equals(dad.getId())) {
//                ArrayList<Dragon> drags = getDragons(vipDragons,withEnchantment,false,true,true);
//                for (Dragon d : drags) {
//                    if (isChild(mom, dad, d)) {
//                        dragList.add(d);
//                    }
//                }
//                retval =  CalcOdds(dragList);
//            }

            saveInDb(key, retval);
        }
//        Date mid = new Date();
//        retval2 = _breed2( mom,  dad);
//        Date end = new Date();
//
//        long b1 = mid.getTime() - start.getTime();
//        long b2 = end.getTime() - mid.getTime();


        return retval;
    }

    public String getElementKey(String e1, String e2) {
        ArrayList<String> al = new ArrayList<>();
        al.add(e1);
        al.add(e2);
        return getElementKey(al);
    }

    public String getElementKey(String e1, String e2, String e3) {
        ArrayList<String> al = new ArrayList<>();
        al.add(e1);
        al.add(e2);
        al.add(e3);
        return getElementKey(al);
    }

    public String getElementKey(String e1, String e2, String e3, String e4) {
        ArrayList<String> al = new ArrayList<>();
        al.add(e1);
        al.add(e2);
        al.add(e3);
        al.add(e4);
        return getElementKey(al);
    }

    public String getElementKey(List<String> myElements) {
        String retval = "";
        int elementCount = elements.size();
        String tmp;
        for(int i = 0; i < elementCount; i++){
            tmp = elements.get(i).id;
            if(myElements.contains( tmp)){
                retval = retval + tmp;
            }
        }
        return retval;
    }

    private  List<Pair<Dragon,Double>> _breed2(Dragon mom, Dragon dad) {
        return _breed2(mom,dad,enchantDragons);
    }

    private  List<Pair<Dragon,Double>> _breed2(Dragon mom, Dragon dad, boolean withEchant) {
        HashSet<String> elementKeys = new HashSet<String>();
        List<Dragon> dragList;
        List<Pair<Dragon,Double>> retval;
        int iM, iD, i;
        for( iM = 0; iM < mom.getElements().size(); iM++){
            for( iD = 0; iD < dad.getElements().size(); iD++){
                elementKeys.add(getElementKey(mom.getElements().get(iM),dad.getElements().get(iD)));
            }
        }
        if(mom.getElements().size() == 2){
            for( iD = 0; iD < dad.getElements().size(); iD++){
                elementKeys.add(getElementKey(dad.getElements().get(iD),mom.getElement1(),mom.getElement2()));
            }
        }
        if(mom.getElements().size() == 3){
            for( iD = 0; iD < dad.getElements().size(); iD++){
                elementKeys.add(getElementKey(dad.getElements().get(iD),mom.getElement1(),mom.getElement2()));
                elementKeys.add(getElementKey(dad.getElements().get(iD),mom.getElement1(),mom.getElement3()));
                elementKeys.add(getElementKey(dad.getElements().get(iD),mom.getElement2(),mom.getElement3()));
            }
        }
        if(dad.getElements().size() == 2){
            for( iM = 0; iM < mom.getElements().size(); iM++){
                elementKeys.add(getElementKey(mom.getElements().get(iM),dad.getElement1(),dad.getElement2()));
            }
        }
        if(dad.getElements().size() == 3){
            for( iM = 0; iM < mom.getElements().size(); iM++){
                elementKeys.add(getElementKey(mom.getElements().get(iM),dad.getElement1(),dad.getElement2()));
                elementKeys.add(getElementKey(mom.getElements().get(iM),dad.getElement1(),dad.getElement3()));
                elementKeys.add(getElementKey(mom.getElements().get(iM),dad.getElement2(),dad.getElement3()));
            }
        }

        //DDM
        if(mom.getElements().size() == 1 && dad.getElements().size() == 3) {
            elementKeys.add(getElementKey(mom.getElement1(),dad.getElement1(),dad.getElement2(),dad.getElement3()));
        }
        if(dad.getElements().size() == 1 && mom.getElements().size() == 3) {
            elementKeys.add(getElementKey(dad.getElement1(),mom.getElement1(),mom.getElement2(),mom.getElement3()));
        }
        if(mom.getElements().size() == 2) {
            if (dad.getElements().size() == 2) {
                elementKeys.add(getElementKey(mom.getElement1(), dad.getElement1(), dad.getElement2(), mom.getElement2()));
            } else if(dad.getElements().size() == 3){
                elementKeys.add(getElementKey(mom.getElement1(),dad.getElement1(),dad.getElement2(),dad.getElement3()));
                elementKeys.add(getElementKey(mom.getElement2(),dad.getElement1(),dad.getElement2(),dad.getElement3()));
            }
        }
        if(mom.getElements().size() == 3) {
            if (dad.getElements().size() == 2) {
                elementKeys.add(getElementKey(dad.getElement1(),mom.getElement1(),mom.getElement2(),mom.getElement3()));
                elementKeys.add(getElementKey(dad.getElement2(),mom.getElement1(),mom.getElement2(),mom.getElement3()));
            } else if (dad.getElements().size() == 3) {
                // 1/2 & 1/2
                // 1/2 & 1/3
                // 1/2 & 2/3
                elementKeys.add(getElementKey(mom.getElement1(), mom.getElement2(), dad.getElement1(), dad.getElement2()));
                elementKeys.add(getElementKey(mom.getElement1(), mom.getElement2(), dad.getElement1(), dad.getElement3()));
                elementKeys.add(getElementKey(mom.getElement1(), mom.getElement2(), dad.getElement2(), dad.getElement3()));

                // 1/3 & 1/2
                // 1/3 & 1/3
                // 1/3 & 2/3
                elementKeys.add(getElementKey(mom.getElement1(), mom.getElement3(), dad.getElement1(), dad.getElement2()));
                elementKeys.add(getElementKey(mom.getElement1(), mom.getElement3(), dad.getElement1(), dad.getElement3()));
                elementKeys.add(getElementKey(mom.getElement1(), mom.getElement3(), dad.getElement2(), dad.getElement3()));

                // 2/3 & 1/2
                // 2/3 & 1/3
                // 2/3 & 2/3
                elementKeys.add(getElementKey(mom.getElement2(), mom.getElement3(), dad.getElement1(), dad.getElement2()));
                elementKeys.add(getElementKey(mom.getElement2(), mom.getElement3(), dad.getElement1(), dad.getElement3()));
                elementKeys.add(getElementKey(mom.getElement2(), mom.getElement3(), dad.getElement2(), dad.getElement3()));

                for( i = 0; iM < 3; i++){
                    elementKeys.add(getElementKey(mom.getElements().get(i),dad.getElement1(),dad.getElement2(),dad.getElement3()));
                    elementKeys.add(getElementKey(dad.getElements().get(i),mom.getElement1(),mom.getElement2(),mom.getElement3()));
                }

            }
        }




//        elements.add(new element("fire"));
//        elements.add(new element("wind"));
//        elements.add(new element("earth"));
//        elements.add(new element("water"));
//        elements.add(new element("plant"));
//        elements.add(new element("metal"));
//        elements.add(new element("energy"));
//        elements.add(new element("void"));
//        elements.add(new element("light"));
//        elements.add(new element("shadow"));
//        elements.add(new element("legendary"));
//        elements.add(new element("divine"));

        if(dad.getElements().size() == 1 || mom.getElements().size() == 1){
            elementKeys.remove(getElementKey("fire","plant"));
            elementKeys.remove(getElementKey("wind","energy"));
            elementKeys.remove(getElementKey("water","metal"));
            elementKeys.remove(getElementKey("earth","void"));
            elementKeys.remove(getElementKey("shadow","light"));
        }

        dragList = new ArrayList<>();
        for (String s : elementKeys){
            if(breedresultsByElementkey.containsKey(s)) {
                dragList.addAll(breedresultsByElementkey.get(s));
            }
        }
//        if( (mom.getId().equalsIgnoreCase(getDDW_mom()) && dad.getId().equalsIgnoreCase(getDDW_dad()))
//                || (mom.getId().equalsIgnoreCase(getDDW_dad()) && dad.getId().equalsIgnoreCase(getDDW_mom())) ){
//            dragList.add(dragons.get(getDDW()));
//        }
        if (checkMomDad(mom, dad, getDDW_mom(), getDDW_dad())) {
            dragList.add(dragons.get(getDDW()));
        }

        for (SpecialBreed b:specialBreeds) {
            if(withEchant && b.isEnchanted || !b.isEnchanted){
                if(b.checkMomDad(mom,dad)){
                    dragList.add(dragons.get(b.childId));
                }
            }
        }

//        if(withEchant) {
//
//            if (checkMomDad(mom, dad, "bee", "tree")) {
//                dragList.add(dragons.get("amber"));
//            } else if (checkMomDad(mom, dad, "clay", "toxic")) {
//                dragList.add(dragons.get("ant"));
//            } else if (checkMomDad(mom, dad, "orange", "venom")) {
//                dragList.add(dragons.get("beetle"));
//            } else if (checkMomDad(mom, dad, "cloud", "agave")) {
//                dragList.add(dragons.get("bloom"));
//            } else if (checkMomDad(mom, dad, "clownfish", "seed")) {
//                dragList.add(dragons.get("box"));
//            } else if (checkMomDad(mom, dad, "dark_mech", "cockatoo")) {
//                dragList.add(dragons.get("briar"));
//            } else if (checkMomDad(mom, dad, "mist", "golden_crow")) {
//                dragList.add(dragons.get("cockatoo"));
//            } else if (checkMomDad(mom, dad, "frosty", "fossil")) {
//                dragList.add(dragons.get("crumbly"));
//            } else if (checkMomDad(mom, dad, "armored", "alien")) {
//                dragList.add(dragons.get("dark_mech"));
//            } else if (checkMomDad(mom, dad, "magnet", "rocker")) {
//                dragList.add(dragons.get("disco_ball"));
//            } else if (checkMomDad(mom, dad, "lightfish", "brick")) {
//                dragList.add(dragons.get("fossil"));
//            } else if (checkMomDad(mom, dad, "frostbite", "yeti")) {
//                dragList.add(dragons.get("frosty"));
//            } else if (checkMomDad(mom, dad, "blueflame", "lava")) {
//                dragList.add(dragons.get("frosty"));
//            } else if (checkMomDad(mom, dad, "blueflame", "lava")) {
//                dragList.add(dragons.get("hellfire"));
//            } else if (checkMomDad(mom, dad, "witch", "elemental")) {
//                dragList.add(dragons.get("hypoestes"));
//            } else if (checkMomDad(mom, dad, "ice", "geiger")) {
//                dragList.add(dragons.get("origami"));
//            } else if (checkMomDad(mom, dad, "tribal", "sunflower")) {
//                dragList.add(dragons.get("owl"));
//            } else if (checkMomDad(mom, dad, "superhero", "disco_ball")) {
//                dragList.add(dragons.get("plushie"));
//            } else if (checkMomDad(mom, dad, "tiger", "jelly")) {
//                dragList.add(dragons.get("superhero"));
//            }
//
//        }
//
//        if (checkMomDad(mom, dad, "emperor", "magnet")) {
//            dragList.add(dragons.get("crystal"));
//        } else if (checkMomDad(mom, dad, "magnet", "sunflower")) {
//            dragList.add(dragons.get("dark_machine"));
//        } else if (checkMomDad(mom, dad, "lightning", "magnet")) {
//            dragList.add(dragons.get("narwhale"));
//        } else if (checkMomDad(mom, dad, "lightning", "sunflower")) {
//            dragList.add(dragons.get("pixie"));
//        } else if (checkMomDad(mom, dad, "mercury", "sunflower")) {
//            dragList.add(dragons.get("siren"));
//        } else if (checkMomDad(mom, dad, "magnet", "mercury")) {
//            dragList.add(dragons.get("titan"));
//        } else if (checkMomDad(mom, dad, "lightning", "mercury")) {
//            dragList.add(dragons.get("vortex"));
//        }

        if(!vipDragons) {
            for (i = dragList.size() - 1; i >= 0; i--) {
                if (dragList.get(i).isVIP()) {
                    dragList.remove(i);
                }
            }
        }
        retval =  CalcOdds(dragList);
        return retval;
    }


    private boolean isChild(Dragon mom, Dragon dad, Dragon child) {
        Boolean retval = false;

        Boolean dotmcontrol = false;
        Boolean mumLegendary = false;
        Boolean dadLegendary = false;

        if (mom.getId().equals(dad.getId())) {
            // gleiche drachen geht nicht
            return false;
        }

        if(child.isUnreleased() || mom.isUnreleased() || dad.isUnreleased()){
             return false;
        }
        if (child.isBoss() || dad.isBoss() || mom.isBoss()) {
            return false;
        }


//        if (!child.boss_vip.isEmpty()  && !child.boss_vip.equals("V")) { // nicht VIP, aber Boss oder Event oder unreleased
            // Drogon of the week
            String tmp = getDDW();

            if(child.getId().equalsIgnoreCase(tmp)){
                if(mom.getId().equalsIgnoreCase(getDDW_mom()) && dad.getId().equalsIgnoreCase(getDDW_dad())
                        || dad.getId().equalsIgnoreCase(getDDW_mom()) && mom.getId().equalsIgnoreCase(getDDW_dad()) ){
                    return true;
                }
            }
            //if (DateTime.Now() > DOW.begin && DateTime.Now() < DOW.end && this.id == DOW.id) {
            //  if(DOW.momid == mom.id && DOW.dadid == dad.id || DOW.momid == dad.id && DOW.dadid == mom.id ){
            //    return true;
            //  }
            //}


            // Dragon of the month
            if(child.getId().equalsIgnoreCase(getDDM())){
                if(mom.isBoss() || mom.isUnreleased() || mom.islegendary() || mom.isdivine()){
                    return false;
                }
                if(dad.isBoss() || dad.isUnreleased() || dad.islegendary() || dad.isdivine()){
                    return false;
                }
                List<String> MomDadElements = new ArrayList<>();
                MomDadElements.addAll(mom.getElements());
                MomDadElements.addAll(dad.getElements());
                List<String> ddmElements = getDdmElements();
                if(ddmElements.size() > 0) {
                  if(MomDadElements.containsAll(ddmElements)){
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



        if(!child.isBreadable()){
//            if(enchantDragons) {
                return isSpecialBreed(mom, dad, child);
//            }
//            return false;
        }

 //           return false;
 //       }

        /** SPECIAL BREED **/
        //breed with legendary

//        List<string> mumOrDadLegendaryListElements = new List<string>();

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
        if (dad.islegendary() || mom.islegendary() || dad.isdivine() || mom.isdivine() ) {
            return false; // Lassen wir mal weg....
//            if (!this.islegendary()) {
//            } else {
//                return false;
//            }
        } else {
            //sunflower, mercury, lightning, magnet, emperor

            if (child.getId().equalsIgnoreCase("sunflower") ||
                    child.getId().equalsIgnoreCase("mercury") ||
                    child.getId().equalsIgnoreCase("lightning") ||
                    child.getId().equalsIgnoreCase("magnet") ||
                    child.getId().equalsIgnoreCase("emperor")) {

                // Inkompatible Elemente ?
                if (mom.getElements().size() == 1 || dad.getElements().size() == 1) {
                    return false;
                }
            }
            //siren,pixie,dark machine,vortex,titan,narwhale
            if (child.getId().equalsIgnoreCase("siren") && ((mom.getId().equalsIgnoreCase("sunflower") && dad.getId().equalsIgnoreCase("mercury")) || (dad.getId().equalsIgnoreCase("sunflower") && mom.getId().equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (child.getId().equalsIgnoreCase("pixie") && ((mom.getId().equalsIgnoreCase("sunflower") && dad.getId().equalsIgnoreCase("lightning")) || (dad.getId().equalsIgnoreCase("sunflower") && mom.getId().equalsIgnoreCase("lightning")))) {
                return true;
            }
            if (child.getId().equalsIgnoreCase("dark_machine") && ((mom.getId().equalsIgnoreCase("sunflower") && dad.getId().equalsIgnoreCase("magnet")) || (dad.getId().equalsIgnoreCase("sunflower") && mom.getId().equalsIgnoreCase("magnet")))) {
                return true;
            }
            if (child.getId().equalsIgnoreCase("vortex") && ((mom.getId().equalsIgnoreCase("lightning") && dad.getId().equalsIgnoreCase("mercury")) || (dad.getId().equalsIgnoreCase("lightning") && mom.getId().equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (child.getId().equalsIgnoreCase("titan") && ((mom.getId().equalsIgnoreCase("magnet") && dad.getId().equalsIgnoreCase("mercury")) || (dad.getId().equalsIgnoreCase("magnet") && mom.getId().equalsIgnoreCase("mercury")))) {
                return true;
            }
            if (child.getId().equalsIgnoreCase("narwhale") && ((mom.getId().equalsIgnoreCase("magnet") && dad.getId().equalsIgnoreCase("lightning")) || (dad.getId().equalsIgnoreCase("magnet") && mom.getId().equalsIgnoreCase("lightning")))) {
                return true;
            }
            if (child.getId().equalsIgnoreCase("crystal") && ((mom.getId().equalsIgnoreCase("emperor") && dad.getId().equalsIgnoreCase("magnet")) || (dad.getId().equalsIgnoreCase("emperor") && mom.getId().equalsIgnoreCase("magnet")))) {
                return true;
            }
            if (child.getElements().size() == 1) {
                //1 elements => mum & dad must have element
                if (mom.getElements().contains(child.getElement1()) && dad.getElements().contains(child.getElement1())) {
                    return true;
                } else {
                    // JS ist hier schrott. Immer false
                    return false;
                }
            } else if (child.getElements().size() == 2) {
                //2 elements => mum & dad can have his element
                // Auch hier ist das JS kaputt:
                //if ((mumHasFirstElem && dadHasFirstElem && mumHasSecondElem && dadHasSecondElem) || (mumHasFirstElem && dadHasSecondElem) || (mumHasSecondElem && dadHasFirstElem)) {
                //  return true;
                //}
                // Das erste ist ein Sonderfall und wird duch zweiten und dritten Therm aufgefangen

                if ((mom.getElements().contains(child.getElement1()) && dad.getElements().contains(child.getElement2()))
                        || (mom.getElements().contains(child.getElement2()) && dad.getElements().contains(child.getElement1()))) {
                    return true;
                }


            } else if (child.getElements().size() == 3) {
                //3 elements => mum and dad must have all the elements
                if (child.islegendary() || child.isdivine()) {
                    return false;
                }
                Boolean e1 = false;
                Boolean e2 = false;
                Boolean e3 = false;
                Boolean m = false;
                Boolean d = false;
                if (mom.getElements().contains(child.getElement1())) {
                    m = true;
                    e1 = true;
                }
                if (mom.getElements().contains(child.getElement2())) {
                    m = true;
                    e2 = true;
                }
                if (mom.getElements().contains(child.getElement3())) {
                    m = true;
                    e3 = true;
                }
                if (dad.getElements().contains(child.getElement1())) {
                    d = true;
                    e1 = true;
                }
                if (dad.getElements().contains(child.getElement2())) {
                    d = true;
                    e2 = true;
                }
                if (dad.getElements().contains(child.getElement3())) {
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

    public boolean isBreedable(Dragon d){
        if(d.isBreadable()){
            return true;
        }
        if(d.getId().equalsIgnoreCase(getDDW())){
            return true;
        }
        if(d.getId().equalsIgnoreCase(getDDM())){
            return true;
        }
        for (SpecialBreed b: specialBreeds) {
            if(d.getId().equalsIgnoreCase(b.childId)){
                return true;
            }
        }
        return false;
    }

    private boolean isSpecialBreed(Dragon mom, Dragon dad, Dragon child) {
        for (SpecialBreed b: specialBreeds) {
            if(b.checkMomDadChild(mom,dad,child)){
                return true;
            }
        }
        return false;
    }

//    private boolean checkMomDadChild(Dragon mom, Dragon dad, Dragon child, String sMom, String sDad, String sChild) {
//        if(child.getId().equalsIgnoreCase(sChild)){
//            if(mom.getId().equalsIgnoreCase(sMom) && dad.getId().equalsIgnoreCase(sDad)
//                    || dad.getId().equalsIgnoreCase(sMom) && mom.getId().equalsIgnoreCase(sDad) ){
//                return true;
//            }
//        }
//        return false;
//    }
//
    private boolean checkMomDad(Dragon mom, Dragon dad, String sMom, String sDad) {

            if(mom.getId().equalsIgnoreCase(sMom) && dad.getId().equalsIgnoreCase(sDad)
                    || dad.getId().equalsIgnoreCase(sMom) && mom.getId().equalsIgnoreCase(sDad) ){
                return true;
            }

        return false;
    }

}
