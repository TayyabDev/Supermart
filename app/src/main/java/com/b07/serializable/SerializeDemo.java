package com.b07.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.users.Admin;

public class SerializeDemo {
  // All data stored in the database should be outputted into a file called database_copy.ser
  private static final String filepath = "database_copy.ser";

  /**
   * Serializes the given object to the file at the filepath.
   * 
   * @param obj The object to serialize.
   * @param Admin to serialize
   * @throws IOException If the ID’s do not align with the ones in your Enum classes
   * @throws SQLException when there is an invalid idinput
   */
  public static void serialize(Object obj, Admin admin) throws IOException, SQLException {
    // check if the Admin is valid, else throw a new exception
    if (DatabaseSelectHelper.checkUserId(admin.getId()) == true) {
      throw new SQLException();
    }
    FileOutputStream fileOut = new FileOutputStream(filepath);
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(obj);
    out.close();
    fileOut.close();
    System.out.println("Serialized data is saved in" + filepath);
  }

  /**
   * Deserializes the next object in the given serialized file.
   * 
   * @param Admin to deserialize.
   * @throws IOException If the user is no longer in the database, or if
   *         the database fails to load.
   * @throws ClassNotFoundException If a superclass of the deserialized object has since been
   *         modified.
   * @throws SQLException when there is an invalid idinput
   */
  public static Object deserialize(Admin admin) throws IOException, ClassNotFoundException, SQLException {
    // check if the Admin is valid, else throw a new exception
    if (DatabaseSelectHelper.checkUserId(admin.getId()) == true) {
      throw new SQLException();
    }
    FileInputStream fileIn = new FileInputStream(filepath);
    ObjectInputStream in = new ObjectInputStream(fileIn);
    Object deserialized = null;
    deserialized = in.readObject();
    in.close();
    fileIn.close();
    // clear the database
    Path path = Paths.get("Inventory.db");
    Files.delete(path);
    return deserialized;
  }
  
}
