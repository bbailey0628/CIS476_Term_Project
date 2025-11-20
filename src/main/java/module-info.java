module com.example.cis476termproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cis476termproject to javafx.fxml;
    exports com.example.cis476termproject;
}