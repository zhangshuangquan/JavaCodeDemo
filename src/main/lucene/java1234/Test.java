package java1234;

/**
 * Created by zsq on 2017/2/21.
 * 面向对象 继承 的  向上转型  和  向下转型
 */
public class Test{

    public static void main(String[] args) {
        Animal animal = new Animal();
        animal.say();  // this is a Animal class
        Cat cat = new Cat();
        cat.say();     // this is a Cat class
        cat.catchMice();   //cat can catch mouse
        animal = cat;   // 向上转型  转为 父类
        //子类向上转型为父类后表现出仍是子类的特征
        animal.say();   // this is a Cat class
        System.out.println(animal.a);  // 20
        System.out.println(cat.a);  // 30

        // animal.catchMice();  上转型后  把子类当做父类后 已经不具备父类没有的特征

        cat = (Cat) animal;  //向下转型
        cat.catchMice();  //cat can catch mouse

    }
}

class Animal {

    public int a = 20;

    public void say(){
        System.out.println("this is a Animal class");
    }
}

class Cat extends Animal{

    public int a = 30;

    public void say() {
        System.out.println("this is a Cat class");
    }

    public void catchMice(){
        System.out.println("cat can catch mouse");
    }
}