package com.example.mislugares.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mislugares.fragment.PreferenciasFragment;

public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}