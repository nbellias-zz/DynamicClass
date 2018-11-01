/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicclass;

import database.RecordWriter;
import database.RecordsFile;
import database.RecordsFileException;
import entity.Entity;
import entity.Attribute;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nikolaos
 */
public class DynamicClass {

    /**
     * @param args the command line arguments
     *
     */
    public static void main(String[] args) throws IOException, RecordsFileException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        // TODO code application logic here
        new File("data").mkdirs();
        
//            if (!filedir.exists()) {
//                filedir.mkdir();
//            } else {
//                filedir.delete();
//                filedir.mkdir();
//            }
        //CREATE ENTITIES AND THEIR CORRESPONDING RANDOM ACCESS FILES
        String entityluName = "Rank"; //lu = look-up table (class) 1-to-many relation
        String entityName = "Employee";
        Entity entitylu = new Entity("data", entityluName, new Attribute("id", "int", 2), new Attribute("title", "String", 50));
        Entity entity = new Entity("data", entityName, new Attribute("id", "int", 2), new Attribute("name", "String", 50), new Attribute("address", "String", 100), new Attribute("rank", entityluName, 100));
        Constructor[] constructors = new Constructor[2];//Because we have 2 classes
        int k = 0;

        //POPULATE THE ENTITY AND SAVE IN THE DATABASE (INSERT)
        //FIRST GET ALL ENTITY CLASSES IN FOLDER "DATA"
        String directory = "data";
        for (File f : new File(directory).listFiles()) {
            if (f.getName().contains(".class")) {
                System.out.println("File Class: " + f.getName() + ", " + f.getName().split("\\.").length);
                List<Class> myContructorTypes = new ArrayList();
                String attributeType;
                int numberOfFields = 0;
                File parentDirectory = f.getParentFile();
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{parentDirectory.toURI().toURL()});
                Class<?> entityClass = classLoader.loadClass(f.getName().split("\\.")[0]);
                System.out.println("Entity Class: " + entityClass.getCanonicalName());
                //Field list
                for (Field field : entityClass.getDeclaredFields()) {
                    if (!field.getName().contains("serialVersionUID")) {
                        switch (field.getGenericType().getTypeName()) {
                            case "int":
                                attributeType = "int";
                                myContructorTypes.add(int.class);
                                break;
                            case "long":
                                attributeType = "long";
                                myContructorTypes.add(long.class);
                                break;
                            case "short":
                                attributeType = "short";
                                myContructorTypes.add(short.class);
                                break;
                            case "boolean":
                                attributeType = "boolean";
                                myContructorTypes.add(boolean.class);
                                break;
                            case "java.lang.String":
                                attributeType = "String";
                                myContructorTypes.add(String.class);
                                break;
                            default:
                                attributeType = field.getName().replace(field.getName().charAt(0), field.getName().toUpperCase().charAt(0));
                                myContructorTypes.add(Class.forName(attributeType));
                                break;
                        }
                        System.out.println("\t" + field.getName() + " " + attributeType);
                        ++numberOfFields;
                    }
                }
                //System.out.println("numberOfArgumentsInConstructor=" + numberOfArgumentsInConstructor);
                Class[] types = new Class[numberOfFields];
                int cnt = 0;
                for (Class t : myContructorTypes) {
                    types[cnt] = t;
                    cnt++;
                }
                Constructor constructor = entityClass.getDeclaredConstructor(types);
                constructors[k] = constructor;
                ++k;
                //Constructor cons = entityClass.getDeclaredConstructor();//No Argument constructor
            }
        }

        //CREATING SAMPLE DATA AND DATA ENTRY
        Object r1 = new Object();
        Object r2 = new Object();
        Object r3 = new Object();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        Object o4 = new Object();
        Object o5 = new Object();

        //Data Entry for Rank
        for (Constructor constructor : constructors) {
            //System.out.println(constructor.getName());
            if (constructor.getName().equals("Rank")) {
                RecordsFile recordsFile = new RecordsFile(directory + "/" + constructor.getName() + ".db", "rw");
                //
                r1 = constructor.newInstance(1, "CEO");
                System.out.println(r1.toString());
                RecordWriter recr1 = new RecordWriter("" + r1.hashCode());
                recr1.writeObject(r1);
                recordsFile.insertRecord(recr1);
                //
                r2 = constructor.newInstance(2, "CIO");
                System.out.println(r2.toString());
                RecordWriter recr2 = new RecordWriter("" + r2.hashCode());
                recr2.writeObject(r2);
                recordsFile.insertRecord(recr2);
                //
                r3 = constructor.newInstance(3, "WORKING EMPLOYEE");
                System.out.println(r3.toString());
                RecordWriter recr3 = new RecordWriter("" + r3.hashCode());
                recr3.writeObject(r3);
                recordsFile.insertRecord(recr3);
                //
                recordsFile.close();
            }
        }

        //Data Entry for Employee    
        for (Constructor constructor : constructors) {
            //System.out.println(constructor.getName());
            if (constructor.getName().equals("Employee")) {
                RecordsFile recordsFile = new RecordsFile(directory + "/" + constructor.getName() + ".db", "rw");
                //
                o1 = constructor.newInstance(1, "Vassilis", "Artemidos", r1);
                System.out.println(o1.toString());
                RecordWriter rec1 = new RecordWriter("" + o1.hashCode());
                rec1.writeObject(o1);
                recordsFile.insertRecord(rec1);
                //
                o2 = constructor.newInstance(2, "Nikos", "-----", r2);
                System.out.println(o2.toString());
                RecordWriter rec2 = new RecordWriter("" + o2.hashCode());
                rec2.writeObject(o2);
                recordsFile.insertRecord(rec2);
                //
                o3 = constructor.newInstance(3, "Maria", "Aspasias", r3);
                System.out.println(o3.toString());
                RecordWriter rec3 = new RecordWriter("" + o3.hashCode());
                rec3.writeObject(o3);
                recordsFile.insertRecord(rec3);
                //
                o4 = constructor.newInstance(4, "Kostas", "Spevsippou", r3);
                System.out.println(o4.toString());
                RecordWriter rec4 = new RecordWriter("" + o4.hashCode());
                rec4.writeObject(o4);
                recordsFile.insertRecord(rec4);
                //
                o5 = constructor.newInstance(5, "Fanh", "Artemisiou", r3);
                System.out.println(o5.toString());
                RecordWriter rec5 = new RecordWriter("" + o5.hashCode());
                rec5.writeObject(o5);
                recordsFile.insertRecord(rec5);
                //
                recordsFile.close();
            }
        }

        //RETRIEVE ALL EMPLOYEE DATA FROM DATABASE AND SHOW
        RecordsFile employees = new RecordsFile(directory + "/" + "Employee" + ".db", "rw");
        Set set = employees.enumerateKeys();

        System.out.println("Employee NumRecords=" + employees.getNumRecords());

        System.out.println("RETRIEVE ALL");
        for (Object key : set) {
            Object o = employees.readRecord((String) key).readObject();
            System.out.println("Tuple with id " + key + " is: " + o.toString());
        }

        //RETRIEVE SPECIFIC DATA FROM DATABASE AND SHOW
        System.out.println("RETRIEVE ONE");
        String name = "Nikos";
        for (Object key : set) {
            Object o = employees.readRecord((String) key).readObject();
            if (o.getClass().getName().equals("Employee")) {
                Field field = o.getClass().getDeclaredField("name");
                field.setAccessible(true);
                if (field.get(o).equals(name)) {
                    System.out.println("Tuple with name " + name + " is: " + o.toString());
                }
            }
        }

        //UPDATE TUPLE AND SHOW
        System.out.println("UPDATE ONE");
        String nameToUpdate = "Nikos";
        for (Object key : set) {
            Object o = employees.readRecord((String) key).readObject();
            if (o.getClass().getName().equals("Employee")) {
                Field field = o.getClass().getDeclaredField("name");
                field.setAccessible(true);
                if (field.get(o).equals(nameToUpdate)) {
                    System.out.println("Tuple with name " + nameToUpdate + " to update is: " + o.toString());
                    Field fieldToUpdate = o.getClass().getDeclaredField("address");
                    fieldToUpdate.setAccessible(true);
                    fieldToUpdate.set(o, "Vryaxidos");
                    RecordWriter rec = new RecordWriter((String) key);
                    rec.writeObject(o);
                    employees.updateRecord(rec);
                }
            }
        }
        //AND SHOW UPDATED RECORD
        for (Object key : set) {
            Object o = employees.readRecord((String) key).readObject();
            if (o.getClass().getName().equals("Employee")) {
                Field field = o.getClass().getDeclaredField("name");
                field.setAccessible(true);
                if (field.get(o).equals(name)) {
                    System.out.println("New tuple with name " + name + " is: " + o.toString());
                }
            }
        }

        //DELETE TUPLE AND SHOW ALL
        System.out.println("UPDATE ONE");
        String nameToDelete = "Kostas";
        for (Object key : set) {
            Object o = employees.readRecord((String) key).readObject();
            if (o.getClass().getName().equals("Employee")) {
                Field field = o.getClass().getDeclaredField("name");
                field.setAccessible(true);
                if (field.get(o).equals(nameToDelete)) {
                    System.out.println("Tuple with name " + nameToDelete + " to delete is: " + o.toString());
                    employees.deleteRecord((String) key);
                    System.out.println("RECORD DELETED!");
                }
            }
        }

        System.out.println("AFTER DELETION WE HAVE");
        for (Object key : set) {
            Object o = employees.readRecord((String) key).readObject();
            System.out.println("Tuple with id " + key + " is: " + o.toString());
        }

        System.out.println("NumRecords left=" + employees.getNumRecords());

        employees.close();
    }

}
