package com.b07.database.helper.android;

import android.content.Context;

import com.b07.database.DatabaseDriverAndroid;

/**
 * Created by Tayyab on 2017-12-01.
 */

public class DatabaseAndroidUpdateHelper extends DatabaseDriverAndroid
  throws SQLException, InvalidInputException, InvalidStringException, InvalidIdException{

    public DatabaseAndroidUpdateHelper(Context context) {
        super(context);
    }

    public boolean updateInventoryQuantity(int quantity, int itemId)
        throws SQLException, InvalidInputException, InvalidIdException {
        boolean quantityCheck = quantity >= 0;
        boolean itemExist = itemExists(itemId);
        boolean itemIdCheck = positiveInt(itemId);

        if (quantityCheck == itemIdCheck == itemExist == true) {
        boolean complete = DatabaseUpdater.updateInventoryQuantity(quantity, itemId);
        complete.close();
        return complete;

        }
        // deal with all the exceptions
        if (quantityCheck == false) {
        throw new InvalidInputException("The quantity of the item entered is not valid.");
        }

        if (itemIdCheck == false) {
        throw new InvalidInputException("The Id that was inputed was not not valid.");
        }

        if (itemExist == false) {
        throw new InvalidIdException("The Id does not exist in the database");
        }
    }

    public static boolean updateRoleName(String name, int id)
        throws SQLException, InvalidInputException, InvalidStringException, InvalidIdException{

        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<Integer> roleidlist = sel.getRoleIds();
        // check if name is appropriate (not null or empty)
        boolean nameCheck = goodString(name);
        boolean idCheck = positiveInt(id);

        if (nameCheck == idCheck == true) {
            for (Integer roleid : roleidlist) {
                // check if the id exists in the list of roles
                if (id == roleid) {
                    boolean complete = super.updateRoleName(name, id);
                    complete.close();
                    return complete;
                }
            }
        }
        if (nameCheck == false) {
        throw new InvalidStringException("The name entered is not valid.");
        }

        if (idCheck == false) {
        throw new InvalidIdException("The Id that was inputed was not not valid.");
        }
        return false;
    }

    public static boolean updateUserName(String name, int userId)
        throws SQLException, InvalidInputException, InvalidStringException, InvalidIdException, InvalidRoleException {

        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<Integer> roleidlist = sel.getRoleIds();

        // check if name is appropriate (not null or empty)
        boolean nameCheck = goodString(name);
        // check if number is not negative
        boolean userIdCheck = positiveInt(userId);
        // check if user exists in the database
        boolean userIdExist = userExists(userId);
        if (nameCheck == userIdCheck == userIdExist == true) {
            for (User user : userlist) {
                if (userId == user.getId()) {
                    boolean complete = super.updateUserName(name, userId);
                    complete.close();
                    return complete;
                }
            }
        }

        if (nameCheck == false) {
        throw new InvalidStringException("The name entered is not valid.");
        }

        if (userIdCheck == false) {
        throw new InvalidIdException("The Id that was inputed was not not valid.");
        }

        if (userIdExist = false) {
        throw new InvalidIdException("The Id of the user does not exist.");
        }
        return false;
        }

    public static boolean updateUserAge(int age, int userId) {

        // perform check for appropriate values
        boolean ageCheck = age >= 0;
        boolean userIdCheck = positiveInt(userId);
        boolean userIdExist = userExists(userId);
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<User> userIdlist = sel.getUsersDetails();

        if (ageCheck == userIdCheck == userIdExist == true) {
            for (User user : userIdlist) {
                if (userId == user.getId()) {
                    boolean complete = super.updateUserAge(age, userId);
                    complete.close();
                    return complete;
                }
            }
        if (ageCheck == false) {
        throw new InvalidInputException("The age entered is not valid.");
        }

        if (userIdCheck == false) {
        throw new InvalidIdException("The Id that was inputed was not not valid.");
        }

        if (userIdExist = false) {
        throw new InvalidIdException("The Id of the user does not exist.");
        }
        return false;

    }

    public static boolean updateUserAddress(String address, int userId)
        throws SQLException, InvalidInputException, InvalidStringException, InvalidIdException, InvalidRoleException {

        // perform check for appropriate values
        boolean addressCheck = goodString(address);
        boolean userIdCheck = positiveInt(userId);
        boolean addressLength = address.length() <= 100;
        boolean userIdExist = userExists(userId);

        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<User> userIdlist = sel.getUsersDetails();

        if (addressCheck == userIdCheck == addressLength == userIdExist == true) {
            for (User user : userIdlist) {
                if (userId == user.getId()) {
                    boolean complete = super.updateUserAddress(address, userId, connection);
                    complete.close();
                    return complete;
                }
            }
        }

        if (addressCheck == false) {
        throw new InvalidStringException("The adress entered is not valid.");
        }

        if (userIdCheck == false) {
        throw new InvalidIdException("The Id that was inputed was not not valid.");
        }

        if (userIdExist = false) {
        throw new InvalidIdException("The Id of the user does not exist.");
        }

        if (addressLength == false) {
        throw new InvalidInputException("The address length input is not valid");
        }

        return false;
        }

    public static boolean updateUserRole(int roleId, int userId)
        throws SQLException, InvalidInputException, InvalidIdException, InvalidRoleException {
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<User> userlist = sel.getUsersDetails();

        // perform checks
        boolean roleIdCheck = positiveInt(roleId);
        boolean userIdCheck = positiveInt(userId);
        boolean userIdExist = userExists(userId);

        if (roleIdCheck == userIdCheck == userIdExist == true) {
            for (User user : userlist) {
                if (userId == user.getId()) {
                    boolean complete = super.updateUserRole(roleId, userId);
                    complete.close();
                    return complete;
                }
            }
        }
        // deal with all the exceptions
        if (roleIdCheck == false) {
        throw new InvalidInputException("The role Id entered is not valid.");
        }

        if (userIdCheck == false) {
        throw new InvalidInputException("The Id that was inputed was not not valid.");
        }

        if (userIdExist = false) {
        throw new InvalidIdException("The Id of the user does not exist.");
        }

        return false;
        }


    public static boolean updateItemName(String name, int itemId)
        throws SQLException, InvalidInputException, InvalidStringException, InvalidIdException {

        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<Item> itemidlist = sel.getAllItems();

        // perform checks
        boolean nameCheck = goodString(name);
        boolean itemIdCheck = positiveInt(itemId);
        boolean itemExist = itemExists(itemId);

        if (nameCheck == itemIdCheck == itemExist == true) {
            for (Item item : itemidlist) {
                if (itemId == item.getId()) {
                    boolean complete = super.updateItemName(name, itemId);
                    complete.close();
                    return complete;
                }
            }
        }
        // deal with all the exceptions
        if (nameCheck == false) {
        throw new InvalidStringException("The name of the item entered is not valid.");
        }

        if (itemIdCheck == false) {
        throw new InvalidInputException("The Id that was inputed was not not valid.");
        }

        if (itemExist == false) {
        throw new InvalidIdException("The Id that was inputed was not in the database");
        }

        return false;
        }


    public static boolean updateItemPrice(BigDecimal price, int itemId)
        throws SQLException, InvalidInputException, InvalidIdException {


            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<Item> itemidlist = sel.getAllItems();

        // perform checks
        boolean priceCheck = (price.doubleValue() > 0);
        boolean itemIdCheck = positiveInt(itemId);
        boolean itemExist = itemExists(itemId);

        if (priceCheck == itemIdCheck == itemExist == true) {
            for (Item item : itemidlist) {
                if (itemId == item.getId()) {
                    boolean complete = super.updateItemPrice(price, itemId);
                    complete.close();
                    return complete;
                }
            }
        }
        // deal with all the exceptions
        if (priceCheck == false) {
            throw new InvalidInputException("The price of the item entered is not valid.");
        }

        if (itemIdCheck == false) {
            throw new InvalidInputException("The Id that was inputed was not not valid.");
        }

        if (itemExist == false) {
            throw new InvalidIdException("The Id does not exist in the database");
        }

        return false;
        }



    public static boolean updateAccountStatus(int accountId, boolean active) throws
        SQLException, InvalidInputException, InvalidRoleException, InvalidIdException {

        boolean complete = false;
        boolean accountIdcheck = positiveInt(accountId) == userExists(accountId);

        if (accountIdcheck == true) {
            complete = super.updateAccountStatus(accountId, active);
            complete.close();
            return complete;
        }
        if (accountIdcheck == false) {
            throw new InvalidIdException("The Id is not valid");
        }
        return complete;
    }



    public static boolean updateUserPassword(String password, int id) throws SQLException,
        InvalidInputException, InvalidRoleException, InvalidIdException, InvalidStringException {
        // perform checks
        boolean wordCheck = goodString(password);
        boolean userIdCheck = positiveInt(id);
        boolean userExist = userExists(id);
        boolean complete = false;
        if (wordCheck == userIdCheck == userExist == true) {
            complete = super.updateUserPassword(password, id);
            return complete;
            complete.close();
        }
        // deal with all the exceptions
        if (wordCheck == false) {
        throw new InvalidStringException("The password entered is not valid.");
        }

        if (userIdCheck == false) {
        throw new InvalidInputException("The Id that was inputed was not not valid.");
        }

        if (userExist == false) {
        throw new InvalidIdException("The Id that was inputed was not in the database");
        }


        return complete;
        }

    /**
     * check if a name is in a correct format.
     *
     * @param name the name that needs to be checked.
     * @return false if name is null or blank, otherwise true
     */

    private static boolean goodString(String name) {
        boolean good = true;
        if (name.equals(null)) {
            good = false;
        }
        if (name.equals("")) {
            good = false;
        }
        return good;
    }

    /**
     * check if a number is positive
     *
     * @param num the number that needs to be checked
     * @return false if num is less than or equals 0. True otherwise
     */
    private static boolean positiveInt(int num) {
        boolean good = true;
        if (num <= 0) {
            good = false;
        }
        return good;
    }

    /**
     * check if the user exists in the database.
     *
     * @param userId the user's Id that needs to be checked
     * @return true if the user exists in the database
     * @throws SQLException if an SQL error exists
     * @throws InvalidInputException if an invalid input exists
     * @throws InvalidIdException if the Id is invalid
     * @throws InvalidRoleException if the role is invalid
     */
    private static boolean userExists(int userId)
            throws SQLException, InvalidInputException, InvalidRoleException, InvalidIdException {
        boolean idExists = false;
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<User> userIdList = sel.getUsersDetails();

        for (User user : userIdList) {
            if (user.getId() == userId) {
                idExists = true;
            }
        }
        return idExists;
    }

    /**
     * check if the item exists in the database.
     *
     * @param itemId the id of the item
     * @return true if item exists in the database
     * @throws SQLException if SQL error occurs
     */
    private static boolean itemExists(int itemId) throws SQLException {
        boolean idExists = false;
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<Item> itemIdList = sel.getAllItems();

        for (Item item : itemIdList) {
            if (itemId == item.getId()) {
                idExists = true;
            }
        }

        return idExists;
    }
}

}
