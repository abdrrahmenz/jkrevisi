package id.or.qodr.jadwalkajian.model;

import com.google.gson.Gson;

/**
 * Created by adul on 05/08/17.
 */

public class Users {
    /**
     * id : 2
     * full_name : Admin1
     * username : admin1
     * password : e00cf25ad42683b3df678c61f42c6bda
     * created_at : 2017-04-22 07:57:17
     * updated_at : 2017-04-22 07:57:17
     */

    private int id;
    private String full_name;
    private String username;
    private String password;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
