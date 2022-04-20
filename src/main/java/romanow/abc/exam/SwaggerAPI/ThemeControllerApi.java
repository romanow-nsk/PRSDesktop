package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.ThemeBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface ThemeControllerApi {
  
  /**
   * Get one theme
   * 

   * @param themeId  (required)

   * @return Call&lt;ThemeBean&gt;

   */
  
  
  
    
  @GET("theme/{themeId}")
  Call<ThemeBean> getOne(
    @retrofit2.http.Path("themeId") Long themeId
  );

  
}

