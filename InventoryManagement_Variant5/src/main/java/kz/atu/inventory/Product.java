package kz.atu.inventory;
import javafx.beans.property.*;
public class Product {
 private final IntegerProperty id=new SimpleIntegerProperty(),quantity=new SimpleIntegerProperty();
 private final StringProperty name=new SimpleStringProperty(),category=new SimpleStringProperty();
 private final DoubleProperty price=new SimpleDoubleProperty();
 public Product(int id,String name,String category,int quantity,double price){this.id.set(id);this.name.set(name);this.category.set(category);this.quantity.set(quantity);this.price.set(price);}
 public int getId(){return id.get();} public String getName(){return name.get();} public String getCategory(){return category.get();} public int getQuantity(){return quantity.get();} public double getPrice(){return price.get();}
 public void setQuantity(int v){quantity.set(v);} public IntegerProperty idProperty(){return id;} public StringProperty nameProperty(){return name;} public StringProperty categoryProperty(){return category;} public IntegerProperty quantityProperty(){return quantity;} public DoubleProperty priceProperty(){return price;}
}