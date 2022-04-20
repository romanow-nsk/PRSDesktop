package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.AccountBean;
import romanow.abc.exam.model.CreateGroupBean;
import romanow.abc.exam.model.CreateTeacherBean;
import romanow.abc.exam.model.DisciplineBean;
import romanow.abc.exam.model.GroupBean;
import romanow.abc.exam.model.StudentBean;
import romanow.abc.exam.model.TeacherBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface OnlyAdminApi {
  
  /**
   * Create many students
   * 

   * @param body  (required)

   * @return Call&lt;List&lt;StudentBean&gt;&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("student/bulk")
  Call<List<StudentBean>> addStudents(
    @retrofit2.http.Body List<StudentBean> body
  );

  
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
   * Create a group
   * 

   * @param body  (required)

   * @return Call&lt;GroupBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("group")
  Call<GroupBean> create2(
    @retrofit2.http.Body CreateGroupBean body
  );

  
  /**
   * Create a discipline
   * 

   * @param body  (required)

   * @return Call&lt;DisciplineBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("discipline")
  Call<DisciplineBean> create4(
    @retrofit2.http.Body DisciplineBean body
  );

  
  /**
   * Create an account
   * Used by admins to create admins

   * @param body  (required)

   * @return Call&lt;AccountBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("account")
  Call<AccountBean> createAccount(
    @retrofit2.http.Body AccountBean body
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
  
  
    
  @POST("student")
  Call<StudentBean> createStudent(
    @retrofit2.http.Body StudentBean body
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
   * Edit group
   * 

   * @param body  (required)

   * @param groupId  (required)

   * @return Call&lt;GroupBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @PUT("group/{groupId}")
  Call<GroupBean> edit(
    @retrofit2.http.Body CreateGroupBean body, @retrofit2.http.Path("groupId") Long groupId
  );

  
  /**
   * Get all accounts
   * 

   * @return Call&lt;List&lt;AccountBean&gt;&gt;

   */
  
  
  
    
  @GET("account")
  Call<List<AccountBean>> findAll1();
    

  
}

