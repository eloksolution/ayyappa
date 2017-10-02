package ayyappa.eloksolutions.in.ayyappaap.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.Random;

import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.UserDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;

/**
 * Created by welcome on 8/19/2017.
 */

public class Util{

    public static void setPreferances(Context mcontext, RegisterDTO registerDto){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(Config.APP_PREFERENCES, mcontext.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("firstName", registerDto.getFirstName());
        edit.putString("lastName", registerDto.getLastName());
        edit.putString("loc", registerDto.getArea());
        edit.putString("lat", registerDto.getLati()+"");
        edit.putString("long",registerDto.getLongi()+"");
        edit.putString("userId", registerDto.getUserId());
        edit.commit();

        Config.setFirstName(registerDto.getFirstName());
        Config.setLastName(registerDto.getLastName());
        Config.setUserId(registerDto.getUserId());

        System.out.println("out opf the shared prefrence is" +registerDto.getUserId()+ registerDto.getLastName()+registerDto.getFirstName());
    }

    public static UserDTO getPreferances(Context mcontext) {
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(Config.APP_PREFERENCES, mcontext.MODE_PRIVATE);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(sharedPreferences.getString("firstName", ""));
        userDTO.setLastName(sharedPreferences.getString("lastName", ""));
        userDTO.setUserId(sharedPreferences.getString("userId", ""));
        userDTO.setLongi(sharedPreferences.getString("long", ""));
        userDTO.setLati(sharedPreferences.getString("lat", ""));
        userDTO.setArea(sharedPreferences.getString("loc", ""));
        return userDTO;

    }

    public  static int getRandomNumbers(){
        int min=100;
        int max=1000;
        return (new Random().nextInt(max-min+1))+min;
    }

    public static boolean isEmpty(String str){
        return str==null||str.trim().length()==0;
    }
    public static boolean isFull(File file){
        return file==null;

    }
}
