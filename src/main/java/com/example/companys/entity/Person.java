package com.example.companys.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "userid")
    private String user_id;
    @Column(name = "name")
    private String name;
    @Column(name = "Department")
    private Integer dep_id;
    @Column(name = "mobile")
    private String tel;
    @Column(name = "email")
    private String email;
    @Column(name = "position")
    private String position;
    @Column(name = "gender")
    private String gender;
    public Person(){}
    public Person(String userid, String name, int department, String mobile,
                  String email, String position, String gender) {
        super();
        this.user_id = userid;
        this.name = name;
        this.dep_id = department;
        this.tel = mobile;
        this.email = email;
        this.position = position;
        this.gender = gender;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDep_id(Integer dep_id) {
        this.dep_id = dep_id;
    }
    public Integer getDep_id() {
        return dep_id;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getTel() {
        return tel;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public String getPosition() {
        return position;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender(){return gender;}


    @Override
    public String toString() {
        return "Person [userid=" + this.user_id + ", name=" + this.name + ", department="
                + dep_id + ", mobile=" + tel + ", email=" + email
                + ", position=" + position + ", gender=" + gender + "]";
    }

    public String toStrings() {
        return name +","+ user_id + "," + tel + "," + email + "," + dep_id + "," + position + "," + gender;
    }

    public String toJson(){
        String str="{\"userid\": \"%s\",\"name\":\"%s\",\"department\": [%d],\"mobile\":\"%s\",\"email\":\"%s\",\"position\":\"%s\",\"gender\":\"%s\"}";
        return String.format(str,this.user_id,this.name,this.dep_id,this.tel,this.email,this.position,this.gender);

    }
}

