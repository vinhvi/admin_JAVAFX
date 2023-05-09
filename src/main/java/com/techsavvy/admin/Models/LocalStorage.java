package com.techsavvy.admin.Models;

import java.io.*;

public class LocalStorage {

    public void saveToken(String token) throws IOException {
        FileOutputStream fileOut = new FileOutputStream("token_user");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(token);
        out.close();
        fileOut.close();
        System.out.println("Đã lưu token_user !!");
    }


    public void saveEmployee(String maNV) throws IOException{
        FileOutputStream fileOut = new FileOutputStream("employee.eer");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(maNV);
        out.close();
        fileOut.close();
        System.out.println("Đã lưu " + maNV);
    }

    public String getTokenInLocal() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("token_user");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        String token = (String) in.readObject();
        in.close();
        fileIn.close();
        return token;
    }

    public String getEmployeeInLocal() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("employee.eer");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        String maNV = (String) in.readObject();
        in.close();
        fileIn.close();
//        System.out.println("employee: " + employee);
        return maNV;
    }

    public void saveEmailEmployee(String email) throws IOException {
        FileOutputStream fileOut = new FileOutputStream("email_employee.eer");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(email);
        out.close();
        fileOut.close();
        System.out.println("Đã lưu " + email);
    }

    public String getEmailEmployeeInLocal() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("email_employee.eer");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        String email = (String) in.readObject();
        in.close();
        fileIn.close();
//        System.out.println("employee: " + employee);
        return email;
    }
}
