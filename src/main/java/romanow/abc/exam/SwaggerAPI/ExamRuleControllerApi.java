package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.ExamRuleBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface ExamRuleControllerApi {
  
  /**
   * Get one exam rule
   * 

   * @param id  (required)

   * @return Call&lt;ExamRuleBean&gt;

   */
  
  
  
    
  @GET("exam-rule/{id}")
  Call<ExamRuleBean> getOne4(
    @retrofit2.http.Path("id") Long id
  );

  
}

