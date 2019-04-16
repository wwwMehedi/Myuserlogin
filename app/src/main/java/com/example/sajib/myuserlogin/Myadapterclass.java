package com.example.sajib.myuserlogin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Sajib on 31-Mar-18.
 */

public class Myadapterclass extends RecyclerView.Adapter<Myadapterclass.Myholder> {
    Context  context;
    ArrayList<Model> mydatamodels;
    Myadapterclass(Context context,ArrayList<Model> mydatamodels){
       this.context=context;
       this.mydatamodels=mydatamodels;

    }
    @Override
    public Myadapterclass.Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slotlist,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(Myadapterclass.Myholder holder, int position) {

       if(holder instanceof Myadapterclass.Myholder){
           Myadapterclass.Myholder myholdervar=(Myadapterclass.Myholder) holder;
       }
       Model model=mydatamodels.get(position);
       holder.textView.setText(model.getTitle());
       holder.textViewtwo.setText(model.getAddress());
       holder.textViewthree.setText(model.getPhone());
       holder.textViewfour.setText(model.getSlot());

    }

    @Override
    public int getItemCount() {
        return mydatamodels.size();
    }
    public class Myholder extends RecyclerView.ViewHolder{
        //ImageView imageView;
        TextView textView;
        TextView textViewtwo;
        TextView textViewthree;
        TextView textViewfour;

        public Myholder(View itemView) {
            super(itemView);
            //imageView=itemView.findViewById(R.id.imageView);
            textView=itemView.findViewById(R.id.textitleshowid);
            textViewtwo=itemView.findViewById(R.id.textaddresshowid);
            textViewthree=itemView.findViewById(R.id.textphonid);
            textViewfour=itemView.findViewById(R.id.textSlotid);

        }
    }
}
