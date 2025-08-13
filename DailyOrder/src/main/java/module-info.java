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
    exports com.topglobal.dailyorder.controllers.leader;
    opens com.topglobal.dailyorder.controllers.leader to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.admin;
    opens com.topglobal.dailyorder.controllers.admin to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.admin.menu;
    opens com.topglobal.dailyorder.controllers.admin.menu to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.admin.orders;
    opens com.topglobal.dailyorder.controllers.admin.orders to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.leader.orders to javafx.fxml;
    opens com.topglobal.dailyorder.controllers.leader.orders to javafx.fxml;
    exports com.topglobal.dailyorder.controllers.waiter to javafx.fxml;
    opens com.topglobal.dailyorder.controllers.waiter to javafx.fxml;
    opens com.topglobal.dailyorder.controllers.leader.plan to javafx.fxml;
    opens com.topglobal.dailyorder.views.leader.plan to javafx.fxml;

}