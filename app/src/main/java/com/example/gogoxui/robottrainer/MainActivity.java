package com.example.gogoxui.robottrainer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
//import java.util.List;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button bt_listen;
    //private TextView tv_show;
    private RecyclerView rv_show;
    public EditText ed_regex;
    private TextToSpeech tts;
    private Button bt_refresh;
    private Button bt_savedb;
    private Button bt_delete;
    private Button bt_update;
    private Button bt_stt;
    public EditText ed_reaction;
    public EditText ed_name;
    //public EditText ed_language;
    public TextView ed_id;
    private WordlistAdapter adapter;
    private CheckWordlist cwl = new CheckWordlist();
    private ArrayList<String> mData ;
    public String sttFirstResult;
    //private List<Wordlist> wl_speedrate = DataSupport.select("name = ?","speedrate").find(Wordlist.class);
    //private String st_speedrate = wl_speedrate.get(0).getReactionWord();
    //private Wordlist wl_pitch = DataSupport.select("name = ?","pitch").findFirst(Wordlist.class);





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//////////////////////////////////////

        /////////創建DB框架/////////
        LitePal.getDatabase();
        /////////檢查初始DB/////////
        checkDB();
        //initDB();
        /////////載入TTS設定////////
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                final Locale tts_language = localeCallBack();
                String st_speedrate = DataSupport.find(Wordlist.class,1).getReactionWord();
                String st_pitch = DataSupport.find(Wordlist.class,2).getReactionWord();
                float speedrate = Float.parseFloat(st_speedrate);
                float pitch = Float.parseFloat(st_pitch);
                tts.setLanguage(tts_language);
                //if (isFloat(st_speedrate)|isFloat(st_pitch)){
                //    tts.setSpeechRate(Float.parseFloat(st_speedrate));
                //    tts.setPitch(Float.parseFloat(st_pitch));
                //} else {
                tts.setSpeechRate((float)speedrate);
                tts.setPitch((float)pitch);
            }});


        //Toast.makeText(this,st_speedrate,Toast.LENGTH_SHORT).show();
//////////////////////////////////////
        //view.getRootView();
        bt_listen = (Button) this.findViewById(R.id.listen);

        //tv_show = (TextView) this.findViewById(R.id.show_word);
        //tv_show.setMovementMethod(new ScrollingMovementMethod());
        rv_show = (RecyclerView) this.findViewById(R.id.rv_word);
        bt_refresh = (Button) this.findViewById(R.id.refresh);
        bt_savedb = (Button) this.findViewById(R.id.save_db);
        bt_update = (Button) this.findViewById(R.id.update_db);
        bt_delete = (Button) this.findViewById(R.id.delete);
        bt_stt = (Button) this.findViewById(R.id.voice_input);
        //ed_language = (EditText) this.findViewById(R.id.input_language);
        ed_regex = (EditText) this.findViewById(R.id.input_regex);
        ed_name = (EditText) this.findViewById(R.id.input_name);
        ed_id = (TextView) this.findViewById(R.id.ed_id);
        ed_reaction = (EditText) this.findViewById(R.id.input_reaction);

        //final float speedRate = Float.parseFloat(st_speedrate);
        //final float pitch = Float.parseFloat(wl_pitch.getReactionWord());
        //float speedRate = (float) 1.5;
        //float pitch = (float) 1.5;
                //final CheckWordlist cwl = new CheckWordlist();

        //for(int i = 0; i < 50; i++) {
        //    mData.add("項目"+i);
        //}

        showList();





        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tv_show.setText(cwl.checkList());
                Toast.makeText(view.getContext(),
                        "已刷新",Toast.LENGTH_SHORT).show();
                showList();
            }
        });

        bt_savedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ed1 = ed_name.getText().toString();
                String ed2 = ed_regex.getText().toString();
                String ed3 = ed_reaction.getText().toString();
                if (ed1.equals("")|ed2.equals("")|ed3.equals("")){
                    Toast.makeText(view.getContext(),"数据不完整",Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(view.getContext(),"数据是"+ed1,Toast.LENGTH_SHORT).show();
                    createWord();
                }


                //showList();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWord();
                //Toast.makeText(view.getContext(),
                //        "已更新1条数据",Toast.LENGTH_SHORT).show();

            }
        });


        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWord();
                //Toast.makeText(view.getContext(),
                //        "已删除1条数据",Toast.LENGTH_SHORT).show();

            }
        });

        bt_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listenNow(1);
                //String whatIheard = sttFirstResult;
                //checkRobot cr = new checkRobot();
                //String saywhat =  cr.getReturnword(mView,whatIheard);
                //sayNow(saywhat);
                //透過 Intent的方式開啟內建的語音辨識 Activity...
                //Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh_HK");
                //intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //語音辨識 Dialog 上要顯示的提示文字
                //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "讲野啦臭...");
                //startActivityForResult(intent,1);
            }
        });

        bt_stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                sayNow("你是說，"+sttFirstResult +"，嗎");

                */
                listenNow(2);

            }
        });

    }


    public void showList() {
        mData = new ArrayList<>();
        mData = cwl.dbToList();
        rv_show.setLayoutManager(new LinearLayoutManager(this
        ));
        rv_show.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new WordlistAdapter(mData,ed_id,ed_name,ed_regex,ed_reaction);
        rv_show.setAdapter(adapter);
    }

    public void deleteWord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title")
                .setMessage("是否要删除？")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    } })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String beforeId = ed_id.getText().toString();
                        Wordlist wl1 = new Wordlist();
                        wl1.setName("保留位置："+ beforeId);
                        wl1.setReactionWord("");
                        wl1.setTriggerWord("");
                        wl1.updateAll("id = ?",beforeId);
                        showList();
                    } })
                .create().show();
        ;
    }

    public void updateWord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title")
                .setMessage("是否要更新？")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    } })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String beforeId = ed_id.getText().toString();
                        Wordlist wl1 = new Wordlist();
                        wl1.setName(ed_name.getText().toString());
                        wl1.setReactionWord(ed_reaction.getText().toString());
                        wl1.setTriggerWord(ed_regex.getText().toString());
                        wl1.updateAll("id = ?",beforeId);
                        showList();
                    } })
                .create().show();
        ;
    }

    public void createWord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title")
                .setMessage("是否要创建？")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    } })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Wordlist wl2 = new Wordlist();
                        wl2.setName(ed_name.getText().toString());
                        wl2.setReactionWord(ed_reaction.getText().toString());
                        wl2.setTriggerWord(ed_regex.getText().toString());
                        wl2.save();
                        showList();
                    } })
                .create().show();
    }

    /*
    public boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }*/

    public void initDB(){
        Wordlist wl1 = new Wordlist();
        wl1.setName("tts_speedrate");
        wl1.setTriggerWord("@");
        wl1.setReactionWord("1.0");
        wl1.saveOrUpdate("id = ?","1");
        Wordlist wl2 = new Wordlist();
        wl2.setName("tts_pitch");
        wl2.setTriggerWord("@");
        wl2.setReactionWord("1.0");
        wl2.saveOrUpdate("id = ?","2");
        Wordlist wl3 = new Wordlist();
        wl3.setName("tts_language(1=yue,2=jp,3=us)");
        wl3.setTriggerWord("@");
        wl3.setReactionWord("1");
        wl3.saveOrUpdate("id = ?","3");
        Wordlist wl4 = new Wordlist();
        wl4.setName("noResult");
        wl4.setTriggerWord("@");
        wl4.setReactionWord("你噏咩呀");
        wl4.saveOrUpdate("id = ?","4");
    }

    private void checkDB(){
        try {
            String test_speedrate = DataSupport.find(Wordlist.class,1).getReactionWord();
            String test_pitch = DataSupport.find(Wordlist.class,2).getReactionWord();
            String language = DataSupport.find(Wordlist.class,3).getReactionWord();
            String noresult = DataSupport.find(Wordlist.class,4).getName();;
            float speedRate = Float.parseFloat(test_speedrate);
            float pitch = Float.parseFloat(test_pitch);
            int key = Integer.parseInt(language);
            //boolean testnoresult = "noResult";
            Toast.makeText(this,"已載入DB!",Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException e) {
            initDB();
            Toast.makeText(this,"已新建DB!",Toast.LENGTH_SHORT).show();
        }
    }


    private void listenNow(int request_num){
        //透過 Intent的方式開啟內建的語音辨識 Activity...

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh_HK");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "讲野啦臭..."); //語音辨識 Dialog 上要顯示的提示文字


        startActivityForResult(intent,request_num);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case 1:
                //////1:listen and react
                if (resultCode == RESULT_OK) {
                    //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //String all = "";
                    //for (String r : result) {
                    //   all = all + r + "\n";
                    //}
                    //String all = data.getStringExtra(RecognizerIntent.EXTRA_RESULTS);
                    //sttFirstResult = result.get(0);
                    //Toast.makeText(this,"我聽到："+sttFirstResult,Toast.LENGTH_SHORT).show();

                    //tv_show.setText(nowWord);
                    //String tryRegex = "";

                    checkRobot cr = new checkRobot();
                    String saywhat =  cr.getReturnword(this,result.get(0));
                    sayNow(saywhat);
                    break;
                }
            case 2:
                /////////2:listen word to EditText;
                if (resultCode == RESULT_OK) {
                    final ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sayNow("你確定要輸入,"+results.get(0)+",嗎");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Title").setMessage(results.get(0))
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                } })
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ed_reaction.setText(results.get(0));
                                } })
                            .create().show();


                    break;
                }
        }


    }


    public void sayNow(String w){
        tts.speak(w,TextToSpeech.QUEUE_FLUSH,null);
    }

    public Locale localeCallBack(){
        String st_language = DataSupport.find(Wordlist.class,3).getReactionWord();
        int key = Integer.parseInt(st_language);

        Locale locale = new Locale("yue","HK");
        switch (key){
            case 1:
                locale = new Locale("yue","HK");
                break;
            case 2:
                locale = new Locale("ja","JP");
                break;
            case 3:
                locale = Locale.US;
                break;
        }
        return locale;
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        tts.shutdown();
    }

}

