/**
 * Class responsible for storing a single day's data 
 */
public class Date 
{
    private String date;
    private Integer dailyCases, totalCases, movingCases;
    private Integer dailyDeaths, totalDeaths, movingDeaths;
    
    public void setDate(String date) 
    {
        this.date = date;
    }

    public String getDate() 
    {
        return date;
    }
    
    public void setDailyCases(Integer dailyCases) 
    {
        this.dailyCases = dailyCases;
    }
    
    public Integer getDailyCases() 
    {
        return this.dailyCases;
    }
    
    public void setTotalCases(Integer totalCases)
    {
        this.totalCases = totalCases;
    }

    public Integer getTotalCases()
    {
        return this.totalCases;
    }

    public void setMovingCases(Integer movingCases) 
    {
        this.movingCases = movingCases;
    }
    
    public Integer getMovingCases() 
    {
        return this.movingCases;
    }

    public void setDailyDeaths(Integer dailyDeaths) 
    {
        this.dailyDeaths = dailyDeaths;
    }

    public Integer getDailyDeaths() 
    {
        return this.dailyDeaths;
    }

    public void setTotalDeaths(Integer totalDeaths)
    {
        this.totalDeaths = totalDeaths;
    }
    
    public Integer getTotalDeaths()
    {
        return this.totalDeaths;
    }

    public void setMovingDeaths(Integer movingDeaths) 
    {
        this.movingDeaths = movingDeaths;
    }

    public Integer getMovingDeaths() 
    {
        return this.movingDeaths;
    }
}
