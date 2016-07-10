package Parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import Model.primeByKey;

/**
 * Created by user on 2016-07-09.
 */
public class AuthJsonParser {
    public static String getStrFromJson(List<primeByKey> authList)
    {
        if(authList == null)
            return null;

        Gson gson = new Gson();
        String jsonStr = gson.toJson(
                authList,
                new TypeToken<ArrayList<primeByKey>>() {}.getType());

        return jsonStr;
    }
}
