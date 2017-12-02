package com.b07.database.helper.android;

import android.content.Context;

import com.b07.database.DatabaseDriverAndroid;

/**
 * Created by Tayyab on 2017-12-01.
 */

public class DatabaseAndroidUpdateHelper extends DatabaseDriverAndroid {
    public DatabaseAndroidUpdateHelper(Context context) {
        super(context);
    }
    public boolean updateInventoryQuantity(int quantity, int itemId){
        // update the quantity and return if its succesfull
        return super.updateInventoryQuantity(quantity, itemId);
    }


}
