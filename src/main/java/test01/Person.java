package test01;

public class Person {

    private String name;

    private String Id;

    private Integer age;

    public Person(String name, String id, Integer age) {
        this.name = name;
        Id = id;
        this.age = age;
    }
    public Person(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return Thread.currentThread().getName() + " : Person{" +
                "name='" + name + '\'' +
                ", Id='" + Id + '\'' +
                ", age=" + age +
                '}';
    }
}
