package romanow.abc.vkr.exam.SwaggerAPI;

import retrofit2.Call;
import retrofit2.http.*;

import romanow.abc.vkr.exam.model.CreateTeacherBean;
import romanow.abc.vkr.exam.model.TeacherBean;

import java.util.List;

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

}
