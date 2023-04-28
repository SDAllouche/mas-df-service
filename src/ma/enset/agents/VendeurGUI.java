package ma.enset.agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class VendeurGUI extends Application {
    private VendeurAgent vendeurAgent;
    private ObservableList<Map<String, Object>> data = FXCollections.<Map<String, Object>>observableArrayList();
    public static void main(String[] args) throws Exception {
       launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startConatiner();
        BorderPane root=new BorderPane();
        root.setPadding(new Insets(10,10,10,10));
        GridPane gridPane=new GridPane();
        Label name=new Label("Name");
        Label type=new Label("Type");
        TextField nameText=new TextField();
        TextField typeText=new TextField();
        nameText.setPrefSize(300,40);
        typeText.setPrefSize(300,40);
        Button addButton=new Button("Add");
        addButton.setPrefSize(100,35);
        addButton.setOnAction(actionEvent -> {
            String name1=nameText.getText();
            String type1=typeText.getText();
            if (name1!=null && type1 != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("Name", name1);
                item.put("Type", type1);
                GuiEvent guiEvent=new GuiEvent(this,1);
                guiEvent.addParameter(name1);
                guiEvent.addParameter(type1);
                vendeurAgent.onGuiEvent(guiEvent);
                data.add(item);
                nameText.setText("");
                typeText.setText("");
            }
        });
        gridPane.add(name,0,0);
        gridPane.add(nameText,1,0);
        gridPane.add(type,0,1);
        gridPane.add(typeText,1,1);
        gridPane.add(addButton,1,2);
        gridPane.setPadding(new Insets(20,0,20,0));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        root.setTop(gridPane);
        TableView tableView=new TableView(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Map,String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new MapValueFactory<>("Name"));
        TableColumn<Map,String> column2 = new TableColumn<>("Type");
        column2.setCellValueFactory(new MapValueFactory<>("Type"));
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        root.setCenter(tableView);
        Scene scene=new Scene(root,600,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startConatiner() throws Exception {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer container=runtime.createAgentContainer(profile);
        AgentController agent=container.createNewAgent("Vendeur","ma.enset.tp.agents.VendeurAgent",new Object[]{this});
        agent.start();
    }

    public void setVendeurAgent(VendeurAgent vendeurAgent) {

        this.vendeurAgent = vendeurAgent;
    }
}
