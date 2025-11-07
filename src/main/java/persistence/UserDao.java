package persistence;


import entities.User;

public  interface UserDao {
    public  boolean LoginUser(String  email,String password);
     public User  findUserByUsername(String  username );
    public int registerUser(User  newuser);
    public  User  findByEmail(User  email);
    public User  Login (String username , String password);
    public boolean updateUserPassword(String password, String username) throws RuntimeException;


}
