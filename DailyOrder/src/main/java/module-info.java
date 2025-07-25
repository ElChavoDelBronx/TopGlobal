module com.topglobal.dailyorder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens com.topglobal.dailyorder to javafx.fxml;
    exports com.topglobal.dailyorder;
    exports com.topglobal.dailyorder.controllers.login;
    opens com.topglobal.dailyorder.controllers.login to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.waiter;
    opens com.topglobal.dailyorder.controllers.waiter to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.leader;
    opens com.topglobal.dailyorder.controllers.leader to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.admin;
    opens com.topglobal.dailyorder.controllers.admin to javafx.fxml;
}