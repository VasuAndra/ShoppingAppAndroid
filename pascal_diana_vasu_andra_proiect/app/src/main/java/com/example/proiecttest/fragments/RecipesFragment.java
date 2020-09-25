package com.example.proiecttest.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.proiecttest.R;
import com.example.proiecttest.RecipesAdapter;
import com.example.proiecttest.RequestSingleton;
import com.example.proiecttest.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecipesFragment extends Fragment {
    private RecipesAdapter adapter;
    private RecyclerView recyclerView;
    private List<Recipe> recipes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recipesFragment = inflater.inflate(R.layout.recipes_fragment, container, false);

        recyclerView = recipesFragment.findViewById(R.id.recipes_recycler_view);
        adapter = new RecipesAdapter(recipes, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getRecipes();
        return recipesFragment;
    }

    private void getRecipes() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = "https://my-json-server.typicode.com/VasuAndra/AndroidProjectJSON/recipes";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int x = response.length();
                for (int index = 0; index < response.length(); index++) {
                    try {
                        JSONObject recipeJSON = response.getJSONObject(index);
                        recipes.add(new Recipe().fromJSON(recipeJSON));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "VolleyError: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestSingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
