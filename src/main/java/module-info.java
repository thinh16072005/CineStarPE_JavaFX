module org.de190006.cinestar.cinestarpe_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.de190006.cinestar.cinestarpe_javafx to javafx.fxml;
    exports org.de190006.cinestar.cinestarpe_javafx;
}