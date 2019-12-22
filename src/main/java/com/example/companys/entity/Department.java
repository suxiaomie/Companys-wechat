package com.example.companys.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "department")
public class Department {
    @Id
    @Column(name = "dep_id")
    private Integer dep_id;
    @Column(name = "name")
    private String name;
    @Column(name = "parentid")
    private Integer parent_id;
    @Column(name = "orders")
    private Integer order;

    public void setDep_id(Integer dep_id) {
        this.dep_id = dep_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public Department(){}
    //参数要和前端 json 包的 key 值相同
    public Department(int dep_id, String name, int parent_id,int order) {
        super();
        this.dep_id = dep_id;
        this.name = name;
        this.parent_id = parent_id;
        this.order=order;
    }
    @Override
    public String toString() {
        return "Department [id=" + dep_id + ", name=" + name + ", parentid="
                + parent_id + ", order=" + order + "]";
    }

    public String toStrings() {
        return name+ "," + dep_id + "," + parent_id ;
    }

    public String toJson(){
        String str="{\"id\": %d,\"name\":\" %s\",\"parentid\": %d,\" order\": %d}";
        return String.format(str,this.dep_id,this.name,this.parent_id,this.order);
    }
}
