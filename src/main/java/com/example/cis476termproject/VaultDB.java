package com.example.cis476termproject;

import java.sql.*;
import java.util.ArrayList;

public class VaultDB {
    private static Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:sqlite:vault.db";
        return DriverManager.getConnection(dbUrl);
    }

    public static void addCredentials(Credentials credentials){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into Credentials ( OwnerID, URL, Username, Password ) values ( ?, ?, ?, ? )");

            preparedStatement.setInt(1, credentials.getOwnerID());
            preparedStatement.setString(2, credentials.getURL());
            preparedStatement.setString(3, credentials.getUsername());
            preparedStatement.setString(4, credentials.getPassword());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static void updateCredentials(Credentials credentials){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("update credentials set OwnerID = ?, URL = ?, Username = ?, Password = ? where ID = ?");

            preparedStatement.setInt(1, credentials.getOwnerID());
            preparedStatement.setString(2, credentials.getURL());
            preparedStatement.setString(3, credentials.getUsername());
            preparedStatement.setString(4, credentials.getPassword());
            preparedStatement.setInt(5, credentials.getID());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static ArrayList<Credentials> getCredentials(int ownerID){
        ArrayList<Credentials> credentials = new ArrayList<Credentials>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Credentials where OwnerID = ?");

            preparedStatement.setInt(1, ownerID);

            ResultSet credentialsQuery = preparedStatement.executeQuery();

            while (credentialsQuery.next()) {
                int id = credentialsQuery.getInt("ID");
                String url = credentialsQuery.getString("URL");
                String username = credentialsQuery.getString("Username");
                String password = credentialsQuery.getString("Password");

                credentials.add(new Credentials(id, ownerID, url, username, password));
            }
            credentialsQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return credentials;
    }

    public static void deleteCredentials(int id){
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Credentials WHERE ID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static void addCreditCard(CreditCard creditCard){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into CreditCard ( OwnerID, Issuer, CreditCardNumber, CardholderName, ExpirationDate, CCVCode ) values ( ?, ?, ?, ?, ?, ? )");

            preparedStatement.setInt(1, creditCard.getOwnerID());
            preparedStatement.setString(2, creditCard.getIssuer());
            preparedStatement.setInt(3, creditCard.getCreditCardNumber());
            preparedStatement.setString(4, creditCard.getCardholderName());
            preparedStatement.setInt(5, creditCard.getExpirationDate());
            preparedStatement.setInt(6, creditCard.getCCVCode());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static void updateCreditCard(CreditCard creditCard){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("update creditCard set OwnerID = ?, Issuer = ?, CreditCardNumber = ?, CardholderName = ?, ExpirationDate = ?, CCVCode = ? where ID = ?");

            preparedStatement.setInt(1, creditCard.getOwnerID());
            preparedStatement.setString(2, creditCard.getIssuer());
            preparedStatement.setInt(3, creditCard.getCreditCardNumber());
            preparedStatement.setString(4, creditCard.getCardholderName());
            preparedStatement.setInt(5, creditCard.getExpirationDate());
            preparedStatement.setInt(6, creditCard.getCCVCode());
            preparedStatement.setInt(7, creditCard.getID());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static ArrayList<CreditCard> getCreditCard(int ownerID){
        ArrayList<CreditCard> creditCard = new ArrayList<CreditCard>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CreditCard where OwnerID = ?");

            preparedStatement.setInt(1, ownerID);

            ResultSet creditCardQuery = preparedStatement.executeQuery();

            while (creditCardQuery.next()) {
                int id = creditCardQuery.getInt("ID");
                String issuer = creditCardQuery.getString("Issuer");
                int creditCardNumber = creditCardQuery.getInt("CreditCardNumber");
                String cardholderName = creditCardQuery.getString("CardholderName");
                int expirationDate = creditCardQuery.getInt("ExpirationDate");
                int CCVCode = creditCardQuery.getInt("CCVCode");

                creditCard.add(new CreditCard(id, ownerID, issuer, creditCardNumber, cardholderName, expirationDate, CCVCode));
            }
            creditCardQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return creditCard;
    }

    public static void deleteCreditCard(int id){
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM CreditCard WHERE ID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static void addPersonalInfo(PersonalInfo personalInfo){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into PersonalInfo ( OwnerID, LicenseNumber, SocialSecurityNumber, PassportNumber ) values ( ?, ?, ?, ? )");

            preparedStatement.setInt(1, personalInfo.getOwnerID());
            preparedStatement.setString(2, personalInfo.getLicenseNumber());
            preparedStatement.setInt(3, personalInfo.getSocialSecurityNumber());
            preparedStatement.setString(4, personalInfo.getPassportNumber());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static void updatePersonalInfo(PersonalInfo personalInfo){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("update personalInfo set LicenseNumber = ?, SocialSecurityNumber = ?, PassportNumber = ? where OwnerID = ?");

            preparedStatement.setString(1, personalInfo.getLicenseNumber());
            preparedStatement.setInt(2, personalInfo.getSocialSecurityNumber());
            preparedStatement.setString(3, personalInfo.getPassportNumber());
            preparedStatement.setInt(4, personalInfo.getOwnerID());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static PersonalInfo getPersonalInfo(int ownerID){
        PersonalInfo personalInfo = null;

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PersonalInfo where OwnerID = ?");

            preparedStatement.setInt(1, ownerID);

            ResultSet personalInfoQuery = preparedStatement.executeQuery();

            if (personalInfoQuery.next()) {
                String licenseNumber = personalInfoQuery.getString("LicenseNumber");
                int socialSecurityNumber = personalInfoQuery.getInt("SocialSecurityNumber");
                String passportNumber = personalInfoQuery.getString("PassportNumber");

                personalInfo = new PersonalInfo(ownerID, licenseNumber, socialSecurityNumber, passportNumber);
            }
            personalInfoQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return personalInfo;
    }

    public static void deletePersonalInfo(int ownerID){
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM PersonalInfo WHERE OwnerID = ?");
            preparedStatement.setInt(1, ownerID);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static void addSecureNote(SecureNote secureNote){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into SecureNote ( OwnerID, Contents ) values ( ?, ? )");

            preparedStatement.setInt(1, secureNote.getOwnerID());
            preparedStatement.setString(2, secureNote.getContents());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static void updateSecureNote(SecureNote secureNote){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("update secureNote set OwnerID = ?, Contents = ? where ID = ?");

            preparedStatement.setInt(1, secureNote.getOwnerID());
            preparedStatement.setString(2, secureNote.getContents());
            preparedStatement.setInt(3, secureNote.getID());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static ArrayList<SecureNote> getSecureNote(int ownerID){
        ArrayList<SecureNote> secureNote = new ArrayList<SecureNote>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SecureNote where OwnerID = ?");

            preparedStatement.setInt(1, ownerID);

            ResultSet secureNoteQuery = preparedStatement.executeQuery();

            while (secureNoteQuery.next()) {
                int id = secureNoteQuery.getInt("ID");
                String contents = secureNoteQuery.getString("Contents");

                secureNote.add(new SecureNote(id, ownerID, contents));
            }
            secureNoteQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return secureNote;
    }

    public static void deleteSecureNote(int id){
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM SecureNote WHERE ID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static void addUserLogin(UserLogin userLogin){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into UserLogin ( Email, Password, SecurityQuestion1, SecurityAnswer1, SecurityQuestion2, SecurityAnswer2, SecurityQuestion3, SecurityAnswer3 ) values ( ?, ?, ?, ?, ?, ?, ?, ? )");

            preparedStatement.setString(1, userLogin.getEmail());
            preparedStatement.setString(2, userLogin.getPassword());
            preparedStatement.setString(3, userLogin.getSecurityQuestion1());
            preparedStatement.setString(4, userLogin.getSecurityAnswer1());
            preparedStatement.setString(5, userLogin.getSecurityQuestion2());
            preparedStatement.setString(6, userLogin.getSecurityAnswer2());
            preparedStatement.setString(7, userLogin.getSecurityQuestion3());
            preparedStatement.setString(8, userLogin.getSecurityAnswer3());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static void updateUserLogin(UserLogin userLogin){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("update userLogin set Email = ?, Password = ?, SecurityQuestion1 = ?, SecurityAnswer1 = ?, SecurityQuestion2 = ?, SecurityAnswer2 = ?, SecurityQuestion3 = ?, SecurityAnswer3 = ? where ID = ?");

            preparedStatement.setString(1, userLogin.getEmail());
            preparedStatement.setString(2, userLogin.getPassword());
            preparedStatement.setString(3, userLogin.getSecurityQuestion1());
            preparedStatement.setString(4, userLogin.getSecurityAnswer1());
            preparedStatement.setString(5, userLogin.getSecurityQuestion2());
            preparedStatement.setString(6, userLogin.getSecurityAnswer2());
            preparedStatement.setString(7, userLogin.getSecurityQuestion3());
            preparedStatement.setString(8, userLogin.getSecurityAnswer3());
            preparedStatement.setInt(9, userLogin.getID());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){ System.out.println("SQLException: " + e.getMessage()); }
    }

    public static UserLogin getUserLogin(int id){
        UserLogin userLogin = null;

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM UserLogin where ID = ?");

            preparedStatement.setInt(1, id);

            ResultSet userLoginQuery = preparedStatement.executeQuery();

            String email = userLoginQuery.getString("Email");
            String password = userLoginQuery.getString("Password");
            String securityQuestion1 = userLoginQuery.getString("SecurityQuestion1");
            String securityAnswer1 = userLoginQuery.getString("SecurityAnswer1");
            String securityQuestion2 = userLoginQuery.getString("SecurityQuestion2");
            String securityAnswer2 = userLoginQuery.getString("SecurityAnswer2");
            String securityQuestion3 = userLoginQuery.getString("SecurityQuestion3");
            String securityAnswer3 = userLoginQuery.getString("SecurityAnswer3");

            userLogin = new UserLogin(id, email, password, securityQuestion1, securityAnswer1, securityQuestion2, securityAnswer2, securityQuestion3, securityAnswer3);
            userLoginQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return userLogin;
    }

    public static UserLogin getUserLoginByEmail(String email){
        UserLogin userLogin = null;

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM UserLogin where Email = ?");

            preparedStatement.setString(1, email);

            ResultSet userLoginQuery = preparedStatement.executeQuery();

            if (userLoginQuery.next()) {
                int id = userLoginQuery.getInt("ID");
                String password = userLoginQuery.getString("Password");
                String securityQuestion1 = userLoginQuery.getString("SecurityQuestion1");
                String securityAnswer1 = userLoginQuery.getString("SecurityAnswer1");
                String securityQuestion2 = userLoginQuery.getString("SecurityQuestion2");
                String securityAnswer2 = userLoginQuery.getString("SecurityAnswer2");
                String securityQuestion3 = userLoginQuery.getString("SecurityQuestion3");
                String securityAnswer3 = userLoginQuery.getString("SecurityAnswer3");

                userLogin = new UserLogin(id, email, password, securityQuestion1, securityAnswer1, securityQuestion2, securityAnswer2, securityQuestion3, securityAnswer3);
            }
            userLoginQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return userLogin;
    }

    public static void deleteUserLogin(int id){
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM UserLogin WHERE ID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
}
