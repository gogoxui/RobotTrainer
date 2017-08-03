package com.example.gogoxui.robottrainer;

import android.content.Context;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
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

    //private String unknowWord;
    private List<Wordlist> wls = DataSupport.findAll(Wordlist.class);
    //private int size;

    public String getReturnword(Context context, String iw){
        returnword = "乜料呀";
        inputword = iw;
        ArrayList<String> resultList = new ArrayList<String>();
        //size = wls.size();
        //boolean goon = true;
        //Toast.makeText(context,"Input is:"+iw,Toast.LENGTH_SHORT).show();

        //Toast.makeText(,"Regex is:"+tr,Toast.LENGTH_SHORT).show();

        for (Wordlist wl:wls){
            Pattern pattern = Pattern.compile(wl.getTriggerWord());
            Matcher matcher = pattern.matcher(iw);
            //Matcher haveName = Pattern.compile("\\?\\<.*\\>").matcher(wl.getTriggerWord());
            if (matcher.matches()){
                //if (!haveName.find()){
                    //returnword = wl.getReactionWord();
                    //returnword = matcher.replaceAll(wl.getReactionWord());

                String newword = matcher.replaceAll(wl.getReactionWord());
                Toast.makeText(context,"Trigger is:"+wl.getTriggerWord()+"\nReactioni is:"+newword,Toast.LENGTH_LONG).show();
                //returnword = newword;
                Toast.makeText(context,"Reactioni is:"+wl.getReactionWord(),Toast.LENGTH_SHORT).show();
                  //break;
                //}else {
                    //String word1 = haveName.group("name1");

                    //Toast.makeText(context,"I said"+returnword,Toast.LENGTH_SHORT).show();
                    //
                resultList.add(newword);
                //}
            }}


        int listSize = resultList.size();
        int wordOfList = (int) (Math.random()*listSize);
        //Toast.makeText(context,"第"+wordOfList+"個結果",Toast.LENGTH_LONG).show();
        //(int) Math.random()*listSize +1

        if (listSize>=1){
            //Toast.makeText(context,"有到"+listSize+"個結果",Toast.LENGTH_LONG).show();
            returnword = resultList.get(wordOfList);
            //Toast.makeText(context,"共有"+(listSize-1)+"個結果，顯示第"+wordOfList+"個",Toast.LENGTH_LONG).show();
            //returnword = resultList.get((int) Math.random()*listSize +1 );
        }else{
            Toast.makeText(context,"無找到結果",Toast.LENGTH_LONG).show();
            returnword = dontKnowWhat();
        }

        return returnword;
    }

    private String dontKnowWhat(){
        String unknowWord;
        List<Wordlist> wl_unknowWord = DataSupport.where("name = ?","noResult").find(Wordlist.class);
        int nwListSize = wl_unknowWord.size();
        int numOfList = (int) (Math.random()*nwListSize);
        unknowWord = wl_unknowWord.get(numOfList).getReactionWord();
        return unknowWord;
    }

}
