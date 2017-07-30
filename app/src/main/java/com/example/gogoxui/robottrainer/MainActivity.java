package com.example.gogoxui.robottrainer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public EditText ed_reaction;
    public EditText ed_name;
    public TextView ed_id;
    private WordlistAdapter adapter;
    private CheckWordlist cwl = new CheckWordlist();
    private ArrayList<String> mData ;
    //private View view;


    public void sayNow(String w){
        tts.speak(w,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.getDatabase();
        //view.getRootView();
        bt_listen = (Button) this.findViewById(R.id.listen);

        //tv_show = (TextView) this.findViewById(R.id.show_word);
        //tv_show.setMovementMethod(new ScrollingMovementMethod());
        rv_show = (RecyclerView) this.findViewById(R.id.rv_word);
        bt_refresh = (Button) this.findViewById(R.id.refresh);
        bt_savedb = (Button) this.findViewById(R.id.save_db);
        bt_update = (Button) this.findViewById(R.id.update_db);
        bt_delete = (Button) this.findViewById(R.id.delete);
        ed_regex = (EditText) this.findViewById(R.id.input_regex);
        ed_name = (EditText) this.findViewById(R.id.input_name);
        ed_id = (TextView) this.findViewById(R.id.ed_id);
        ed_reaction = (EditText) this.findViewById(R.id.input_reaction);
        //final CheckWordlist cwl = new CheckWordlist();

        //for(int i = 0; i < 50; i++) {
        //    mData.add("項目"+i);
        //}

        showList();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(new Locale("yue","HK"));
                tts.setSpeechRate((float)1.5);
            }
        });


        bt_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //透過 Intent的方式開啟內建的語音辨識 Activity...

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh_HK");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "讲野啦臭..."); //語音辨識 Dialog 上要顯示的提示文字


                startActivityForResult(intent,1);
            }
        });

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
                if (ed1.equals("")|ed2.equals("")|ed2.equals("")){
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                //String all = "";
                //for (String r : result) {
                //   all = all + r + "\n";
                //}
                //String all = data.getStringExtra(RecognizerIntent.EXTRA_RESULTS);
                String nowWord = result.get(0);

                //tv_show.setText(nowWord);
                //String tryRegex = "";

                checkRobot cr = new checkRobot();
                String saywhat =  cr.getReturnword(this,nowWord);
                sayNow(saywhat);
        }
        }
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
                        DataSupport.deleteAll(Wordlist.class, "id = ?", beforeId);
                        //tv_show.setText(cwl.checkList());
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
        ;
    }

}