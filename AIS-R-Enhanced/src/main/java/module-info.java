module ais {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    
    opens ais to javafx.fxml;
    exports ais;
    
  
}
