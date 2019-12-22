package com.example.companys.service;

import com.example.companys.entity.Person;

import java.util.List;

public interface PersonService {
    //添加成员
    Person addPerson(Person person);
    //通过 userid 删除成员
    String deletePerson(String userid);
    //通过 userid 查找成员
    Person queryByUserId(String userid);
    //通过 userid 更改成员
    String updatePerson(Person person);
    //查找全部信息
    List<Person> queryAll();
}
