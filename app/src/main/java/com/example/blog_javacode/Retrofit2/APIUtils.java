package com.example.blog_javacode.Retrofit2;

public class APIUtils {
    public static final String Base_Url = "https://theskinery.000webhostapp.com/";
    public static final String Base_Url_image = "https://res.cloudinary.com/dw2roke3d/image/upload/v1652117032/";

    public static DataClient getData(){
        return RetrofitClient.getClient(Base_Url).create(DataClient.class);
    }
}
