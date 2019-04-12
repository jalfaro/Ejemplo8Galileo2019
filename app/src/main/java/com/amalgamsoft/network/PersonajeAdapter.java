package com.amalgamsoft.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amalgamsoft.network.data.Personaje;

import java.util.List;

public class PersonajeAdapter extends ArrayAdapter<Personaje> {
    private Context context;
    private int layout;
    private List<Personaje> lista;

    public PersonajeAdapter(@NonNull Context context, @NonNull List<Personaje> objects) {
        super(context, R.layout.item_personaje_layout, objects);
        this.context = context;
        this.lista = objects;
        this.layout = R.layout.item_personaje_layout;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {
      View v = convertView;
      if (v == null) {
          LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          v = li.inflate(layout, null);
      }

      if (lista.get(position) != null) {
          TextView nombre, anno;
          nombre = v.findViewById(R.id.personaje_nombre);
          anno = v.findViewById(R.id.personaje_anno);
          nombre.setText(lista.get(position).getNombre());
          anno.setText(lista.get(position).getAnno());
      }
      return v;
    }
}
