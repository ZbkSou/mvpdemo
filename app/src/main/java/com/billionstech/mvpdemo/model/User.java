package com.billionstech.mvpdemo.model;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:
 * {
 "code": "200",
 "data": {
 "userName": "Steven",
 "userSex": "man",
 "id": 1
 },
 "msg": "success"
 }
 */
public class User {


        private String userName;
        private String userSex;
        private int id;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

}
