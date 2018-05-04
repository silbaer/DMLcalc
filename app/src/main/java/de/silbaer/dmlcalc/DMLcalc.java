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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * Durch Ableitung von Application gibt es immer eine Instanz
 *
 */
public class DMLcalc extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{

    /*********************************************
     * Halter für eine Spezialzucht => Nur eine Kombination.
     * Spezialzucht: Züchtbare legendäre Drachen
     *               Verzauberte Zucht
     *
     * TODO: Vorraussetzungen für verzauberte Zucht: Z-Level / Bruthölen-Level
     */
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

        /**
         * Prüft ob die Drachen (Mom, Dad, Child) der gegebenen Spezialzucht entsprechen
         * @param mom Drachen-ID
         * @param dad Drachen-ID
         * @param child Drachen-ID
         * @return
         */
        public boolean checkMomDadChild(Dragon mom, Dragon dad, Dragon child) {
            if(child.getId().equalsIgnoreCase(childId)){
                return checkMomDad(mom,dad);
            }
            return false;
        }

        /**
         * Prüft ob die Drachen (Mom, Dad) der gegebenen Spezialzucht entsprechen
         * @param mom Drachen-ID
         * @param dad Drachen-ID
         * @return
         */
        public boolean checkMomDad(Dragon mom, Dragon dad) {

            if(mom.getId().equalsIgnoreCase(momId) && dad.getId().equalsIgnoreCase(dadId)
                    || dad.getId().equalsIgnoreCase(momId) && mom.getId().equalsIgnoreCase(dadId) ){
                return true;
            }
            return false;
        }
    }

    /****************************************
     * Gibt eine Stringresource anhand ihres Identifiers und nicht der ResourceID zurück
     * @param aString Identifier der Resource
     * @return Resourcestring oder aString wenn Identifier nicht gefunden wurde
     */
    public String getStringResourceByName(String aString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        if(resId == 0) {
            return aString;
        }
        return getString(resId);
    }

    /********************************************************
     * Ermittelt die ResourceID  eines Drawables anhang des Identifier
     * @param aString Identifier
     * @return ResourceID ( 0 = nicht gefunden)
     */
    public int getDrawableIdentifierByName(String aString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(aString, "drawable", packageName);
        return resId;
    }

//    public static Application getApplication() {
//        return _instance;
//    }

    /****
     * Gibt den Application-Context zurück
     * @return
     */
    public static Context getContext() {
        return _instance.getApplicationContext();
    }

    /***********************************************
     * Gibt ein auf Größe und Breite skaliertes Drawable anhand einer ResourceID zurück
     * @param resourceID ID des Drawables
     * @param width Breite
     * @param height Höhe
     * @return
     */
    private Drawable getScaledDrawable(int resourceID, int width, int height){

        Resources resources = getContext().getResources();

        // Read your drawable from somewhere
        Drawable dr = resources.getDrawable(resourceID);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to width x height
        Drawable d = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true));
// Set your new, scaled drawable "d"
        return d;
    }

    /*****************************************************
     * Gibt ein auf Größe und Breite skaliertes Drawable anhand einer Asset-URL zurück
     * @param assetsUrl File-URL relativ zum Asset-Verzeichnis
     * @param width Breite
     * @param height Höhe
     * @return
     */
    private Drawable getScaledDrawable(String assetsUrl, int width, int height){
        Drawable drawable = null;
        InputStream inputStream = null;

        try {
            Resources resources = getContext().getResources();
            inputStream = getContext().getAssets().open(assetsUrl);
            Drawable dr = Drawable.createFromStream(inputStream, null);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to width x height
            drawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true));
// Set your new, scaled drawable "d"
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return drawable;
    }

   // private  Map<String,Drawable> dragonIconCache = new Hashtable<String,Drawable>();

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    /**********************************
     * Lädt anhand einer DragonID ein (Drachenlisten-) Icon in ein Image-View. Das Icon wird per
     * Picasso-Lib aus dem Applications-Verzeichnis geladen
     * @param id DrachenID
     * @param view ImageView
     */
    public void loadDragonIcon(String id, ImageView view){
        String filename = id + "_icon.png";
        File file = new File(getContext().getFilesDir(), filename);
        if(file.exists()) {
            Picasso.with(getContext()).load(file).into(view);
        } else {
            createDragonIcon(id);
            Picasso.with(getContext()).load(file).into(view);
        }
    }
    public void createDragonIcon(String id){
        Dragon dragon = dragons.get(id);
        createDragonIcon(dragon);
    }

    public void createDragonIcon( Dragon dragon){

        try {
            String id = dragon.getId();

            String assetname = "dragonicons/" + id + "_icon.png";

            Context myContext = getContext();
            int iconSize = convertSpToPixels(72,myContext);
            String resName = id + "_icon";
            Resources resources = myContext.getResources();
            List<Drawable> scaledLayers = new ArrayList<Drawable>();


            Drawable assetsIcon = getScaledDrawable(assetname,iconSize, iconSize);
            if(assetsIcon == null){
                assetsIcon = getScaledDrawable("dragonicons/" + "unknown" + "_icon.png",iconSize, iconSize);
            }

            scaledLayers.add(assetsIcon);


            String type = dragon.getType();
            if ("C".equalsIgnoreCase(type)) {
                scaledLayers.add(getScaledDrawable("classification/classification_corner_common.png", iconSize/2, iconSize/2));
            } else if ("D".equalsIgnoreCase(type)) {
                scaledLayers.add(getScaledDrawable("classification/classification_corner_divine.png", iconSize/2, iconSize/2));
            } else if ("E".equalsIgnoreCase(type)) {
                scaledLayers.add(getScaledDrawable("classification/classification_corner_epic.png", iconSize/2, iconSize/2));
            } else if ("L".equalsIgnoreCase(type)) {
                scaledLayers.add(getScaledDrawable("classification/classification_corner_legendary.png", iconSize/2, iconSize/2));
            } else if ("R".equalsIgnoreCase(type)) {
                scaledLayers.add(getScaledDrawable("classification/classification_corner_rare.png", iconSize/2, iconSize/2));
            } else if ("U".equalsIgnoreCase(type)) {
                scaledLayers.add(getScaledDrawable("classification/classification_corner_uncommon.png", iconSize/2, iconSize/2));
            } else {
                scaledLayers.add(getScaledDrawable("classification/classification_corner_empty.png", iconSize/2, iconSize/2));
            }

            if (dragon.isVIP()) {
                scaledLayers.add(getScaledDrawable("notes/vip_dragons_icon.png", iconSize/5*2, iconSize/5*2));
            } else if (dragon.isEnchatmentBreed()) {
                scaledLayers.add(getScaledDrawable("notes/enchanted_breeding_icon.png", iconSize/5*2, iconSize/5*2));
            } else if (dragon.isDailyLogin()) {
                scaledLayers.add(getScaledDrawable("notes/daily_login_reward_icon.jpg", iconSize/5*2, iconSize/5*2));
            } else if (!dragon.isBreadable()) {
                scaledLayers.add(getScaledDrawable("notes/ltd_icon.png", iconSize/5*2, iconSize/5*2));
            }

            LayerDrawable layerDrawable = new LayerDrawable(scaledLayers.toArray(new Drawable[0]));
            layerDrawable.setLayerInset(1, iconSize/2, 0, 0, iconSize/2);
            if (scaledLayers.size() > 2) {
                layerDrawable.setLayerInset(2, iconSize/5*3, iconSize/5*3, 0, 0);
            }

            scaledLayers.clear();

            String filename = id + "_icon.png";

            FileOutputStream outputStream = null;
            layerDrawable.setBounds(0,0,iconSize,iconSize);

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                Bitmap b = Bitmap.createBitmap(iconSize,iconSize, Bitmap.Config.ARGB_8888);
                layerDrawable.draw(new Canvas(b));
                b.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

             //   outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }

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

    Map<String,Double> odds;

    private Hashtable<String,Object> _breedCache;

    private Hashtable<String,ArrayList<Dragon>> dragonsByElementkey; // Alle Drachen
    private Hashtable<String,ArrayList<Dragon>> breedresultsByElementkey;  // Erbrütbare Drachen

    public Hashtable<String,Integer> exceptionalBreadingTimes; // Sonderzeiten beim Breading
    public Hashtable<String,Integer> exceptionalHatchingTimes; // Sonderzeiten beim Hatching

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


    }
    public ArrayList<Dragon> getDragons4BreedCombo() {

        return getDragons(true,true,true,false,false);



    }

    public ArrayList<Dragon> getDragons() {
        return getDragons(true,true,true,true,false);
    }

    // Erbrütbare Drachen aktualisieren
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


    public void clearCache(){
        _breedCache.clear();
        updateBreedResults();
    }
    public void saveCache(){



    }
    public void loadCache(){
        _breedCache = new Hashtable<>();
    }
    private Object getFromCache(String key) {
        Object retval = null;

        if(_breedCache.containsKey(key)){
            retval = _breedCache.get(key);
        }

      return retval;
    }

    private void saveInCache(String key, Object value){
        _breedCache.put(key,value);
    }

    public String getTimeString(Integer seconds){
        StringBuilder retval = new StringBuilder();
        Integer unitCount = 0;

        Integer bDay = seconds / (60 * 60 * 24);
        Integer bHour = (seconds - bDay * (60 * 60 * 24)) / (60 * 60);
        Integer bMin = (seconds - bDay * (60 * 60 * 24) - bHour * (60 * 60)) / (60);
        Integer bSec = seconds - bDay * (60 * 60 * 24) - bHour * (60 * 60) - bMin * 60;

        if (bDay > 0) {
            unitCount++;
            retval.append(bDay + "d, ");
        }
        if (bHour > 0) {
            unitCount++;
            retval.append(bHour + "h, ");
        }
        if (bMin > 0 && unitCount < 2) {
            unitCount++;
            retval.append(bMin + "m, ");
        }
        if (bSec > 0 && unitCount < 2) {
            unitCount++;
            retval.append(bSec + "s, ");
        }
        retval.setLength(retval.length() - 2);

        return retval.toString();
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

        loadCache();

        AssetManager assetManager = getAssets();

        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("newDragon.list"));
            BufferedReader reader = new BufferedReader(is);
            //reader.readLine();
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String dragonJS = sb.toString();

            dragonsByElementkey = new Hashtable<>();

            Dragon d;
            String[] lines = dragonJS.split("[\\n]+");
            for (String jsline : lines) {
                String l = jsline.trim();
                if(!l.isEmpty()){
                    d = new Dragon(l);
                    dragons.put(d.getId(),d);
                    if(!d.isUnreleased() && !d.isBoss()) {
                        String elementKey = d.getElementKey();
                        if (!dragonsByElementkey.containsKey(elementKey)) {
                            dragonsByElementkey.put(elementKey, new ArrayList<Dragon>());
                        }
                        dragonsByElementkey.get(elementKey).add(d);
                    }
                }
            }


            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        for (Dragon d: dragons.values()) {
                            createDragonIcon(d);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();

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
            specialBreeds.add(new SpecialBreed("fire","wind","leopard",true));
            specialBreeds.add(new SpecialBreed("geiger","ice","origami",true));
            specialBreeds.add(new SpecialBreed("sunflower","tribal","owl",true));
            specialBreeds.add(new SpecialBreed("disco_ball","superhero","plushie",true));
            specialBreeds.add(new SpecialBreed("plant","wind","sea_turtle",true));
            specialBreeds.add(new SpecialBreed("jelly","tiger","superhero",true));

            exceptionalBreadingTimes = new Hashtable<String,Integer>();
            exceptionalBreadingTimes.put("fire",30);
            exceptionalBreadingTimes.put("wind",30);
            exceptionalBreadingTimes.put("earth",60);
            exceptionalBreadingTimes.put("water",300);
            exceptionalBreadingTimes.put("plant",60*60);
            exceptionalBreadingTimes.put("metal",60*90);
            exceptionalBreadingTimes.put("energy",60*120);
            exceptionalBreadingTimes.put("void",60*180);
            exceptionalBreadingTimes.put("light",60*60*8);
            exceptionalBreadingTimes.put("shadow",60*60*8);

            exceptionalBreadingTimes.put("bee",60*45);
            exceptionalBreadingTimes.put("dust",60*60*2);
            exceptionalBreadingTimes.put("lava",60*60);
            exceptionalBreadingTimes.put("salamander",60*90);
            exceptionalBreadingTimes.put("smoke",60*30);

            exceptionalHatchingTimes = new Hashtable<String,Integer>();
            exceptionalHatchingTimes.put("fire",30);
            exceptionalHatchingTimes.put("wind",30);
            exceptionalHatchingTimes.put("earth",60);
            exceptionalHatchingTimes.put("water",300);
            exceptionalHatchingTimes.put("plant",60*60);
            exceptionalHatchingTimes.put("metal",60*90);
            exceptionalHatchingTimes.put("energy",60*120);
            exceptionalHatchingTimes.put("void",60*180);
            exceptionalHatchingTimes.put("light",60*(60*8 +50));
            exceptionalHatchingTimes.put("shadow",60*(60*8+50));

            exceptionalHatchingTimes.put("bee",60*80);
            exceptionalHatchingTimes.put("dust",60*(60*3+20));
            exceptionalHatchingTimes.put("fireball",30);
            exceptionalHatchingTimes.put("seahorse",60*10);
            exceptionalHatchingTimes.put("lava",60*90);
            exceptionalHatchingTimes.put("prairie",60*5);
            exceptionalHatchingTimes.put("runestone",30);
            exceptionalHatchingTimes.put("salamander",60*120);
            exceptionalHatchingTimes.put("smoke",60*45);
            exceptionalHatchingTimes.put("tick_tock",60*(60*8));
            exceptionalHatchingTimes.put("tribal",60*5);





        } catch (IOException e) {
            e.printStackTrace();
        }
        updateBreedResults();


    }

    public List<Pair<Dragon,Double>> CalcOdds( List<Dragon> resultList) {
        double sum = 0;
        List<Pair<Dragon,Double>> retval = new ArrayList<>();
        for (Dragon d : resultList) {
            if (!d.isUnreleased()
                    && !d.isBoss()
                    //    && !d.isEvent()
                    // && !d.isVIP()
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

    // Anhand des Ergebnisses (son) werden die Möglichen Eltern berechnet
    //
    private List<Pair<Pair<Dragon,Dragon>,Double>> _howToBreed2(Dragon son) {
        List<Pair<Pair<Dragon,Dragon>,Double>> retval = new ArrayList<>();

        List<String> L1 =new ArrayList<>();
        List<String> L2 =new ArrayList<>();
        List<String> L3 =new ArrayList<>();
        List<String> L12 =new ArrayList<>();
        List<String> L23 =new ArrayList<>();
        List<String> L13 =new ArrayList<>();
        // DDM
        List<String> L4 =new ArrayList<>();
        List<String> L123 =new ArrayList<>();
        List<String> L124 =new ArrayList<>();
        List<String> L134 =new ArrayList<>();
        List<String> L234 =new ArrayList<>();
        List<String> L14 =new ArrayList<>();
        List<String> L24 =new ArrayList<>();
        List<String> L34 =new ArrayList<>();



        int elementCount;
        List<String> breedElements;

        if(son.getId().equalsIgnoreCase(getDDM())){
            breedElements = getDdmElements();
        } else {
            breedElements = son.getElements();
        }
        elementCount = breedElements.size();



        if(son.getId().equalsIgnoreCase(getDDW())){
            List<Pair<Dragon, Double>> tmp = _breed(dragons.get(getDDW_mom()), dragons.get(getDDW_dad()), true);
            for (Pair<Dragon, Double> dp : tmp) {
                if (dp.first.getId().equalsIgnoreCase(son.getId())) {
                    retval.add(new Pair<Pair<Dragon, Dragon>, Double>(new Pair<Dragon, Dragon>(dragons.get(getDDW_mom()),  dragons.get(getDDW_dad())), dp.second));
                    break;
                }
            }

        } else {
            Dragon mom, dad;
            Hashtable<String, Pair<String, String>> breedCombos = new Hashtable<>();
            String comboKey, comboKey2;

            boolean isSpecialBreed = false;
            String sonID = son.getId();
            for (SpecialBreed b : specialBreeds) {
                if (b.childId.equalsIgnoreCase(sonID)) {
                    isSpecialBreed = true;
                    mom = dragons.get(b.momId);
                    dad = dragons.get(b.dadId);
                    List<Pair<Dragon, Double>> tmp = _breed(mom, dad, true);
                    for (Pair<Dragon, Double> dp : tmp) {
                        if (dp.first.getId().equalsIgnoreCase(son.getId())) {
                            retval.add(new Pair<Pair<Dragon, Dragon>, Double>(new Pair<Dragon, Dragon>(mom, dad), dp.second));
                            break;
                        }
                    }
                    break;
                }
            }
            if (!isSpecialBreed) {


                for (String eleKey : dragonsByElementkey.keySet()) {
                    if (eleKey.contains("legendary") || eleKey.contains("divine")) {
                        continue;
                    }
                    if (elementCount > 0) {
                        if (eleKey.contains(breedElements.get(0))) {
                            L1.add(eleKey);
                        }
                    }
                    if (elementCount > 1) {
                        if (eleKey.contains(breedElements.get(1))) {
                            L2.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(0)) && eleKey.contains(breedElements.get(1))) {
                            L12.add(eleKey);
                        }
                    }
                    if (elementCount > 2) {
                        if (eleKey.contains(breedElements.get(2))) {
                            L3.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(0)) && eleKey.contains(breedElements.get(2))) {
                            L13.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(1)) && eleKey.contains(breedElements.get(2))) {
                            L23.add(eleKey);
                        }
                    }
                    if (elementCount > 3) {
                        if (eleKey.contains(breedElements.get(3))) {
                            L4.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(0)) && eleKey.contains(breedElements.get(1)) && eleKey.contains(breedElements.get(2))) {
                            L123.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(1)) && eleKey.contains(breedElements.get(2)) && eleKey.contains(breedElements.get(3))) {
                            L234.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(0)) && eleKey.contains(breedElements.get(2)) && eleKey.contains(breedElements.get(3))) {
                            L134.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(0)) && eleKey.contains(breedElements.get(1)) && eleKey.contains(breedElements.get(3))) {
                            L124.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(0)) && eleKey.contains(breedElements.get(3))) {
                            L14.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(1)) && eleKey.contains(breedElements.get(3))) {
                            L24.add(eleKey);
                        }
                        if (eleKey.contains(breedElements.get(2)) && eleKey.contains(breedElements.get(3))) {
                            L34.add(eleKey);
                        }
                    }
                }

                if (elementCount == 1) {
                    for (int x = 0; x < L1.size(); x++) {
                        for (int y = x + 1; y < L1.size(); y++) {
                            comboKey = L1.get(x) + "/" + L1.get(y);
                            if (!breedCombos.containsKey(comboKey)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L1.get(x), L1.get(y)));
                            }
                        }
                    }
                }

                if (elementCount == 2) {
                    for (int x = 0; x < L1.size(); x++) {
                        for (int y = 0; y < L2.size(); y++) {
                            comboKey = L1.get(x) + "/" + L2.get(y);
                            comboKey2 = L2.get(y) + "/" + L1.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L1.get(x), L2.get(y)));
                            }
                        }
                    }
                }
                if (elementCount == 3) {
                    for (int x = 0; x < L1.size(); x++) {
                        for (int y = 0; y < L23.size(); y++) {
                            comboKey = L1.get(x) + "/" + L23.get(y);
                            comboKey2 = L23.get(y) + "/" + L1.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L1.get(x), L23.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L2.size(); x++) {
                        for (int y = 0; y < L13.size(); y++) {
                            comboKey = L2.get(x) + "/" + L13.get(y);
                            comboKey2 = L13.get(y) + "/" + L2.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L2.get(x), L13.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L3.size(); x++) {
                        for (int y = 0; y < L12.size(); y++) {
                            comboKey = L3.get(x) + "/" + L12.get(y);
                            comboKey2 = L12.get(y) + "/" + L3.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L3.get(x), L12.get(y)));
                            }
                        }
                    }
                }
                if (elementCount == 4) {
                    for (int x = 0; x < L1.size(); x++) {
                        for (int y = 0; y < L234.size(); y++) {
                            comboKey = L1.get(x) + "/" + L234.get(y);
                            comboKey2 = L234.get(y) + "/" + L1.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L1.get(x), L234.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L2.size(); x++) {
                        for (int y = 0; y < L134.size(); y++) {
                            comboKey = L2.get(x) + "/" + L134.get(y);
                            comboKey2 = L134.get(y) + "/" + L2.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L2.get(x), L134.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L3.size(); x++) {
                        for (int y = 0; y < L124.size(); y++) {
                            comboKey = L3.get(x) + "/" + L124.get(y);
                            comboKey2 = L124.get(y) + "/" + L3.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L3.get(x), L124.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L4.size(); x++) {
                        for (int y = 0; y < L123.size(); y++) {
                            comboKey = L4.get(x) + "/" + L123.get(y);
                            comboKey2 = L123.get(y) + "/" + L4.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L4.get(x), L123.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L12.size(); x++) {
                        for (int y = 0; y < L34.size(); y++) {
                            comboKey = L12.get(x) + "/" + L34.get(y);
                            comboKey2 = L34.get(y) + "/" + L12.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L12.get(x), L34.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L23.size(); x++) {
                        for (int y = 0; y < L14.size(); y++) {
                            comboKey = L23.get(x) + "/" + L14.get(y);
                            comboKey2 = L14.get(y) + "/" + L23.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L23.get(x), L14.get(y)));
                            }
                        }
                    }
                    for (int x = 0; x < L13.size(); x++) {
                        for (int y = 0; y < L24.size(); y++) {
                            comboKey = L13.get(x) + "/" + L24.get(y);
                            comboKey2 = L24.get(y) + "/" + L13.get(x);
                            if (!breedCombos.containsKey(comboKey) && !breedCombos.containsKey(comboKey2)) {
                                breedCombos.put(comboKey, new Pair<String, String>(L13.get(x), L24.get(y)));
                            }
                        }
                    }
                }

                for (Pair<String, String> breedpair : breedCombos.values()) {
                    mom = dragonsByElementkey.get(breedpair.first).get(0);
                    dad = dragonsByElementkey.get(breedpair.second).get(0);
                    List<Pair<Dragon, Double>> tmp = _breed(mom, dad, true);
                    for (Pair<Dragon, Double> dp : tmp) {
                        if (dp.first.getId().equalsIgnoreCase(son.getId())) {
                            for (int iMom = 0; iMom < dragonsByElementkey.get(breedpair.first).size(); iMom++) {
                                for (int iDad = 0; iDad < dragonsByElementkey.get(breedpair.second).size(); iDad++) {
                                    retval.add(new Pair<Pair<Dragon, Dragon>, Double>(new Pair<Dragon, Dragon>(dragonsByElementkey.get(breedpair.first).get(iMom), dragonsByElementkey.get(breedpair.second).get(iDad)), dp.second));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return retval;
    }

    private List<Pair<Pair<Dragon,Dragon>,Double>> _howToBreed(Dragon son) {
        List<Pair<Pair<Dragon,Dragon>,Double>> retval = null;

        retval = (List<Pair<Pair<Dragon,Dragon>,Double>>) getFromCache(son.getId());



        if(retval == null){
            retval =  _howToBreed2(son);
            saveInCache(son.getId(),retval);
        }
        return retval;
    }
    private boolean canBeDad(Dragon dad, Dragon son) {
       boolean retval = false;
        if(dad.getId().equalsIgnoreCase( getDDW_mom()) || dad.getId().equalsIgnoreCase(getDDW_dad()) ) {
            retval = true;
            return true;
        }
        if(getDDM().equalsIgnoreCase(son.getId())) {
            for (String ddmElement : getDdmElements()) {
                List<String> dadElements = dad.getElements();
                if (dadElements.contains(ddmElement)) {
                        retval = true;
                    return true; // Springt ans Ende der Funktion => Retval wird zurück gegeben und nicht "true"!
                }
            }
        } else {
            for (String e : son.getElements()) {
                if (dad.getElements().contains(e)) {
                     retval = true;
                    return true;
                }
            }
        }
        String sonID = son.getId();
        for (SpecialBreed b: specialBreeds) {
            if(b.childId.equalsIgnoreCase(sonID)){
                retval = true;
                return true;
            }
        }

        return retval;
       // return false;
    }

    private  List<Pair<Dragon,Double>> _breed(Dragon mom, Dragon dad) {
        return _breed(mom,dad,enchantDragons);
    }

    private  List<Pair<Dragon,Double>> _breed(Dragon mom, Dragon dad, boolean withEnchantment) {
        List<Pair<Dragon,Double>> retval;
        List<Pair<Dragon,Double>> retval2;
        List<Dragon> dragList;
        String key = mom.getId() + dad.getId();
        retval = (List<Pair<Dragon,Double>>) getFromCache(key);


        if (retval == null) {
            retval = _breed2( mom,  dad,withEnchantment);
            saveInCache(key, retval);
        }

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

        if(mom.isdivine() || mom.islegendary() || dad.isdivine() || dad.islegendary()){

//        if((mom.isle = legi|| devin ) || (dad = (legi || devine)) {
            if( ( mom.islegendary() || mom.isdivine()) && (dad.islegendary() || dad.isdivine())){
                for (element momEle : elements) {
                    for (element dadEle : elements) {
                        if(!momEle.isDevine() && !momEle.isLegendary() && !dadEle.isLegendary() && !dadEle.isDevine()){
                            elementKeys.add(getElementKey(momEle.id,dadEle.id));
                        }
                    }

                }
            } else if(mom.isdivine() || mom.islegendary()){
                for (element momEle : elements)  {
                    for (iD = 0; iD < dad.getElements().size(); iD++) {
                        if(!momEle.isDevine() && !momEle.isLegendary()) {
                            elementKeys.add(getElementKey(momEle.id, dad.getElements().get(iD)));
                        }
                    }
                }
                if (dad.getElements().size() == 2) {
                    for (element momEle : elements)  {
                        if(!momEle.isDevine() && !momEle.isLegendary()) {
                            elementKeys.add(getElementKey(momEle.id, dad.getElement1(), dad.getElement2()));
                        }
                    }
                }
                if (mom.getElements().size() == 3) {
                    for (element momEle : elements)  {
                        if(!momEle.isDevine() && !momEle.isLegendary()) {
                            elementKeys.add(getElementKey(momEle.id, dad.getElement1(), dad.getElement2(), dad.getElement3()));
                            elementKeys.add(getElementKey(momEle.id, dad.getElement1(), dad.getElement2()));
                            elementKeys.add(getElementKey(momEle.id, dad.getElement1(), dad.getElement3()));
                            elementKeys.add(getElementKey(momEle.id, dad.getElement2(), dad.getElement3()));

                        }
                    }
                }

            } else {
                for (element dadEle : elements)  {
                    for (iD = 0; iD < mom.getElements().size(); iD++) {
                        if(!dadEle.isDevine() && !dadEle.isLegendary()) {
                            elementKeys.add(getElementKey(dadEle.id, mom.getElements().get(iD)));
                        }
                    }
                }
                if (dad.getElements().size() == 2) {
                    for (element dadEle : elements)  {
                        if(!dadEle.isDevine() && !dadEle.isLegendary()) {
                            elementKeys.add(getElementKey(dadEle.id, mom.getElement1(), mom.getElement2()));
                        }
                    }
                }
                if (mom.getElements().size() == 3) {
                    for (element dadEle : elements)  {
                        if(!dadEle.isDevine() && !dadEle.isLegendary()) {
                            elementKeys.add(getElementKey(dadEle.id, mom.getElement1(), mom.getElement2(), mom.getElement3()));
                            elementKeys.add(getElementKey(dadEle.id, mom.getElement1(), mom.getElement2()));
                            elementKeys.add(getElementKey(dadEle.id, mom.getElement1(), mom.getElement3()));
                            elementKeys.add(getElementKey(dadEle.id, mom.getElement2(), mom.getElement3()));

                        }
                    }
                }

            }
//            if (mom = legi || mom = devine) {
//                if (dad = legi || dad = devine) {
//                    jeweils nur ein element aber alle
//                } else {
//                    ein element von mom und Elementkeys von dad
//                }
//            }
            // ddm berücksichtigen
        } else{

            for (iM = 0; iM < mom.getElements().size(); iM++) {
                for (iD = 0; iD < dad.getElements().size(); iD++) {
                    elementKeys.add(getElementKey(mom.getElements().get(iM), dad.getElements().get(iD)));
                }
            }
            if (mom.getElements().size() == 2) {
                for (iD = 0; iD < dad.getElements().size(); iD++) {
                    elementKeys.add(getElementKey(dad.getElements().get(iD), mom.getElement1(), mom.getElement2()));
                }
            }
            if (mom.getElements().size() == 3) {
                for (iD = 0; iD < dad.getElements().size(); iD++) {
                    elementKeys.add(getElementKey(dad.getElements().get(iD), mom.getElement1(), mom.getElement2()));
                    elementKeys.add(getElementKey(dad.getElements().get(iD), mom.getElement1(), mom.getElement3()));
                    elementKeys.add(getElementKey(dad.getElements().get(iD), mom.getElement2(), mom.getElement3()));
                }
            }
            if (dad.getElements().size() == 2) {
                for (iM = 0; iM < mom.getElements().size(); iM++) {
                    elementKeys.add(getElementKey(mom.getElements().get(iM), dad.getElement1(), dad.getElement2()));
                }
            }
            if (dad.getElements().size() == 3) {
                for (iM = 0; iM < mom.getElements().size(); iM++) {
                    elementKeys.add(getElementKey(mom.getElements().get(iM), dad.getElement1(), dad.getElement2()));
                    elementKeys.add(getElementKey(mom.getElements().get(iM), dad.getElement1(), dad.getElement3()));
                    elementKeys.add(getElementKey(mom.getElements().get(iM), dad.getElement2(), dad.getElement3()));
                }
            }

            //DDM
            if (mom.getElements().size() == 1 && dad.getElements().size() == 3) {
                elementKeys.add(getElementKey(mom.getElement1(), dad.getElement1(), dad.getElement2(), dad.getElement3()));
            }
            if (dad.getElements().size() == 1 && mom.getElements().size() == 3) {
                elementKeys.add(getElementKey(dad.getElement1(), mom.getElement1(), mom.getElement2(), mom.getElement3()));
            }
            if (mom.getElements().size() == 2) {
                if (dad.getElements().size() == 2) {
                    elementKeys.add(getElementKey(mom.getElement1(), dad.getElement1(), dad.getElement2(), mom.getElement2()));
                } else if (dad.getElements().size() == 3) {
                    elementKeys.add(getElementKey(mom.getElement1(), dad.getElement1(), dad.getElement2(), dad.getElement3()));
                    elementKeys.add(getElementKey(mom.getElement2(), dad.getElement1(), dad.getElement2(), dad.getElement3()));
                }
            }
            if (mom.getElements().size() == 3) {
                if (dad.getElements().size() == 2) {
                    elementKeys.add(getElementKey(dad.getElement1(), mom.getElement1(), mom.getElement2(), mom.getElement3()));
                    elementKeys.add(getElementKey(dad.getElement2(), mom.getElement1(), mom.getElement2(), mom.getElement3()));
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

                    for (i = 0; iM < 3; i++) {
                        elementKeys.add(getElementKey(mom.getElements().get(i), dad.getElement1(), dad.getElement2(), dad.getElement3()));
                        elementKeys.add(getElementKey(dad.getElements().get(i), mom.getElement1(), mom.getElement2(), mom.getElement3()));
                    }

                }
            }
        }
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

    private boolean checkMomDad(Dragon mom, Dragon dad, String sMom, String sDad) {

            if(mom.getId().equalsIgnoreCase(sMom) && dad.getId().equalsIgnoreCase(sDad)
                    || dad.getId().equalsIgnoreCase(sMom) && mom.getId().equalsIgnoreCase(sDad) ){
                return true;
            }

        return false;
    }

}
