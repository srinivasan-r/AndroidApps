package com.vs.myapps2.emi;

import com.vs.myapps2.support.Data4Strings;
import com.vs.myapps2.support.EMIData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 305022193 on 1/16/2017.
 */

public interface AsyncListener {
    public void onPostExecute(HashMap<Integer, ArrayList<EMIData>> emiDetailsMap);
}
