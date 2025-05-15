module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive org.json;
    
    opens org.main.voting to javafx.fxml;
    exports org.main.voting;
}