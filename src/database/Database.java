/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author nikolaos
 */
public class Database {

    private String directory;
    private String name;

    public Database() {
    }

    public Database(String directory, String name) throws IOException, RecordsFileException {
        this.directory = directory;
        this.name = name;
        File datafile;
        RecordsFile recordsFile;
        if (!(datafile = new File(directory, name + ".db")).exists()) {
            recordsFile = new RecordsFile(directory + "/" + name + ".db", 64); //create database
        } else {
            datafile.delete();
            recordsFile = new RecordsFile(directory + "/" + name + ".db", 64); //recreate
            //recordsFile= new RecordsFile(directory + "/" + name + ".db", "r"); //open database for read
        }
        recordsFile.close();
    }

    public String getDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }

}
