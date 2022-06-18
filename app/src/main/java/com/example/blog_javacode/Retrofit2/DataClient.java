package com.example.blog_javacode.Retrofit2;

import com.example.blog_javacode.article.Article;
import com.example.blog_javacode.home.Comment;
import com.example.blog_javacode.people.Person;
import com.example.blog_javacode.people.Post;
import com.example.blog_javacode.store.LinkShop;
import com.example.blog_javacode.store.Product;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface DataClient {
    @FormUrlEncoded
    @POST("phpData/getDataArticle.php")
    Call<ArrayList<Article>> getArticles(@Field("datasearch") String datasearch);

    @FormUrlEncoded
    @POST("phpData/getDataProduct.php")
    Call<ArrayList<Product>> getProducts(@Field("account") String account,@Field("datasearch") String datasearch);

    @FormUrlEncoded
    @POST("phpData/getLinkShop.php")
    Call<ArrayList<LinkShop>> getLinkShop(@Field("id_product") String id_product);

    @FormUrlEncoded
    @POST("phpData/getOtherProduct.php")
    Call<ArrayList<Product>> getOtherProduct(@Field("category") String category);

    @FormUrlEncoded
    @POST("phpData/getOtherPeople.php")
    Call<ArrayList<Person>> getOtherPeople(@Field("account") String account,@Field("datasearch") String datasearch);

    @FormUrlEncoded
    @POST("phpData/getPost.php")
    Call<ArrayList<Post>> getPost(@Field("account") String account,@Field("datasearch") String datasearch);

    @FormUrlEncoded
    @POST("phpData/getCmtPost.php")
    Call<ArrayList<Comment>> getCmtPost(@Field("id") String id);

    @FormUrlEncoded
    @POST("phpData/insertCmtPost.php")
    Call<String> insertCmtPost(@Field("id") String id,
                               @Field("account") String account,
                               @Field("comment") String comment);

    @Multipart
    @POST("phpData/createPost.php")
    Call<String> createPost(@Part List<MultipartBody.Part> images,
                           @Part("account")RequestBody account,
                           @Part("text")RequestBody text);


    @Multipart
    @POST("phpData/createProduct.php")
    Call<String> createProduct(@Part List<MultipartBody.Part> images,
                            @Part("account")RequestBody account, @Part("name_product")RequestBody name_product,
                            @Part("brand")RequestBody brand, @Part("origin")RequestBody origin,
                            @Part("category")RequestBody category, @Part("used_for")RequestBody used_for,
                            @Part("descrip")RequestBody descrip, @Part("uses")RequestBody uses);

    @Multipart
    @POST("phpData/updateInfo.php")
    Call<String> updateInfoWava(@Part MultipartBody.Part avatar,
                               @Part("account")RequestBody account, @Part("password")RequestBody password,
                               @Part("name")RequestBody name, @Part("introduce")RequestBody introduce);

    @FormUrlEncoded
    @POST("phpData/updateInfo.php")
    Call<String> updateInfoNava(@Field("account") String account,
                               @Field("password") String password,
                               @Field("name") String name ,
                                @Field("introduce") String introduce);

    @FormUrlEncoded
    @POST("phpData/login.php")
    Call<Person> login(@Field("account") String account,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("phpData/getPeople.php")
    Call<Person> getPeople(@Field("account") String account);

    @FormUrlEncoded
    @POST("phpData/register.php")
    Call<String> registerNava(@Field("account") String account,
                          @Field("password") String password,
                          @Field("name") String name);

    @Multipart
    @POST("phpData/register.php")
    Call<String> registerWava(@Part MultipartBody.Part avatar,
                                @Part("account")RequestBody account, @Part("password")RequestBody password,
                                @Part("name")RequestBody name);

    @FormUrlEncoded
    @POST("phpData/deletePost.php")
    Call<String> deletePost(@Field("id") String id,@Field("account") String account);
}
