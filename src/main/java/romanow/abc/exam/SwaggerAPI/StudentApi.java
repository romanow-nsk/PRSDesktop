package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.StudentBean;
import romanow.abc.exam.model.StudentExamInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StudentApi {
  /**
   * Create many students
   * 
   * @param body  (required)
   * @return Call&lt;List&lt;StudentBean&gt;&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("students/bulk")
  Call<List<StudentBean>> addStudents(
                    @retrofit2.http.Body List<StudentBean> body    
  );

  /**
   * Create one student
   * 
   * @param body  (required)
   * @return Call&lt;StudentBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("students")
  Call<StudentBean> createStudent(
                    @retrofit2.http.Body StudentBean body    
  );

  /**
   * Get info about sender student
   * 
   * @return Call&lt;StudentBean&gt;
   */
  @GET("students/me")
  Call<StudentBean> getSelf();
    

  /**
   * Get student by id
   * 
   * @param studentId  (required)
   * @return Call&lt;StudentBean&gt;
   */
  @GET("students/{studentId}")
  Call<StudentBean> getStudent(
            @retrofit2.http.Path("studentId") Long studentId            
  );

  /**
   * Get student&#x27;s tickets
   * 
   * @return Call&lt;List&lt;StudentExamInfoBean&gt;&gt;
   */
  @GET("students/exam-infos")
  Call<List<StudentExamInfoBean>> getTickets();
    

}
