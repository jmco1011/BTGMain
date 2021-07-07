package com.example.btgmain;

public class Modal {
    private String Username;
    private String Fname;
    private String Lname;
    private String Password;
    private String Email;
    private  int ID;

    public  String getEmail(){ return Email; }
    public void setEmail(String Email){ this.Email = Email; }
    public String getUsername(){
        return Username;
    }
    public void  setUsername(String Username){
        this.Username = Username;
    }
    public String getFname(){
        return Fname;
    }
    public void setFname(String Fname){
        this.Fname = Fname;
    }
    public String getLname(){
        return Lname;
    }
    public void  setLname(String Lname){
        this.Lname = Lname;
    }
    public String getPassword(){
        return Password;
    }
    public void setPassword(String Password){
        this.Password = Password;
    }
    public int getID(){
        return ID;
    }
    public void setID(int ID){
        this.ID = ID;
    }
    public Modal(String Username, String Fname, String Lname, String Password){
        this.Username = Username;
        this.Fname = Fname;
        this.Lname = Lname;
        this.Password = Password;
    }
}
