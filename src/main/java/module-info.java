module prisoner.prisonermanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.net.http;
    requires java.desktop;
    requires com.google.gson;
    requires org.commonmark;
    requires javafx.web;

    opens prisoner.prisonermanager to javafx.fxml;
    exports prisoner.prisonermanager;
}