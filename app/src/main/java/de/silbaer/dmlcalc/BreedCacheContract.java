package de.silbaer.dmlcalc;

import android.provider.BaseColumns;

/**
 * Created by silbaer on 11.02.17.
 */

public final class BreedCacheContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private BreedCacheContract() {}

    /* Inner class that defines the table contents */
    public static class BreedCacheEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_KEY = "breedCacheKey";
        public static final String COLUMN_NAME_VALUE = "cacheJson";
    }


}
