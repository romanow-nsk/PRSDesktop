package romanow.abc.bridge;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.utils.Base64Coder;
import romanow.abc.exam.SwaggerAPI.*;
import romanow.abc.exam.model.*;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConsoleClient {
    @Getter @Setter private String serverIP="";
    @Getter @Setter int serverPort=0;
    @Getter String token="";
    @Getter RestAPI service = null;
    @Getter AccountControllerApi accountApi=null;
    @Getter AnswerControllerApi answerApi=null;
    @Getter ArtefactControllerApi artefactApi=null;
    @Getter DisciplineControllerApi disciplineApi=null;
    @Getter ExamControllerApi examApi=null;
    @Getter ExamRuleControllerApi examRuleApi=null;
    @Getter GroupControllerApi groupApi=null;
    @Getter OnlyAdminApi onlyAdminApi=null;
    @Getter OnlyStudentApi onlyStudentApi=null;
    @Getter OnlyTeacherApi onlyTeacherApi=null;
    @Getter RatingSystemControllerApi ratingSystemApi=null;
    @Getter TaskControllerApi taskApi=null;
    @Getter ThemeControllerApi themeApi=null;

    public boolean isLogged(){
        return service!=null && token.length()!=0;
    }
    public boolean isConnected(){
        return service!=null;
        }
    public void clear(){
        token="";
        service=null;
        }
    public void createService(String ip, String port){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(ValuesBase.HTTPTimeOut, TimeUnit.SECONDS)
            .connectTimeout(ValuesBase.HTTPTimeOut, TimeUnit.SECONDS)
            .build();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://"+ip+":"+port)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();
        service = (RestAPI)retrofit.create(RestAPI.class);
        }
    public  Object createService(String ip,String port,Class face,String token){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(ValuesBase.HTTPTimeOut, TimeUnit.SECONDS)
                .connectTimeout(ValuesBase.HTTPTimeOut, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("X-Access-Token", token).build();
                        okhttp3.Response res = chain.proceed(request);
                        return res;
                        }
                    })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":"+port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(face);
        }
    public String login(String ip, String port, String login, String pass) {
        try {
            if (service==null)
                createService(ip, port);
            String loginBase64 = Base64Coder.encodeString(login + ":" + pass);
            System.out.println(loginBase64);
            System.out.println(Base64Coder.decodeString(loginBase64));
            Response response = new APICallSync0() {
                @Override
                public Call apiFun() {
                    return service.login(loginBase64);
                    }
                }.call();
            String ss = ((ResponseBody)response.body()).string();
            token = ((TokenBody)new Gson().fromJson(ss,TokenBody.class)).getToken();
            if (token==null){
                token = "";
                return "Нет токена сессии";
                }
            else{
                System.out.println("Токен="+token);
                accountApi = (AccountControllerApi)createService(ip,port,AccountControllerApi.class,token);
                answerApi = (AnswerControllerApi)createService(ip,port,AnswerControllerApi.class,token);
                artefactApi = (ArtefactControllerApi)createService(ip,port,ArtefactControllerApi.class,token);
                disciplineApi = (DisciplineControllerApi)createService(ip,port,DisciplineControllerApi.class,token);
                examApi = (ExamControllerApi)createService(ip,port,ExamControllerApi.class,token);
                examRuleApi = (ExamRuleControllerApi) createService(ip,port,ExamRuleControllerApi.class,token);
                groupApi = (GroupControllerApi) createService(ip,port,GroupControllerApi.class,token);
                onlyAdminApi = (OnlyAdminApi) createService(ip,port,OnlyAdminApi.class,token);
                onlyStudentApi = (OnlyStudentApi) createService(ip,port,OnlyStudentApi.class,token);
                onlyTeacherApi = (OnlyTeacherApi) createService(ip,port,OnlyTeacherApi.class,token);
                ratingSystemApi = (RatingSystemControllerApi) createService(ip,port,RatingSystemControllerApi.class,token);
                taskApi = (TaskControllerApi) createService(ip,port,TaskControllerApi.class,token);
                themeApi = (ThemeControllerApi) createService(ip,port,ThemeControllerApi.class,token);
                }
            } catch (IOException ee){
                return ee.toString();
                }
        return "";
        }
    public static void main(String ss[]) throws IOException {
        String ip="217.71.129.139";
        String port="4502";
        String login="romanov@corp.nstu.ru";
        String pass="password";
        final ConsoleClient client = new ConsoleClient();
        String res = client.login(ip,port,login,pass);
        if (res.length()!=0)
            System.out.println(res);
        //DisciplineBean discipline = new DisciplineBean();
        //discipline.setName("Информатика");
        //DisciplineBean discipline2= new APICallSync<DisciplineBean>() {
        //    @Override
        //    public Call<DisciplineBean> apiFun() {
        //        return client.getOnlyAdminApi().create4(discipline);
        //        }
        //    }.call();
        List<DisciplineBean> list = new APICallSync<List<DisciplineBean>>() {
            @Override
            public Call<List<DisciplineBean>> apiFun() {
                return client.getDisciplineApi().getAll5();
                }
            }.call();
        System.out.println(list);
        }

}
