package com.shivprakash.newswave;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyViewHolder>{
    private Context context;
    String linkS;
    private View.OnClickListener onClickListener;
    private ArrayList title,imgUrl,link;
    CustomAdaptor(Context context,ArrayList title,ArrayList imgUrl ,ArrayList link){
        this.context=context;
        this.title=title;
        this.imgUrl=imgUrl;
        this.link=link;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.newsheadline,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdaptor.MyViewHolder holder, int position) {

        linkS=String.valueOf(link.get(position));

        holder.title.setText(String.valueOf(title.get(position)));
        if(String.valueOf(imgUrl.get(position))!="null"){
            Glide.with(context).load(String.valueOf(imgUrl.get(position))).apply(new RequestOptions().circleCrop()).into(holder.image);}
        else{
            holder.image.setImageResource(R.drawable.newspaper_sign_white_icon_in_red_circle_vector_13415475_removebg_preview);
        }

    }

    @Override
    public int getItemCount() {
        return title.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.newsTitle);
            image=itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
            //  iconButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Log.d("ClickFromViewHolder", "Clicked");
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context,NewsDetail.class);
            intent.putExtra("url",link.get(position).toString());
            context.startActivity(intent);

        }
    }
}
       /* search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                url="https://www.google.com/search?q="+s;
                Intent intent = new Intent(MainActivity.this,googleSearch.class);
                intent.putExtra("url",url);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/