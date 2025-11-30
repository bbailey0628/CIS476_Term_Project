package com.example.cis476termproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VaultDB {
    static {
        initialize();
    }

    private static Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:sqlite:vault.db";
        return DriverManager.getConnection(dbUrl);
    }

    private static void initialize() {
        try (Connection connection = getConnection()) {
            ensureCredentialsSchema(connection);
            ensureCreditCardSchema(connection);
            ensurePersonalInfoSchema(connection);
            ensureSecureNoteSchema(connection);
        } catch (SQLException e) {
            System.out.println("SQLException initializing schema: " + e.getMessage());
        }
    }

    private static void ensureCredentialsSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Credentials (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OwnerID INTEGER NOT NULL, " +
                    "URL TEXT NOT NULL, " +
                    "Username TEXT NOT NULL, " +
                    "Password TEXT NOT NULL" +
                    ")");
        }
    }

    private static void ensureCreditCardSchema(Connection connection) throws SQLException {
        boolean tableExists = tableExists(connection, "CreditCard");
        boolean hasTextCardNumber = tableExists && hasColumn(connection, "CreditCard", "CardNumber");
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS CreditCard (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OwnerID INTEGER NOT NULL, " +
                    "Issuer TEXT NOT NULL, " +
                    "CardNumber TEXT NOT NULL, " +
                    "CardholderName TEXT NOT NULL, " +
                    "ExpirationDate TEXT NOT NULL, " +
                    "CVCCode TEXT NOT NULL" +
                    ")");
        }
        if (tableExists && !hasTextCardNumber) {
            migrateCreditCardSchema(connection);
        }
    }

    private static void ensurePersonalInfoSchema(Connection connection) throws SQLException {
        boolean tableExists = tableExists(connection, "PersonalInfo");
        boolean hasIdColumn = tableExists && hasColumn(connection, "PersonalInfo", "ID");
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS PersonalInfo (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OwnerID INTEGER NOT NULL, " +
                    "LicenseNumber TEXT, " +
                    "SocialSecurityNumber TEXT, " +
                    "PassportNumber TEXT" +
                    ")");
        }
        if (tableExists && !hasIdColumn) {
            migratePersonalInfoSchema(connection);
        }
    }

    private static void ensureSecureNoteSchema(Connection connection) throws SQLException {
        boolean tableExists = tableExists(connection, "SecureNote");
        boolean hasTitleColumn = tableExists && hasColumn(connection, "SecureNote", "Title");
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS SecureNote (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OwnerID INTEGER NOT NULL, " +
                    "Title TEXT NOT NULL, " +
                    "Contents TEXT NOT NULL" +
                    ")");
        }
        if (tableExists && !hasTitleColumn) {
            migrateSecureNoteSchema(connection);
        }
    }

    private static boolean hasColumn(Connection connection, String tableName, String columnName) throws SQLException {
        try (PreparedStatement pragma = connection.prepareStatement("PRAGMA table_info(" + tableName + ")")) {
            ResultSet rs = pragma.executeQuery();
            while (rs.next()) {
                if (columnName.equalsIgnoreCase(rs.getString("name"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean tableExists(Connection connection, String tableName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?")) {
            preparedStatement.setString(1, tableName);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }
    }

    private static void migrateCreditCardSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE CreditCard RENAME TO CreditCard_old");
            statement.execute("CREATE TABLE CreditCard (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OwnerID INTEGER NOT NULL, " +
                    "Issuer TEXT NOT NULL, " +
                    "CardNumber TEXT NOT NULL, " +
                    "CardholderName TEXT NOT NULL, " +
                    "ExpirationDate TEXT NOT NULL, " +
                    "CVCCode TEXT NOT NULL" +
                    ")");
            statement.execute("INSERT INTO CreditCard (OwnerID, Issuer, CardNumber, CardholderName, ExpirationDate, CVCCode) " +
                    "SELECT OwnerID, Issuer, CreditCardNumber, CardholderName, ExpirationDate, CVCCode FROM CreditCard_old");
            statement.execute("DROP TABLE CreditCard_old");
        }
    }

    private static void migratePersonalInfoSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE PersonalInfo RENAME TO PersonalInfo_old");
            statement.execute("CREATE TABLE PersonalInfo (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OwnerID INTEGER NOT NULL, " +
                    "LicenseNumber TEXT, " +
                    "SocialSecurityNumber TEXT, " +
                    "PassportNumber TEXT" +
                    ")");
            statement.execute("INSERT INTO PersonalInfo (OwnerID, LicenseNumber, SocialSecurityNumber, PassportNumber) " +
                    "SELECT OwnerID, LicenseNumber, SocialSecurityNumber, PassportNumber FROM PersonalInfo_old");
            statement.execute("DROP TABLE PersonalInfo_old");
        }
    }

    private static void migrateSecureNoteSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE SecureNote RENAME TO SecureNote_old");
            statement.execute("CREATE TABLE SecureNote (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OwnerID INTEGER NOT NULL, " +
                    "Title TEXT NOT NULL, " +
                    "Contents TEXT NOT NULL" +
                    ")");
            statement.execute("INSERT INTO SecureNote (OwnerID, Title, Contents) SELECT OwnerID, '' as Title, Contents FROM SecureNote_old");
            statement.execute("DROP TABLE SecureNote_old");
        }
    }

    public static void addUserLogin(UserLogin userLogin) {
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT into UserLogin ( Email, Password, SecurityQuestion1, SecurityAnswer1, SecurityQuestion2, SecurityAnswer2, SecurityQuestion3, SecurityAnswer3 ) values ( ?, ?, ?, ?, ?, ?, ?, ? )");

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
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static void updateUserLogin(UserLogin userLogin) {
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update userLogin set Email = ?, Password = ?, SecurityQuestion1 = ?, SecurityAnswer1 = ?, SecurityQuestion2 = ?, SecurityAnswer2 = ?, SecurityQuestion3 = ?, SecurityAnswer3 = ? where ID = ?");

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
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static UserLogin getUserLoginByEmail(String email) {
        UserLogin userLogin = null;

        try {
            Connection connection = getConnection();
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
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        return userLogin;
    }

    public static List<Credentials> getCredentials(int ownerId) {
        List<Credentials> credentials = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Credentials WHERE OwnerID = ?")) {
            preparedStatement.setInt(1, ownerId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                credentials.add(new Credentials(
                        rs.getInt("ID"),
                        rs.getInt("OwnerID"),
                        rs.getString("URL"),
                        rs.getString("Username"),
                        rs.getString("Password")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQLException fetching credentials: " + e.getMessage());
        }
        return credentials;
    }

    public static int addCredentials(Credentials credentials) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Credentials (OwnerID, URL, Username, Password) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, credentials.getOwnerID());
            preparedStatement.setString(2, credentials.getURL());
            preparedStatement.setString(3, credentials.getUsername());
            preparedStatement.setString(4, credentials.getPassword());
            preparedStatement.execute();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQLException adding credentials: " + e.getMessage());
        }
        return -1;
    }

    public static void updateCredentials(Credentials credentials) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Credentials SET URL = ?, Username = ?, Password = ? WHERE ID = ?")) {
            preparedStatement.setString(1, credentials.getURL());
            preparedStatement.setString(2, credentials.getUsername());
            preparedStatement.setString(3, credentials.getPassword());
            preparedStatement.setInt(4, credentials.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException updating credentials: " + e.getMessage());
        }
    }

    public static void deleteCredentials(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Credentials WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException deleting credentials: " + e.getMessage());
        }
    }

    public static List<CreditCard> getCreditCards(int ownerId) {
        List<CreditCard> creditCards = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CreditCard WHERE OwnerID = ?")) {
            preparedStatement.setInt(1, ownerId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                creditCards.add(new CreditCard(
                        rs.getInt("ID"),
                        rs.getInt("OwnerID"),
                        rs.getString("Issuer"),
                        rs.getString("CardNumber"),
                        rs.getString("CardholderName"),
                        rs.getString("ExpirationDate"),
                        rs.getString("CVCCode")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQLException fetching credit cards: " + e.getMessage());
        }
        return creditCards;
    }

    public static int addCreditCard(CreditCard creditCard) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO CreditCard (OwnerID, Issuer, CardNumber, CardholderName, ExpirationDate, CVCCode) VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, creditCard.getOwnerID());
            preparedStatement.setString(2, creditCard.getIssuer());
            preparedStatement.setString(3, creditCard.getCreditCardNumber());
            preparedStatement.setString(4, creditCard.getCardholderName());
            preparedStatement.setString(5, creditCard.getExpirationDate());
            preparedStatement.setString(6, creditCard.getCVCCode());
            preparedStatement.execute();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQLException adding credit card: " + e.getMessage());
        }
        return -1;
    }

    public static void updateCreditCard(CreditCard creditCard) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE CreditCard SET Issuer = ?, CardNumber = ?, CardholderName = ?, ExpirationDate = ?, CVCCode = ? WHERE ID = ?")) {
            preparedStatement.setString(1, creditCard.getIssuer());
            preparedStatement.setString(2, creditCard.getCreditCardNumber());
            preparedStatement.setString(3, creditCard.getCardholderName());
            preparedStatement.setString(4, creditCard.getExpirationDate());
            preparedStatement.setString(5, creditCard.getCVCCode());
            preparedStatement.setInt(6, creditCard.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException updating credit card: " + e.getMessage());
        }
    }

    public static void deleteCreditCard(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM CreditCard WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException deleting credit card: " + e.getMessage());
        }
    }

    public static List<PersonalInfo> getPersonalInfos(int ownerId) {
        List<PersonalInfo> personalInfos = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PersonalInfo WHERE OwnerID = ?")) {
            preparedStatement.setInt(1, ownerId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                personalInfos.add(new PersonalInfo(
                        rs.getInt("ID"),
                        rs.getInt("OwnerID"),
                        rs.getString("LicenseNumber"),
                        rs.getString("SocialSecurityNumber"),
                        rs.getString("PassportNumber")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQLException fetching personal info: " + e.getMessage());
        }
        return personalInfos;
    }

    public static int addPersonalInfo(PersonalInfo personalInfo) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO PersonalInfo (OwnerID, LicenseNumber, SocialSecurityNumber, PassportNumber) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, personalInfo.getOwnerID());
            preparedStatement.setString(2, personalInfo.getLicenseNumber());
            preparedStatement.setString(3, personalInfo.getSocialSecurityNumber());
            preparedStatement.setString(4, personalInfo.getPassportNumber());
            preparedStatement.execute();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQLException adding personal info: " + e.getMessage());
        }
        return -1;
    }

    public static void updatePersonalInfo(PersonalInfo personalInfo) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE PersonalInfo SET LicenseNumber = ?, SocialSecurityNumber = ?, PassportNumber = ? WHERE ID = ?")) {
            preparedStatement.setString(1, personalInfo.getLicenseNumber());
            preparedStatement.setString(2, personalInfo.getSocialSecurityNumber());
            preparedStatement.setString(3, personalInfo.getPassportNumber());
            preparedStatement.setInt(4, personalInfo.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException updating personal info: " + e.getMessage());
        }
    }

    public static void deletePersonalInfo(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM PersonalInfo WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException deleting personal info: " + e.getMessage());
        }
    }

    public static List<SecureNote> getSecureNotes(int ownerId) {
        List<SecureNote> notes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SecureNote WHERE OwnerID = ?")) {
            preparedStatement.setInt(1, ownerId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                notes.add(new SecureNote(
                        rs.getInt("ID"),
                        rs.getInt("OwnerID"),
                        rs.getString("Title"),
                        rs.getString("Contents")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQLException fetching secure notes: " + e.getMessage());
        }
        return notes;
    }

    public static int addSecureNote(SecureNote secureNote) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO SecureNote (OwnerID, Title, Contents) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, secureNote.getOwnerID());
            preparedStatement.setString(2, secureNote.getTitle());
            preparedStatement.setString(3, secureNote.getContents());
            preparedStatement.execute();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQLException adding secure note: " + e.getMessage());
        }
        return -1;
    }

    public static void updateSecureNote(SecureNote secureNote) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE SecureNote SET Title = ?, Contents = ? WHERE ID = ?")) {
            preparedStatement.setString(1, secureNote.getTitle());
            preparedStatement.setString(2, secureNote.getContents());
            preparedStatement.setInt(3, secureNote.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException updating secure note: " + e.getMessage());
        }
    }

    public static void deleteSecureNote(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM SecureNote WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException deleting secure note: " + e.getMessage());
        }
    }
}
