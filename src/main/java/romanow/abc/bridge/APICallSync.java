package romanow.abc.bridge;

import retrofit2.Call;
import retrofit2.Response;
import romanow.abc.core.constants.ValuesBase;

import java.io.IOException;

public abstract class APICallSync<T> {
    public static String httpError(Response res){
        String ss = res.message()+" ("+res.code()+") ";
        try {
            ss+=res.errorBody().string();
            } catch (IOException e) {}
        return ss;
        }
    public abstract Call<T> apiFun();
    public APICallSync(){}
    public T call() throws IOException {
        String mes="";
        String mes1="";
        Response<T> res;
        long tt;
        try {
            tt = System.currentTimeMillis();
            Call<T> req = apiFun();
            res = req.execute();
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
            }
        if (!res.isSuccessful()){
            if (res.code()== ValuesBase.HTTPAuthorization){
                mes =  "Сеанс закрыт " + httpError(res);
                System.out.println(mes);
                throw new IOException(mes);
            }
            try {
                mes = res.errorBody().string();
            } catch (IOException ex){
                mes += "Ошибка: "+ex.toString(); }
            System.out.println(mes);
            throw new IOException(mes);
           }
        System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
        return res.body();
        }
    }
