package com.example.newproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category_Main extends AppCompatActivity {
    private ProgressDialog pDialog;

    private RecyclerView recyclerView;
    private CategoryAdapter category;
    private List<Category> categoryList;
    Intent intent;
    ProgressBar progressBar;
    private static String url = "http://limagangsal.com/project/ppns_sickness/api/send/answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = new Intent(Category_Main.this,Recomend.class);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        categoryList = new ArrayList<>();
        category = new CategoryAdapter(this, categoryList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(category);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btn = (Button)findViewById(R.id.btn_send);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.category));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,};

        Category a = new Category("Perokok", covers[0]);
        categoryList.add(a);

        a = new Category("Tekanan darah", covers[1]);
        categoryList.add(a);

        a = new Category("Diabetes", covers[2]);
        categoryList.add(a);

        a = new Category("Kolesterol", covers[3]);
        categoryList.add(a);


        category.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void sendRequest() {
        final SharedPrefManager mPref = new SharedPrefManager(this);
        Register_con user = mPref.getUser();
        JSONObject sendJson = new JSONObject();
        JSONObject userJson = new JSONObject();
        JSONArray categoryJson = new JSONArray();
        JSONObject itemJson;
        try{
            //JSON user
            userJson.put("age",user.getAge());
            userJson.put("name",user.getName());
            userJson.put("marriage",user.getMarriage());
            userJson.put("date_birth",user.getDate());
            userJson.put("position",user.getPosition());
            userJson.put("gender",user.getGender());
            userJson.put("nik_npk",user.getNik());
            sendJson.put("user",userJson);
            //cholesterol
            itemJson = new JSONObject();
            itemJson.put("description","cholesterol");
            itemJson.put("name","cholesterol");
            itemJson.put("question_ids",mPref.get_array("cholesterol"));
            categoryJson.put(itemJson);
            itemJson = null;
            //smoking
            itemJson = new JSONObject();
            itemJson.put("description","smoking");
            itemJson.put("name","smoking");
            itemJson.put("question_ids",mPref.get_array("smoking"));
            categoryJson.put(itemJson);
            itemJson = null;
            //diabetes
            itemJson = new JSONObject();
            itemJson.put("description","diabetes");
            itemJson.put("name","diabetes");
            itemJson.put("question_ids",mPref.get_array("diabetes"));
            categoryJson.put(itemJson);
            itemJson = null;
            //blood pressure
            itemJson = new JSONObject();
            itemJson.put("description","blood pressure");
            itemJson.put("name","blood pressure");
            itemJson.put("question_ids",mPref.get_array("blood pressure"));
            categoryJson.put(itemJson);
            itemJson = null;

            sendJson.put("categories",categoryJson);
            Log.d("REQUEST FOR ANSWER",sendJson.toString());

        }catch (Exception e){

        }
        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.POST, url, sendJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RESPONSE FOR ANSWER",response.toString());
                Log.d("TAG","====================== SUCCESS ========================");
                mPref.save_string("result", response.toString());
                startActivity(new Intent(Category_Main.this, Result.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("RESPONSE FROM ANSWER",error.toString());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(objRequest);

    }

}