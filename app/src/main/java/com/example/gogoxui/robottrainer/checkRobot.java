package com.example.gogoxui.robottrainer;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gogoxui on 17-7-26.
 */

public class checkRobot {
    private String inputword;
    //private String trigger;
    private String returnword ;
    private List<Wordlist> wls = DataSupport.findAll(Wordlist.class);
    //private int size;

    public String getReturnword(Context context, String iw){
        returnword = "我都唔知你噏乜";
        inputword = iw;
        //size = wls.size();
        //boolean goon = true;
        Toast.makeText(context,"Input is:"+iw,Toast.LENGTH_SHORT).show();

        //Toast.makeText(,"Regex is:"+tr,Toast.LENGTH_SHORT).show();

        for (Wordlist wl:wls){
            Pattern pattern = Pattern.compile(wl.getTriggerWord());
            Matcher matcher = pattern.matcher(iw);
            if (matcher.matches()){
                returnword = wl.getReactionWord();
                Toast.makeText(context,"Trigger is:"+wl.getTriggerWord(),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"Reactioni is:"+wl.getReactionWord(),Toast.LENGTH_SHORT).show();
                break;
            }
        }


        return returnword;
    }

}
