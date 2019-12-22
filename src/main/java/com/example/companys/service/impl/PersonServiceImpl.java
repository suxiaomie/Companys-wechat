package com.example.companys.service.impl;

import com.example.companys.dao.PersonDao;
import com.example.companys.entity.Person;
import com.example.companys.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonDao dao;
    //添加人员
    @Override
    public Person addPerson(Person person) {
        Person person1=dao.save(person);
        return person1;
    }
    //删除人员
    @Override
    public String deletePerson(String userid) {
        dao.deleteById(userid);
        return "删除成功";
    }
    //查询人员
    @Override
    public Person queryByUserId(String userid) {
        Optional<Person> optionalPerson=dao.findById(userid);
        Person person=optionalPerson.get();
        return person;
    }
    //更新人员
    @Override
    public String updatePerson(Person person) {
        dao.save(person);
        return "更新成功";
    }
    //查询全部信息
    @Override
    public List<Person> queryAll() {
        List<Person> list=dao.findAll();
        return list;
    }
}
