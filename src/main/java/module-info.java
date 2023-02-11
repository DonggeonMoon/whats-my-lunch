module com.mdg.whatsmylunch {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;

    opens com.mdg.whatsmylunch to javafx.fxml;
    exports com.mdg.whatsmylunch;
    exports com.mdg.whatsmylunch.domain;
    opens com.mdg.whatsmylunch.domain to javafx.fxml;
    exports com.mdg.whatsmylunch.util;
    opens com.mdg.whatsmylunch.util to javafx.fxml;
}