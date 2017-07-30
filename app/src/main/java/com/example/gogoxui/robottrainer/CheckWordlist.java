package com.example.gogoxui.robottrainer;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gogoxui on 17-7-28.
 */

public class CheckWordlist  {
    //private RecyclerView rv_show;


    /*
    public static String checkList(){
        List<Wordlist> wls = DataSupport.findAll(Wordlist.class);
        String listall = "";
        for (Wordlist wl:wls){
            listall =  listall + "ID: " + wl.getId() + ",Name: " + wl.getName() + ",Trigger: " + wl.getTriggerWord() +",Reaction: " + wl.getReactionWord() +"\n"  ;
        }
        return listall;
    };
    */

    public static ArrayList<String> dbToList(){
        List<Wordlist> wls = DataSupport.findAll(Wordlist.class);
        ArrayList<String> wordToList = new ArrayList<>();
        //int listsize = wls.size();
        String list = "";
        for (Wordlist wl:wls){
            list = list + "ID: " + wl.getId() + ",Name: " + wl.getName() +
                    "\n" + "Trigger: " + wl.getTriggerWord() +
                    "\n" + "Reaction: " + wl.getReactionWord() ;
            wordToList.add(list);
            list = "";
        }
        return wordToList;
    }

    public static String getReaction(){
        String reaction = new String();
        return reaction;
    }


}
