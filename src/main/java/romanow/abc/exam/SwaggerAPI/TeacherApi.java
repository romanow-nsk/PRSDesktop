package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.CreateTeacherBean;
import romanow.abc.exam.model.DisciplineBean;
import romanow.abc.exam.model.TeacherBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface TeacherApi {
  
  /**
   * Create many teachers
   * 

   * @param body  (required)

   * @return Call&lt;List&lt;TeacherBean&gt;&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("teacher/bulk")
  Call<List<TeacherBean>> addTeachers(
    @retrofit2.http.Body List<CreateTeacherBean> body
  );

  
  /**
   * Create one teacher
   * 

   * @param body  (required)

   * @return Call&lt;TeacherBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("teacher")
  Call<TeacherBean> createTeacher(
    @retrofit2.http.Body CreateTeacherBean body
  );

  
  /**
   * Get teacher&#x27;s disciplines
   * 

   * @return Call&lt;List&lt;DisciplineBean&gt;&gt;

   */
  
  
  
    
  @GET("teacher/discipline")
  Call<List<DisciplineBean>> getDisciplines();
    

  
}

