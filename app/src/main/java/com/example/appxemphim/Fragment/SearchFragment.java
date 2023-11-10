package com.example.appxemphim.Fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.appxemphim.Activities.MoviesInformationActivity;
import com.example.appxemphim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;



public class SearchFragment extends Fragment {

    private SearchView text_search;
    private GridLayout show_layout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;
    private Map singleUser;
    private ProgressBar progressBar;
    private String idMovies;

    public SearchFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        text_search = view.findViewById(R.id.text_search);
        show_layout = view.findViewById(R.id.show_layout);
        progressBar = view.findViewById(R.id.progressBar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressBar.setVisibility(View.INVISIBLE);
        browserData();
        return view;
    }
    private void browserData(){
        text_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!text_search.getQuery().toString().isEmpty()){
                    show_layout.removeAllViewsInLayout();
                    progressBar.setVisibility(View.VISIBLE);
                    mDatabase.child("Movies").addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                collectData((Map<String,Object>) dataSnapshot.getValue(), query);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    show_layout.removeAllViewsInLayout();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }
    private void collectData(Map<String,Object> listresult, String text) {

        ArrayList<String> listAfter = new ArrayList<String>();

        for (Map.Entry<String, Object> entry : listresult.entrySet()){
            singleUser = (Map) entry.getValue();
            String name = singleUser.get("name").toString();
            if (name.toLowerCase().contains(text.toLowerCase())) {
                idMovies = singleUser.get("name").toString();
                listAfter.add(idMovies);
            }
        }
        Object[] arr = listAfter.toArray();
        if (arr.length > 0){
            for (int i = 0; i < arr.length; ++i ){
                getInfor(arr[i].toString());
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45));
            params2.setMargins(0, dpToPx(10), 0, 0);
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(params2);
            textView.setText("Không tìm thấy kết quả!");
            textView.setTextColor(getResources().getColor(R.color.White));
            textView.setTextSize(18);
            show_layout.addView(textView);
        }
    }
    private void getInfor(String IDMOVIES){
        mDatabase.child("Movies").child(IDMOVIES).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String nameMovies = String.valueOf(dataSnapshot.child("name").getValue());
                        String linkMovies=String.valueOf(dataSnapshot.child("link_poster").getValue());
                        setLayoutSearch(nameMovies, linkMovies);

                    }
                }
            }
        });
    }
    private void setLayoutSearch(String text, String poster_link){
        LinearLayout parent = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpToPx(15), dpToPx(15), dpToPx(15), 0);
        parent.setLayoutParams(params);
        parent.setOrientation(LinearLayout.VERTICAL);
        ///click vào poster
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , MoviesInformationActivity.class);
                intent.putExtra("id",  text);
                startActivity(intent);
            }
        });

        CardView cardView1 = new CardView(getActivity());
        cardView1.setLayoutParams(new CardView.LayoutParams(dpToPx(165), dpToPx(220)));
        cardView1.setRadius(dpToPx(15));
        cardView1.setCardBackgroundColor(getResources().getColor(R.color.White));

        CardView.LayoutParams params3 = new CardView.LayoutParams(dpToPx(160), dpToPx(215), Gravity.CENTER);
        CardView cardView2 = new CardView(getActivity());
        cardView2.setLayoutParams(params3);
        cardView2.setRadius(dpToPx(15));

        ImageView imageView = new ImageView(getActivity());
        imageView.setId((int) 1);
        Picasso.get().load(poster_link).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundResource(R.drawable.ic_filmreel);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45));
        params2.setMargins(0, dpToPx(10), 0, 0);
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(params2);
        textView.setText(text);
        textView.setTextSize(18);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(getResources().getColor(R.color.White));

        show_layout.addView(parent);
        parent.addView(cardView1);
        cardView1.addView(cardView2);
        cardView2.addView(imageView);
        parent.addView(textView);

        progressBar.setVisibility(View.INVISIBLE);
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}