module prisoner.prisonermanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens prisoner.prisonermanager to javafx.fxml;
    exports prisoner.prisonermanager;
}