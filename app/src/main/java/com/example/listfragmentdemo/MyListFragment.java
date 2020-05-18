package com.example.listfragmentdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private RequestQueue myqueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, true);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.pays_list, android.R.layout.simple_list_item_1);

        setListAdapter(arrayAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        myqueue = Volley.newRequestQueue(getActivity());
        final HashMap paysnombre = new HashMap<Integer, String>();
        paysnombre.put(0,"France");
        paysnombre.put(1,"Italy");
        paysnombre.put(2,"China");
        paysnombre.put(3,"Canada");
        paysnombre.put(4,"Morocco");
        paysnombre.put(5,"US");
        paysnombre.put(6,"Germany");

      // Toast.makeText(getActivity(), "Nombre de cas " + "chelou", Toast.LENGTH_SHORT).show();
        String url ="https://pomber.github.io/covid19/timeseries.json";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,  null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            String pays = (String) paysnombre.get(position);
                            JSONArray jsonArray = response.getJSONArray(pays);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            final String formatted = df.format(new Date(System.currentTimeMillis()-48*60*60*1000));
                            String datefinale = formatted.substring(0,5) + formatted.substring(6);

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject tabu = jsonArray.getJSONObject(i);
                                String datee = tabu.getString("date");
                                if(datee.equals(datefinale)){
                                    JSONObject nbcas = jsonArray.getJSONObject(i);
                                    String vrainbcas = nbcas.getString("confirmed");
                                    Toast.makeText(getActivity(), "Nombre de cas " + vrainbcas, Toast.LENGTH_SHORT).show();
                                } else {

                                }
                            }


                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Nombre de cas " + "chelou catch", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
error.printStackTrace();
            }
        });
        //String[] payslist = getActivity().getResources().getStringArray(R.array.pays_list);
       // String[]casparpays = getActivity().getResources().getStringArray(R.array.cas_list);
        myqueue.add(request);
        //Toast.makeText(getActivity(), "Nombre de cas " + casparpays[position], Toast.LENGTH_SHORT).show();
    }
}
