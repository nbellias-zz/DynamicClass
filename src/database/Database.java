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
    private String dbName;

    public Database() {
    }

    public Database(String directory, String dbName) throws IOException, RecordsFileException {
        this.directory = directory;
        this.dbName = dbName;
        File datafile;
        RecordsFile recordsFile;
        if (!(datafile = new File(directory, dbName + ".db")).exists()) {
            recordsFile = new RecordsFile(directory + "/" + dbName + ".db", 64); //create database
        } else {
            datafile.delete();
            recordsFile = new RecordsFile(directory + "/" + dbName + ".db", 64); //recreate
            //recordsFile= new RecordsFile(directory + "/" + name + ".db", "r"); //open database for read
        }
        recordsFile.close();
    }

    public String getDirectory() {
        return directory;
    }

    public String getDbName() {
        return dbName;
    }

}
