package entity;

/*
@author:zhengzhao
@time: 2018/08/20 
*/

public class Items {
    private int id  , price , num ;
    private String name , city , picture;
    public Items(){

    }

    public Items(int id ,String name ,String city ,int price ,int num ,String picture){
        this.id = id;
        this.name =name;
        this.city = city;
        this.price = price;
        this.num = num;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumber() {
        return num;
    }

    public void setNumber(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Items) {
            Items i = (Items) o;
            if (this.getId()==i.getId()&&this.getName().equals(i.getName())){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getId()+this.getName().hashCode();
    }

    @Override
    public String toString() {
        return  "商品编号" + id +" "+
                "单价" + price +" "+
                "库存" + num +" "+
                "商品名字 " + name  +" "+
                "产地 " + city  ;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
