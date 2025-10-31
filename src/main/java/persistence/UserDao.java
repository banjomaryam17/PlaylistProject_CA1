package persistence;

import entities.User;

public  interface UserDao {
    public  boolean LoginUser(String  email,String password);

     public User  findUserByUsername(String  username );

    public int registerUser(User  newuser);
    public  User  findByThereEmail(User  email);

    public User  Login (String username , String password);
    public  boolean updateUserUsername(String usernameToBeUpdated, String username);
    public boolean updateUserEmail(String email, String username) ;
    public boolean updateUserPassword(String password, String username) throws RuntimeException;


}
