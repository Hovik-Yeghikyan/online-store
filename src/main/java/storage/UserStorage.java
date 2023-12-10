package storage;



import model.User;
import types.UserType;
import util.StorageSerializeUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserStorage implements Serializable {

    private Map<String, User> users  = new HashMap<>();

    public void add(User user) {
        users.put(user.getEmail(),user);
        StorageSerializeUtil.serializeUserStorage(this);
    }

    public void printOnlyUsers() {
        for (User value : users.values()) {
            if (value.getType()== UserType.USER){
                System.out.println(value);
            }
        }
    }

    public User getUserEmail(String email) {
        for (User value : users.values()) {
            if (value.getEmail().equals(email)){
                return value;
            }
        }
        return null;
      //  return users.get(email);
    }

    public User getUserById(String id) {
        for (User value : users.values()) {
            if (value.getId().equals(id)){
                return value;
            }
        }
        return null;
    }


    public User getUserEmailAndPassword(String email, String password) {
        for (User value : users.values()) {
            if(value.getEmail().equals(email)&&value.getPassword().equals(password)){
                return value;
            }
        }
        return null;
    }

    public UserType getUserAndAdminType(String type) {
        if (type.equals(UserType.ADMIN.name()) || type.equals(UserType.USER.name())) {
            return UserType.valueOf(type);
        }
        return null;
    }
}

