# hermesComercial
Configure Run VM Options: To run the application, you need to specify the module path and add the required modules in your run configuration:
* Go to Run | Edit Configurations.
* In the VM options field, add the following (adjust the path to your SDK):
bash
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base
