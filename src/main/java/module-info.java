module org.ruru.ffta2editor {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.desktop;
    requires java.logging;

    opens org.ruru.ffta2editor to javafx.fxml;
    exports org.ruru.ffta2editor;
    exports org.ruru.ffta2editor.utility;
}
