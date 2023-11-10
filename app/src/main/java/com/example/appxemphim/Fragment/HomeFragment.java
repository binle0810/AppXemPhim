package com.example.appxemphim.Fragment;

import android.content.Intent;
import android.content.res.Resources;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;



import com.example.appxemphim.Activities.MoviesActivity;
import com.example.appxemphim.Activities.MoviesInformationActivity;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import java.util.Map;
import com.example.appxemphim.R;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {
    private ViewFlipper viewFlipper;

    private TextView textView_category, textView_nation,
            btn_more_recommend_movies, btn_more_action_movies, btn_more_comedy_movies, btn_more_fiction_movies,
            btn_more_cartoon_movies;

    private ProgressBar progressBar_recommend_movies, progressBar_action_movies, progressBar_comedy_movies,
            progressBar_fiction_movies, progressBar_cartoon_movies;
    private DatabaseReference mDatabase;
    private LinearLayout recommend_movies_layout, action_movies_layout,
            comedy_movies_layout, fiction_movies_layout, cartoon_movies_layout;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public HomeFragment() {

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

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);


        viewFlipper.setFlipInterval(3000);//3s
        viewFlipper.setAutoStart(true);

        nemuCategoryOnClick();
        showMoreMoviesOnClick();
        browserData();
        return view;
    }

    private void initUI(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        recommend_movies_layout = view.findViewById(R.id.recommend_movies_layout);
        progressBar_recommend_movies = view.findViewById(R.id.progressBar_recommend_movies);
        progressBar_action_movies = view.findViewById(R.id.progressBar_action_movies);
        action_movies_layout = view.findViewById(R.id.action_movies_layout);
        btn_more_recommend_movies = view.findViewById(R.id.btn_more_recommend_movies);
        btn_more_action_movies = view.findViewById(R.id.btn_more_action_movies);
        textView_category = view.findViewById(R.id.textView_category);
        textView_nation = view.findViewById(R.id.textView_nation);
        btn_more_comedy_movies = view.findViewById(R.id.btn_more_comedy_movies);
        progressBar_comedy_movies = view.findViewById(R.id.progressBar_comedy_movies);
        comedy_movies_layout = view.findViewById(R.id.comedy_movies_layout);
        btn_more_fiction_movies = view.findViewById(R.id.btn_more_fiction_movies);
        progressBar_fiction_movies = view.findViewById(R.id.progressBar_fiction_movies);
        fiction_movies_layout = view.findViewById(R.id.fiction_movies_layout);
        btn_more_cartoon_movies = view.findViewById(R.id.btn_more_cartoon_movies);
        progressBar_cartoon_movies = view.findViewById(R.id.progressBar_cartoon_movies);
        cartoon_movies_layout = view.findViewById(R.id.cartoon_movies_layout);
        viewFlipper = view.findViewById(R.id.viewFlipper);
    }


    private void showMoreMoviesOnClick() {
        btn_more_recommend_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("Phim đề cử");
            }
        });
        btn_more_action_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("Phim hành động");
            }
        });

        btn_more_comedy_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("Phim hài hước");
            }
        });
        btn_more_fiction_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("Phim viễn tưởng");
            }
        });
        btn_more_cartoon_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("Phim hoạt hình");
            }
        });
    }

    private void sendData(String value) {
        Intent intent = new Intent(getActivity(), MoviesActivity.class);
        intent.putExtra("id", value);
        startActivity(intent);
    }

    private void nemuCategoryOnClick() {
        textView_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(getActivity(), v);
                pm.getMenuInflater().inflate(R.menu.category_popup_menu, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.category_item1)
                            sendData("Phim chiến tranh");
                        else if (item.getItemId() == R.id.category_item2)
                            sendData("Phim hành động");
                        else if (item.getItemId() == R.id.category_item3)
                            sendData("Phim tâm lý");
                        else if (item.getItemId() == R.id.category_item4)
                            sendData("Phim viễn tưởng");
                        else if (item.getItemId() == R.id.category_item5)
                            sendData("Phim kinh dị");
                        else if (item.getItemId() == R.id.category_item6)
                            sendData("Phim hài hước");
                        else if (item.getItemId() == R.id.category_item7)
                            sendData("Phim hoạt hình");
                        return true;
                    }
                });
                pm.show();
            }
        });

        textView_nation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(getActivity(), v);
                pm.getMenuInflater().inflate(R.menu.nation_popup_menu, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.nation_item1)
                            sendData("Việt Nam");
                        else if (item.getItemId() == R.id.nation_item2)
                            sendData("Thái Lan");
                        else if (item.getItemId() == R.id.nation_item3)
                            sendData("Hàn Quốc");
                        else if (item.getItemId() == R.id.nation_item4)
                            sendData("Mỹ");
                        else if (item.getItemId() == R.id.nation_item5)
                            sendData("Hồng Kông");
                        else if (item.getItemId() == R.id.nation_item6)
                            sendData("Âu");
                        return true;
                    }
                });
                pm.show();
            }
        });
    }

    private void browserData() {

        mDatabase.child("Movies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    collectData((Map<String, Object>) dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void collectData(Map<String, Object> listresult) {

        collectDataRecommend(listresult);
        collectDataAction(listresult, "category", "Hành động", action_movies_layout, progressBar_action_movies);
        collectDataAction(listresult, "category", "Hài hước", comedy_movies_layout, progressBar_comedy_movies);
        collectDataAction(listresult, "category", "Viễn tưởng", fiction_movies_layout, progressBar_fiction_movies);
        collectDataAction(listresult, "category", "Hoạt hình", cartoon_movies_layout, progressBar_cartoon_movies);
    }

    private void collectDataRecommend(Map<String, Object> listresult) {

        ArrayList<String> listAfter = new ArrayList<String>();
        int z = 0;
        for (Map.Entry<String, Object> entry : listresult.entrySet()) {

            Map singleUser = (Map) entry.getValue();
            String name = singleUser.get("name").toString();
            listAfter.add(name);
            z++;
        }
        Object[] arr = listAfter.toArray();
        for (int i = 0; i < (arr.length / 2); i++) {
            if (i == 0) {
            }
            getInfor(arr[i].toString(), recommend_movies_layout, progressBar_recommend_movies);
        }

    }

    private void collectDataAction(Map<String, Object> listresult, String values, String text, LinearLayout linearlayout, ProgressBar progressbar) {

        ArrayList<String> listAfter = new ArrayList<String>();

        for (Map.Entry<String, Object> entry : listresult.entrySet()) {

            Map singleUser = (Map) entry.getValue();
            String value = singleUser.get(values).toString();
            if (value.contains(text)) {
                String name = singleUser.get("name").toString();
                listAfter.add(name);
            }
        }

        Object[] arr = listAfter.toArray();
        for (int i = 0; i < (arr.length); i++) {
            getInfor(arr[i].toString(), linearlayout, progressbar);
        }
    }

    private void getInfor(String IDMOVIES, LinearLayout linearlayout, ProgressBar progressbar) {
        mDatabase.child("Movies").child(IDMOVIES).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {

                        DataSnapshot dataSnapshot = task.getResult();
                        String nameMovies = String.valueOf(dataSnapshot.child("name").getValue());
                        String linkMovies=String.valueOf(dataSnapshot.child("link_poster").getValue());
                        setLayoutSearch(nameMovies, linkMovies, linearlayout, progressbar);

                    }
                }
            }
        });
    }

    private void setLayoutSearch(String text, String poster_link, LinearLayout linearlayout, ProgressBar progressbar) {
        LinearLayout parent = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dpToPx(10), dpToPx(20), 0);
        parent.setLayoutParams(params);
        parent.setOrientation(LinearLayout.VERTICAL);
        //////Click vào poster
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoviesInformationActivity.class);
                intent.putExtra("id", text);
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

        linearlayout.addView(parent);
        parent.addView(cardView1);
        cardView1.addView(cardView2);
        cardView2.addView(imageView);
        parent.addView(textView);

        progressbar.setVisibility(View.INVISIBLE);

    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}