package com.example.wifidirect.autoConnect;

import android.util.Log;

public class HeaderParsing {
    private String string = "";
    public int value = -1;
    private static final String TAG = "HeaderParsing";
    public HeaderParsing(String string) {
        this.string = string;
        String[] sarray = string.split("\r\n");
        String firstLine = sarray[0];
        String[] firstLineArray = firstLine.split(" ");
        int i = firstLineArray[1].indexOf('?');
        //String strValue = firstLineArray[1];
        Log.d(TAG, "HeaderParsing: i= " + i);
        if(firstLineArray[1] != null){
            try {
                value = Integer.valueOf(firstLineArray[1].substring(i+1));
                Log.d(TAG, "HeaderParsing: " + value );
            }
            catch (NumberFormatException e){

                value=0;
                e.printStackTrace();
            }
            catch (StringIndexOutOfBoundsException e){
                value=0;
                e.printStackTrace();
            }
        }
    }

}
