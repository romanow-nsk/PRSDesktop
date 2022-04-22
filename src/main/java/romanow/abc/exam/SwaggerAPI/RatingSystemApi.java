package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.CreateRatingSystemBean;
import romanow.abc.exam.model.RatingSystemBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface RatingSystemApi {
  
  /**
   * Create rating mapping
   * 

   * @param body  (required)

   * @return Call&lt;RatingSystemBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("rating-systems")
  Call<RatingSystemBean> create1(
    @retrofit2.http.Body CreateRatingSystemBean body
  );

  
  /**
   * Get all rating mappings
   * 

   * @return Call&lt;Void&gt;

   */
  
  
  
    
  @GET("rating-systems")
  Call<Void> getAll1();
    

  
  /**
   * Get one rating mapping
   * 

   * @param id  (required)

   * @return Call&lt;RatingSystemBean&gt;

   */
  
  
  
    
  @GET("rating-systems/{id}")
  Call<RatingSystemBean> getOne1(
    @retrofit2.http.Path("id") Long id
  );

  
}

