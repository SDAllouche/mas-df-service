package ma.enset.tp.agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ClientGUI extends Application {
     private ClientAgent clientAgent;
    private ObservableList<Map<String,Object>> data = FXCollections.<Map<String, Object>>observableArrayList();
    public static void main(String[] args) throws Exception {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startCotainer();
        BorderPane root=new BorderPane();
        root.setPadding(new Insets(10,10,10,10));
        GridPane gridPane=new GridPane();
        gridPane.setPadding(new Insets(10,0,10,0));
        TextField searchText=new TextField();
        searchText.setPrefSize(350,40);
        Button searchButton=new Button("Search");
        searchButton.setPrefSize(100,35);
        gridPane.add(searchText,0,0);
        gridPane.add(searchButton,1,0);
        gridPane.setHgap(20);
        root.setTop(gridPane);
        TableView tableView=new TableView(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Map,String> column1 = new TableColumn<>("Agent");
        column1.setCellValueFactory(new MapValueFactory<>("Agent"));
        TableColumn<Map,String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new MapValueFactory<>("Name"));
        TableColumn<Map,String> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new MapValueFactory<>("Type"));
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        root.setCenter(tableView);
        Button buyButton=new Button("Buy");
        buyButton.setPrefSize(100,35);
        TextArea reponse=new TextArea();
        reponse.setPrefSize(450,150);
        GridPane gridPane2=new GridPane();
        gridPane2.add(reponse,0,0);
        gridPane2.add(buyButton,1,0);
        gridPane2.setHgap(20);
        gridPane2.setPadding(new Insets(10,0,0,0));
        root.setBottom(gridPane2);
        Scene scene=new Scene(root,600,550);
        primaryStage.setScene(scene);
        primaryStage.show();
        searchButton.setOnAction(event -> {
            String word=searchText.getText();
            if (word != null) {
                data.clear();
                GuiEvent guiEvent=new GuiEvent(this,1);
                guiEvent.addParameter(word);
                clientAgent.onGuiEvent(guiEvent);
            }
        });
        tableView.setOnMouseClicked(event->{
            if (event.getClickCount() == 2 ) {
                reponse.setText("");
                Map<String, Object> item= (Map<String, Object>) tableView.getSelectionModel().getSelectedItem();
                String str="You selected the product :\n" +
                        "   -Name : "+item.get("Name")+
                        "\n   -Type : "+item.get("Type");
                reponse.setText(str);
            }
        });
        buyButton.setOnAction(event -> {
            String check=reponse.getText();
            if (check.contains("selected")) {
                reponse.setText(check+"\nYou purchased the product!!");
            }
        });
    }

    private void startCotainer() throws Exception {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer container=runtime.createAgentContainer(profile);
        AgentController agentClient1=container.createNewAgent("client2","ma.enset.tp.agents.ClientAgent",new Object[]{this});
        agentClient1.start();
    }

    public void setClientAgent(ClientAgent clientAgent) {
        this.clientAgent = clientAgent;
    }

    public void setService(Map<String,Object> map){
        data.add(map);
    }
}
