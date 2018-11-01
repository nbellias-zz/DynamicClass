/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import database.Database;
import database.RecordsFileException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 *
 * @author nikolaos
 */
public class Entity {

    private String dataDir;
    private String entityName;
    private List<Attribute> entityAttributes;

    public Entity() {
    }

    public Entity(String dataDir, String entityName, Attribute... entityAttributesList) throws RecordsFileException {
        this.dataDir = dataDir;
        this.entityName = entityName;
        this.entityAttributes = new ArrayList();
        this.entityAttributes.addAll(Arrays.asList(entityAttributesList));
        try {
            createEntityAndFile();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Entity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Entity(String dataDir, String entityName, List<Attribute> entityAttributes) {
        this.dataDir = dataDir;
        this.entityName = entityName;
        this.entityAttributes = entityAttributes;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<Attribute> getEntityAttributes() {
        return entityAttributes;
    }

    public void setEntityAttributes(List<Attribute> entityAttributes) {
        this.entityAttributes = entityAttributes;
    }

    private void createEntityAndFile() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, RecordsFileException {
        //Create an empty source file
        String className = this.entityName;

        File sourceFile = new File(this.dataDir, className + ".java");

        //Write the source code into the source file .java
        FileWriter writer = new FileWriter(sourceFile);
        writer.write(classTemplate(className)); //here we create the new .java
        writer.close();

        //Compile the saved .java file and create the source file .class
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, Locale.ENGLISH, Charset.forName("UTF-8"));
        fileManager.setLocation(StandardLocation.CLASS_PATH, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
        compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
        fileManager.close();

        sourceFile.deleteOnExit(); //Delete .java source file
        System.out.println("Entity " + className + " has been created!");

        //CREATE ALSO THEIR CORRSPONDING RANDOM ACCESS FILE
        Database db = new Database(this.dataDir, className);
        System.out.println("Datafile " + className + " has also been created!");
    }

    private String classTemplate(String className) {
        int i = 0;
        int randomNumberForHashCode = (new Random().nextInt(9)) + 1;
        int randomNumber = (new Random().nextInt(100)) + 1;

        //Generate the source code, using the the class name
        //Import statements
        String sourceCode = "import java.io.Serializable;\n";
        sourceCode += "import java.util.Objects;\n\n";
        sourceCode += "public class " + className + " implements Serializable {\n\n";
        sourceCode += "  private static final long serialVersionUID = 7" + getSaltStringNum() + "L;\n\n";
        //private class attributes
        for (Attribute attribute : entityAttributes) {
            sourceCode += "  private " + attribute.getAttributeDataType() + " " + attribute.getAttributeName() + ";\n";
        }
        sourceCode += "\n";
        //constructors
        sourceCode += "  public " + className + "(){\n"; //Constructor without any argument
        sourceCode += "  }\n";
        sourceCode += "\n";
        sourceCode += "  public " + className + "("; //Constructor with arguments
        for (Attribute attribute : entityAttributes) {
            ++i;
            sourceCode += attribute.getAttributeDataType() + " " + attribute.getAttributeName();
            if (i != entityAttributes.size()) {
                sourceCode += ", ";
            }
        }
        i = 0;
        sourceCode += ") {\n";
        for (Attribute attribute : entityAttributes) {
            sourceCode += "      this." + attribute.getAttributeName() + " = " + attribute.getAttributeName() + ";\n";
        }
        sourceCode += "  }\n";
        sourceCode += "\n";
        //getters-setters
        for (Attribute attribute : entityAttributes) {
            sourceCode += "  public " + attribute.getAttributeDataType() + " get" + attribute.getAttributeName().replace(attribute.getAttributeName().charAt(0), attribute.getAttributeName().toUpperCase().charAt(0)) + "() {\n";
            sourceCode += "     return " + attribute.getAttributeName() + ";\n";
            sourceCode += "  };\n\n";
            sourceCode += "  public void set" + attribute.getAttributeName().replace(attribute.getAttributeName().charAt(0), attribute.getAttributeName().toUpperCase().charAt(0)) + "(" + attribute.getAttributeDataType() + " " + attribute.getAttributeName() + ") {\n";
            sourceCode += "     this." + attribute.getAttributeName() + " = " + attribute.getAttributeName() + ";\n";
            sourceCode += "  };\n\n";
        }
        //hashCode
        sourceCode += "  @Override\n";
        sourceCode += "  public int hashCode() {\n";
        sourceCode += "     int hash = " + randomNumberForHashCode + ";\n";
        for (Attribute attribute : entityAttributes) {
            sourceCode += "     hash = " + randomNumber + " * hash + Objects.hashCode(this." + attribute.getAttributeName() + ");\n";
        }
        sourceCode += "     return hash;\n";
        sourceCode += "  }\n\n";
        //equals
        sourceCode += "  @Override\n";
        sourceCode += "  public boolean equals(Object obj) {\n";
        sourceCode += "    if (this == obj) {\n";
        sourceCode += "        return true;\n";
        sourceCode += "    }\n";
        sourceCode += "    if (obj == null) {\n";
        sourceCode += "        return false;\n";
        sourceCode += "    }\n";
        sourceCode += "    if (getClass() != obj.getClass()) {\n";
        sourceCode += "        return false;\n";
        sourceCode += "    }\n";
        sourceCode += "    final " + entityName + " other = (" + entityName + ") obj;\n";
        for (Attribute attribute : entityAttributes) {
            sourceCode += "    if (!Objects.equals(this." + attribute.getAttributeName() + ", other." + attribute.getAttributeName() + ")) {\n";
            sourceCode += "        return false;\n";
            sourceCode += "    }\n";
        }
        sourceCode += "    return true;\n";
        sourceCode += "  }\n\n";
        //toString()
        sourceCode += "  @Override\n";
        sourceCode += "  public String toString() {\n";
        sourceCode += "     return \"" + entityName + " { \" + \"";
        for (Attribute attribute : entityAttributes) {
            ++i;
            sourceCode += attribute.getAttributeName() + "=\" + " + attribute.getAttributeName();
            if (i != entityAttributes.size()) {
                sourceCode += " + \", ";
            }
        }
        sourceCode += " + \" }\";\n";
        sourceCode += "  }\n\n";
        sourceCode += "}";//END OF GENERATED CLASS CODE

        return sourceCode;
    }

    /**
     * Creates a string of a random long number with length less than 19 digits.
     *
     * @return
     */
    private static String getSaltStringNum() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();

        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();

        return saltStr;
    }

}
