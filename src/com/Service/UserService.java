package com.Service;

import com.DAO.UserDAO;
import com.Model.UserInfo;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public UserInfo getUserByIdAndPwd(String uid, String pwd){
        String saltAdded = "";
        saltAdded = (String) userDAO.queryScalar("SELECT user_salt from user where user_name = ?", uid);
        if(saltAdded.equals("")){
            return null;
        }
        return userDAO.querySingle("SELECT * from user where user_name = ? and user_hash = md5(?)", UserInfo.class, uid, (pwd+saltAdded));
    }

    public boolean ifUserExist(String uid){
        Long temp = (Long) userDAO.queryScalar("SELECT 1 FROM user WHERE user_name = ? limit 1", uid);
        return temp != null;
    }

    public boolean addUser(String uid, String pwd, String salt){
        return userDAO.dmlUpdate("INSERT INTO user VALUES(null, ?, MD5(?), ?)", uid, pwd + salt, salt) > 0;
    }


    public boolean setUserPwd(String uid, String pwd){
        return userDAO.dmlUpdate("UPDATE user SET user_hash = md5(?) WHERE user_name = ?", pwd, uid) > 0;
    }
}
