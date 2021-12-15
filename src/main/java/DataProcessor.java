import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.nio.file.Files;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.nio.file.Paths;

import java.net.URISyntaxException;

/**
 * Class responsible for processing the collected data 
 */
public class DataProcessor 
{
    /**
     * Handles the csv file 
     * @param path The path of the unzipped file
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void formatter(Path path) throws IOException, URISyntaxException
    {
        List<String[]> dados = csvReader(path);
        Files.delete(path);

        List<String> states = new ArrayList<String>();
        String state = "";
        Formatter f = new Formatter();

        for (String[] dado : dados)
        {
            if (!state.equals(dado[1]))
            {
                f.close();
                f = new Formatter(DataPaths.getCustomPath(dado[1] + "-", DataPaths.NULL));
                f.format("%s,%s,%s\n", "Data", "Casos", "Mortes");
                states.add(dado[1]);
            }
            if (dado[3].equals("state")) 
            {
                f.format("%s,%s,%s\n", dado[0], dado[4], dado[5]);
            }
            state = dado[1];
        }
        f.close();
        csvStates(states);
    }

    /**
     * @param path A path of a handled csv file
     * @return A list of Date objects
     * @throws IOException
     */
    public static List<Date> deserializer(Path path) throws IOException
    {
        List<String[]> dados = csvReader(path);
        Collections.reverse(dados); 

        List<Date> dates = new ArrayList<Date>(0);
        Date date;

        int i = 0;
        for (String[] dado : dados)
        {
            date = new Date();
            Integer totalCases = Integer.parseInt(dado[1]);
            Integer totalDeaths = Integer.parseInt(dado[2]);

            // Total
            date.setDate(dado[0]);
            date.setTotalCases(totalCases);
            date.setTotalDeaths(totalDeaths);

            // Daily
            Integer dailyCases;
            Integer dailyDeaths;
            if (i != 0)
            {
                dailyCases = totalCases - dates.get(i - 1).getTotalCases();
                dailyCases = dailyCases < 0 ? 0 : dailyCases;
                dailyDeaths = totalDeaths - dates.get(i - 1).getTotalDeaths();
                dailyDeaths = dailyDeaths < 0 ? 0 : dailyDeaths;
            }   
            else
            {
                dailyCases = totalCases;
                dailyDeaths = totalDeaths;
            }
            date.setDailyCases(dailyCases);
            date.setDailyDeaths(dailyDeaths);

            // Moving average
            if (i < 6)
            {
                date.setMovingCases(dailyCases);
                date.setMovingDeaths(dailyDeaths);
            }
            else
            {
                Integer sumCases = dailyCases;
                Integer sumDeaths = dailyDeaths;
                for (int j = 1; j < 7; j++)
                {
                    sumCases += dates.get(i - j).getDailyCases();
                    sumDeaths += dates.get(i - j).getDailyDeaths();
                }
                date.setMovingCases((int) Math.round((double)sumCases / 7));
                date.setMovingDeaths((int) Math.round((double)sumDeaths / 7));
            }
            dates.add(date);
            i++;
        }
        return dates;
    }

    /**
     * Stores the csv file in a List<String[]> variable
     * @param path A path of a csv file
     * @return A list of file lines
     * @throws IOException
     */
    public static List<String[]> csvReader(Path path) throws IOException
    {
        Reader reader = Files.newBufferedReader(path);
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        return csvReader.readAll();
    }

    /**
     * Stores the states acronyms in a csv file
     * @param list A list with the acronyms of the states
     * @throws FileNotFoundException
     */
    public static void csvStates(List<String> list) throws FileNotFoundException
    {
        if (!Files.exists(Paths.get("siglas.csv")))
        {
            Formatter f = new Formatter("siglas.csv");
            Collections.sort(list);
            f.format("\n");
            int i = 0;
            for (i = 0; i < list.size() - 1; i++)
            {
                f.format("%s,", list.get(i));
            }
            f.format("%s", list.get(i));
            f.close();
        }
    }
}