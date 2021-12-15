import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

/**
 * Class responsible for controlling the GUI
 */
public class ScreenController implements Initializable
{
    @FXML
    private LineChart<String, Number> casesLineChart;
   
    @FXML
    private LineChart<String, Number> deathsLineChart;
    
    @FXML
    private NumberAxis casesLineAxis;

    @FXML
    private NumberAxis deathsLineAxis;
    
    @FXML
    private BarChart<String, Number> casesBarChart;

    @FXML
    private BarChart<String, Number> deathsBarChart;

    @FXML
    private NumberAxis casesBarAxis;

    @FXML
    private NumberAxis deathsBarAxis;

    @FXML
    private Button updateButton;

    @FXML
    private ComboBox<String> localBox;

    @FXML
    private Label messageLabel;

    @FXML
    private Label casesLabel;

    @FXML
    private Label deathsLabel;

    @FXML
    private TabPane tabPane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            localBox.setItems(FXCollections.observableArrayList(Arrays.asList(DataProcessor.csvReader(Paths.get("siglas.csv")).get(0))));
        }
        catch (NoSuchFileException e)
        {
            localBox.setDisable(true);
            return;
        }
        catch (IOException e)
        {
            localBox.setDisable(true);
            return;
        }
        localBox.setDisable(false);
    }
    
    /**
     * Updates the data
     * @param event when the update button is clicked
     * @throws InterruptedException
     */
    @FXML
    void update(ActionEvent event) throws InterruptedException
    {
        try
        {
            DataCollector.dataCollector();
        }
        catch (IOException e)
        {
            messageLabel.setText(Status.getText(Status.ERROR, Status.INTERNET.getStatus()));
            return;
        }
        catch (URISyntaxException e)
        {
            messageLabel.setText(Status.getText(Status.ERROR, "ao baixar os dados."));
            return;
        }

        try
        {
            localBox.setItems(FXCollections.observableArrayList(Arrays.asList(DataProcessor.csvReader(Paths.get("siglas.csv")).get(0))));
        }
        catch (IOException e)
        {
            messageLabel.setText(Status.getText(Status.ERROR, DataPaths.NULL));
            return; 
        }

        localBox.setDisable(false);
        messageLabel.setText(Status.getText(Status.SUCCSESS, DataPaths.NULL));
    }
    
    /**
     * Shows the charts of the selected state  
     * @param event when the ComboBox is clicked
     */
    @FXML
    void showInformation(ActionEvent event) 
    {
        List<Date> dates;
        try 
        {
            dates = DataProcessor.deserializer(Paths.get(DataPaths.getCustomPath(localBox.getValue() + "-", DataPaths.NULL)));
        }
        catch (NoSuchFileException e)
        {
            messageLabel.setText(Status.getText(Status.FILE, DataPaths.NULL));
            return;
        }
        catch (IOException e)
        {
            messageLabel.setText(Status.getText(Status.ERROR, "ao abrir o arquivo."));
            return;
        }

        messageLabel.setText("");
        casesLineChart.getData().clear();
        deathsLineChart.getData().clear();
        casesBarChart.getData().clear();
        deathsBarChart.getData().clear();
        
        XYChart.Series<String, Number> lineCases = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> lineDeaths = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> barCases = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> barDeaths = new XYChart.Series<String, Number>();
        
        casesLabel.setText("Casos totais: " + dates.get(dates.size() - 1).getTotalCases());
        deathsLabel.setText("Mortes totais: " + dates.get(dates.size() - 1).getTotalDeaths());

        int upperBoundCases = 0, 
            upperBoundDeaths = 0;
            
        for (Date date: dates)
        {
            lineCases.getData().add(new XYChart.Data<String, Number>(date.getDate(), (Number) date.getMovingCases()));
            lineDeaths.getData().add(new XYChart.Data<String, Number>(date.getDate(), (Number) date.getMovingDeaths())); 
            barCases.getData().add(new XYChart.Data<String, Number>(date.getDate(), (Number) date.getDailyCases()));
            barDeaths.getData().add(new XYChart.Data<String, Number>(date.getDate(), (Number) date.getDailyDeaths()));
            
            upperBoundCases = Math.max(date.getDailyCases(), upperBoundCases);
            upperBoundDeaths = Math.max(date.getDailyDeaths(), upperBoundDeaths);
        }
        upperBoundCases += upperBoundCases/100;
        upperBoundDeaths += upperBoundDeaths/100;

        casesLineAxis.setUpperBound(upperBoundCases);
        deathsLineAxis.setUpperBound(upperBoundDeaths);
        casesBarAxis.setUpperBound(upperBoundCases);
        deathsBarAxis.setUpperBound(upperBoundDeaths);

        casesLineAxis.setTickUnit(upperBoundCases/5);
        deathsLineAxis.setTickUnit(upperBoundDeaths/5);
        casesBarAxis.setTickUnit(upperBoundCases/5);
        deathsBarAxis.setTickUnit(upperBoundDeaths/5);

        casesLineChart.getData().add(lineCases);
        deathsLineChart.getData().add(lineDeaths);
        casesBarChart.getData().add(barCases);
        deathsBarChart.getData().add(barDeaths);
        
        tabPane.setDisable(false);
    }
}