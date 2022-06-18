package com.example.blog_javacode.people;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.article.CategoryRVAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Other_personFragment extends Fragment implements CategoryRVAdapter.CategoryClickInterface {
    private ArrayList<Person> personArrayList;
    private PeopleRVAdapter peopleRVAdapter;

    private ArrayList<String> categoryArrayList;
    private SwipeRefreshLayout idSRL;

    private final String datasearch;
    private final String account;

    public Other_personFragment(String account, String datasearch ) {
        this.datasearch = datasearch;
        this.account = account;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_person, container, false);

        RecyclerView idRVPeople = view.findViewById(R.id.idRVPeople);
        personArrayList = new ArrayList<>();
        peopleRVAdapter = new PeopleRVAdapter(personArrayList,this.getActivity(),R.layout.other_people_rv_item,account);
        idRVPeople.setAdapter(peopleRVAdapter);
        getPeople();
        peopleRVAdapter.notifyDataSetChanged();

        RecyclerView idRVPropPeople = view.findViewById(R.id.idRVPropPeople);
        categoryArrayList = new ArrayList<>();
        categoryArrayList.add("Tất cả");
        categoryArrayList.add("Theo dõi");
        categoryArrayList.add("Bài viết");
        categoryArrayList.add("Sản phẩm");
        CategoryRVAdapter categoryRVAdapter = new CategoryRVAdapter(categoryArrayList, this.getActivity(), this);
        idRVPropPeople.setAdapter(categoryRVAdapter);

        idSRL = view.findViewById(R.id.idSRL);

        idSRL.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            getPeople();
            idSRL.setRefreshing(false);
        }, 2500));

        return view;
    }

    private void getPeople(){
        personArrayList.clear();
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<Person>> callback = dataClient.getOtherPeople(account,datasearch);
        callback.enqueue(new Callback<ArrayList<Person>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ArrayList<Person>> call, @NonNull Response<ArrayList<Person>> response) {
                ArrayList<Person> modal = response.body();
                for(int i = 0; i< Objects.requireNonNull(modal).size(); i++){
                    Person person = new Person(modal.get(i).getAccount(),modal.get(i).getName(),modal.get(i).getIntro(),modal.get(i).getView(),
                            modal.get(i).getProductNumber(),modal.get(i).getPostNumber(),modal.get(i).getAvatar());
                    personArrayList.add(person);
                }
                Log.d("AAAA","Lay du lieu thanh cong");
                peopleRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Person>> call, @NonNull Throwable t) {
                Log.d("AAAA", t.getLocalizedMessage());
            }
        });
    }

    public static class PersonComparator implements Comparator<Person> {
        private final String property;

        public PersonComparator(String property) {
            this.property = property;
        }

        @Override
        public int compare(Person p1, Person p2 ) {
            switch (property){
                case "Theo dõi":
                    int v1 = p1.getView();
                    int v2 = p2.getView();
                    return Integer.compare(v2, v1);
                case "Bài viết":
                    int post1 = p1.getPostNumber();
                    int post2 = p2.getPostNumber();
                    return Integer.compare(post2, post1);
                case "Sản phẩm":
                    int product1 = p1.getProductNumber();
                    int product2 = p2.getProductNumber();
                    return Integer.compare(product2, product1);
            }
            return 0;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCategoryClick(int position) {
        String category = categoryArrayList.get(position);
        if(category.equals("Tất cả"))
            getPeople();
        else
            personArrayList.sort(new PersonComparator(category));
        peopleRVAdapter.notifyDataSetChanged();

    }

}