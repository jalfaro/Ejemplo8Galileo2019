package com.amalgamsoft.network;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.amalgamsoft.network.data.Personaje;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lista;
    private RequestQueue queue;
    private StringRequest request;
    private ProgressDialog dialog;
    private List<Personaje> listado;
    private Button next, back;
    private String nextUrl, backUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextUrl = "";
        backUrl = "";
        setContentView(R.layout.activity_main);
        lista = findViewById(R.id.lista_personaje);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        boolean variable = isAfterToaday("2019-04-03");
        Log.d("DEBUG", "el valor de la funcion es " + variable);
        queue = Volley.newRequestQueue(this);
        pidePersonaje("https://swapi.co/api/people/");

    }

    public void pidePersonaje(String url) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);
        dialog.show();
        request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Personaje personaje;
                        JSONObject json;
                        listado = new ArrayList<Personaje>();
                        try {
                            json = new JSONObject(response);
                            nextUrl = json.getString("next");
                            backUrl = json.getString("previous");
                            JSONArray arreglo = json.getJSONArray("results");
                            for (int i = 0; i < arreglo.length(); i ++) {
                                personaje = new Personaje();
                                personaje.setNombre(arreglo.getJSONObject(i).getString("name"));
                                personaje.setAnno(arreglo.getJSONObject(i).getString("birth_year"));
                                personaje.setSexo(arreglo.getJSONObject(i).getString("gender"));
                                listado.add(personaje);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        lista.setAdapter(new PersonajeAdapter(MainActivity.this, listado));
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.getMessage());
                        dialog.dismiss();
                    }
                });
        queue.add(request);
    }
    public boolean isAfterToaday(String fecha) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar fechaEvaluear = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        try {
            fechaEvaluear.setTime(df.parse(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (today.get(Calendar.YEAR) < fechaEvaluear.get(Calendar.YEAR)) {
            return true;
        } else {
            if (today.get(Calendar.YEAR) > fechaEvaluear.get(Calendar.YEAR)) {
                return false;
            } else {
                if (today.get(Calendar.MONTH) < fechaEvaluear.get(Calendar.MONTH)) {
                    return true;
                } else {
                    if (today.get(Calendar.MONTH)> fechaEvaluear.get(Calendar.MONTH)) {
                        return false;
                    } else {
                        if (today.get(Calendar.DAY_OF_YEAR) < fechaEvaluear.get(Calendar.DAY_OF_YEAR)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            if ((backUrl != "" && backUrl != null)) {
                pidePersonaje(backUrl);
            }
        } else {
            if ((nextUrl != "" && nextUrl != null)) {
                pidePersonaje(nextUrl);
            }
        }

    }
}
