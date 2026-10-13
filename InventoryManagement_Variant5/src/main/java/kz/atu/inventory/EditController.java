package kz.atu.inventory;
import javafx.fxml.FXML;import javafx.scene.control.*;import javafx.stage.Stage;
public class EditController {
 @FXML private TextField idField,nameField,categoryField,quantityField,priceField;
 private Product result;
 public void setup(Product product){if(product!=null){idField.setText(""+product.getId());idField.setDisable(true);nameField.setText(product.getName());categoryField.setText(product.getCategory());quantityField.setText(""+product.getQuantity());priceField.setText(""+product.getPrice());}}
 @FXML private void save(){try{int id=Integer.parseInt(idField.getText().trim());String n=nameField.getText().trim(),c=categoryField.getText().trim();int q=Integer.parseInt(quantityField.getText().trim());double price=Double.parseDouble(priceField.getText().trim().replace(',','.'));if(id<=0||n.isBlank()||c.isBlank()||q<0||!Double.isFinite(price)||price<0)throw new IllegalArgumentException();result=new Product(id,n,c,q,price);close();}catch(Exception e){new Alert(Alert.AlertType.ERROR,"Проверьте поля: ID > 0, количество >= 0, цена >= 0, название и категория обязательны.",ButtonType.OK).showAndWait();}}
 @FXML private void cancel(){close();} private void close(){((Stage)idField.getScene().getWindow()).close();} public Product getResult(){return result;}
}