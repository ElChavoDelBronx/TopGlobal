module com.topglobal.dailyorder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.topglobal.dailyorder to javafx.fxml;
    exports com.topglobal.dailyorder;
    exports com.topglobal.dailyorder.controllers.login;
    opens com.topglobal.dailyorder.controllers.login to javafx.fxml;
}