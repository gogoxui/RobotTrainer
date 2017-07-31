package com.example.gogoxui.robottrainer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by gogoxui on 17-7-30.
 */

public class WordlistAdapter
        extends RecyclerView.Adapter<WordlistAdapter.ViewHolder> {
    private List<String> mData;
    private EditText mName;
    private EditText mRegex;
    private EditText mReacion;
    private TextView mId;
    private int dataSize;


    //private View mView;

    List<Wordlist> wls = DataSupport.findAll(Wordlist.class);


    //EditText ed_regex = (EditText) mView.findViewById(R.id.input_regex);
    //EditText ed_name = (EditText) mView.findViewById(R.id.input_name);
    //EditText ed_reaction = (EditText) mView.findViewById(R.id.input_reaction);

    WordlistAdapter(List<String> data,TextView id,EditText name,EditText regex,EditText reaction){
        mId = id;
        mName = name;
        mRegex = regex;
        mReacion = reaction;
        mData = data;
        dataSize = mData.size()-1;

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        private Button bt;
        ViewHolder(View itemView){
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.txtItem);
            bt = (Button) itemView.findViewById(R.id.itemLoad);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        holder.tv.setText(mData.get(dataSize-position));
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegex.setText(wls.get(dataSize-position).getTriggerWord());
                mReacion.setText(wls.get(dataSize-position).getReactionWord());
                mName.setText(wls.get(dataSize-position).getName());
                mId.setText(""+ wls.get(dataSize-position).getId());
                //mName.setText(""+dataSize);
                //mId.setText(""+position);
                Toast.makeText(view.getContext(),
                        "已回调数据",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }
}
