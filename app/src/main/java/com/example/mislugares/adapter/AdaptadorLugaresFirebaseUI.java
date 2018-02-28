package com.example.mislugares.adapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mislugares.R;
import com.example.mislugares.model.Lugar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Created by Rafael S Martin on 28/02/2018.
 */

public class AdaptadorLugaresFirebaseUI extends
        FirebaseRecyclerAdapter<Lugar, AdaptadorLugares.ViewHolder>
        implements AdaptadorLugaresInterface
{

    protected View.OnClickListener onClickListener;

    public AdaptadorLugaresFirebaseUI(@NonNull FirebaseRecyclerOptions<Lugar> opciones){
        super(opciones);

        Log.d("TIPO", "Firebase UI");

    }

    @Override
    protected void onBindViewHolder(@NonNull AdaptadorLugares.ViewHolder holder, int position, @NonNull Lugar lugar) {
        AdaptadorLugares.personalizaVista(holder, lugar);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public AdaptadorLugares.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_lista, parent, false);
        return new AdaptadorLugares.ViewHolder(view);
    }

    public void setOnItemClickListener(View.OnClickListener onClick) {
        onClickListener = onClick;
    }

    public String getKey(int pos) {
        return super.getSnapshots().getSnapshot(pos).getKey();
    }
}
