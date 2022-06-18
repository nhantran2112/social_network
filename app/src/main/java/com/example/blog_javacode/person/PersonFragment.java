package com.example.blog_javacode.person;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.VPAdapter;
import com.example.blog_javacode.home.PopupCreatePostActivity;
import com.example.blog_javacode.people.Person;
import com.example.blog_javacode.people.PostPersonDetailFragment;
import com.example.blog_javacode.people.ProductPersonDetailFragment;
import com.example.blog_javacode.store.CreateProductActivity;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonFragment extends Fragment {

    private final String account;
    private String password;
    private String name;
    private String introduce;
    private String views;
    private String prodn;
    private String postn;
    private String avatar;
    private PostPersonDetailFragment postPersonDetailFragment;
    private ProductPersonDetailFragment productPersonDetailFragment;


    public PersonFragment(String account, String password, String name, String introduce, String views, String prodn, String postn, String avatar) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.introduce = introduce;
        this.views = views;
        this.prodn = prodn;
        this.postn = postn;
        this.avatar = avatar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        postPersonDetailFragment = new PostPersonDetailFragment(account,account,"");
        productPersonDetailFragment = new ProductPersonDetailFragment(account);

        setuptab(view);

        getdataperson();
        //anh xa
        TextView idTVNamePersonDetail = view.findViewById(R.id.idTVNamePersonDetail);
        TextView idTVViewPersonDetail = view.findViewById(R.id.idTVViewPersonDetail);
        TextView idTVPostNPersonDetail = view.findViewById(R.id.idTVPostNPersonDetail);
        TextView idTVProdNPersonDetail = view.findViewById(R.id.idTVProdNPersonDetail);
        TextView idTVIntroPersonDetail = view.findViewById(R.id.idTVIntroPersonDetail);
        ImageView idIVPersonDetail = view.findViewById(R.id.idIVPersonDetail);
        Button ideditinfo = view.findViewById(R.id.ideditinfo);
        Button idbtncreatepost = view.findViewById(R.id.idbtncreatepost);
        Button idbtncreateproduct = view.findViewById(R.id.idbtncreateproduct);


        //setText
        idTVNamePersonDetail.setText(name);
        idTVViewPersonDetail.setText(views);
        idTVPostNPersonDetail.setText(postn);
        idTVProdNPersonDetail.setText(prodn);
        idTVIntroPersonDetail.setText(introduce);
        Picasso.get().load(APIUtils.Base_Url_image+ avatar).into(idIVPersonDetail);


        //click edit info
        ideditinfo.setOnClickListener(view1 -> {
            Intent i = new Intent( PersonFragment.this.getActivity(), EditInfoActivity.class);
            i.putExtra("account" , account);
            i.putExtra("password" , password);
            i.putExtra("name" , name);
            i.putExtra("introduce" , introduce);
            i.putExtra("views" , views);
            i.putExtra("postn" , postn);
            i.putExtra("prodn" , prodn);
            i.putExtra("avatar" , avatar);
            startActivity(i);
        });

        //click add post
        idbtncreatepost.setOnClickListener(view12 -> {
            Intent i = new Intent( PersonFragment.this.getActivity(), PopupCreatePostActivity.class);
            i.putExtra("account" , account);
            i.putExtra("name" , name);
            i.putExtra("avatar" , avatar);
            startActivity(i);
        });

        //click add product
        idbtncreateproduct.setOnClickListener(view13 -> {
            Intent i = new Intent( PersonFragment.this.getActivity(), CreateProductActivity.class);
            i.putExtra("account" , account);
            i.putExtra("name" , name);
            i.putExtra("avatar" , avatar);
            startActivity(i);
        });



        return view ;
    }

    private void getdataperson(){
        DataClient dataClient = APIUtils.getData();
        Call<Person> callback = dataClient.getPeople(account);
        callback.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if(response.body() != null){
                    Person modal = response.body();
                    password = modal.getPassword();
                    name = modal.getName();
                    introduce = modal.getIntro();
                    avatar = modal.getAvatar();
                    prodn = modal.getProductNumber().toString();
                    postn = modal.getPostNumber().toString();
                    views = modal.getView().toString();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                Log.d("getdataperson" , t.getLocalizedMessage());
            }
        });
    }

    private void setuptab(View view){
        TabLayout tabLayout;
        ViewPager viewPager;
        VPAdapter vpAdapter = new VPAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(postPersonDetailFragment,"");
        vpAdapter.addFragment(productPersonDetailFragment,"");


        tabLayout = view.findViewById(R.id.idtablayoutperson);
        viewPager = view.findViewById(R.id.viewpagerpersondetail);
        viewPager.setAdapter(vpAdapter);

        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_baseline_article_24);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_baseline_store_24);
    }

}