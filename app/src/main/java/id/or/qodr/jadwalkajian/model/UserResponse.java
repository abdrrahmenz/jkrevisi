package id.or.qodr.jadwalkajian.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by adul on 01/08/17.
 */

public class UserResponse {

    /**
     * result : true
     * users : [{"id":2,"full_name":"Admin1","username":"admin1","password":"e00cf25ad42683b3df678c61f42c6bda","created_at":"2017-04-22 07:57:17","updated_at":"2017-04-22 07:57:17"}]
     */

    private String result;
    private List<Users> users;

    public static UserResponse objectFromData(String str) {

        return new Gson().fromJson(str, UserResponse.class);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}
