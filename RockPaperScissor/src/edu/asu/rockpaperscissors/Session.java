package edu.asu.rockpaperscissors;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("username", usename).commit();        
    }

    public String getusename() {
        String usename = prefs.getString("username","");
        return usename;
    }
    
    public void setResult(Boolean result){
    	if(result==null)
    		prefs.edit().putInt("result", 0).commit();
    	else if(result == true)
    		prefs.edit().putInt("result", 1).commit();
    	else if(result == false)
    		prefs.edit().putInt("result", -1).commit();    		
    }
    
    public Boolean getResult(){
    	if(prefs.getInt("result", 0)==1)
    		return true;
    	else if(prefs.getInt("result", 0)==-1)
    		return false;
    	else if(prefs.getInt("result", 0)==0)
    		return null;
    	return null;
    }
    
    public void setCompChoice(String str){
    	prefs.edit().putString("comp",str).commit();
    }
    
    public String getCompChoice(){
    	return prefs.getString("comp", null);
    }
    
    public void setUserChoice(String str){
    	prefs.edit().putString("userc",str).commit();
    }
    
    public String getUserChoice(){
    	return prefs.getString("userc", null);
    }
    
    
    
}