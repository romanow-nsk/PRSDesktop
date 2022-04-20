package romanow.abc.bridge;

import retrofit2.Call;
import retrofit2.Response;
import romanow.abc.core.constants.ValuesBase;


import java.io.IOException;

public abstract class APICallSync0 {
    public static String httpError(Response res){
        String ss = res.message()+" ("+res.code()+") ";
        try {
            ss+=res.errorBody().string();
            } catch (IOException e) {}
        return ss;
        }
    public abstract Call apiFun();
    public APICallSync0(){}
    public Response call() throws IOException {
        String mes="";
        String mes1="";
        Response res;
        long tt;
        try {
            tt = System.currentTimeMillis();
            res = apiFun().execute();
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
        return res;
        }
    }
