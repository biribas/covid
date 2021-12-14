import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Scene;

public class Main extends Application
{
    private Stage mainStage;
    private AnchorPane root;

    @Override
    public void start(Stage stage) throws Exception
    {
        this.mainStage = stage;
        this.mainStage.setTitle("Covid-19 no Brasil");
        
        initRootLayout();
    }

    /**
     * Sets up the GUI
     */
    public void initRootLayout()
    {
        try
        {
            // Load root layout from fxml file.
            this.root = (AnchorPane) FXMLLoader.load(this.getClass().getResource("GUI.fxml"));
            
            // Show the scene containing the root layout.
            this.mainStage.setScene(new Scene(this.root));
            this.mainStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        launch(args);
    }
}