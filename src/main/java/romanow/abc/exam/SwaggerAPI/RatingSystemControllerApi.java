package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.RatingSystemBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface RatingSystemControllerApi {
  
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

