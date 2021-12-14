/**
 * Enumerator that handles error messages  
 */
public enum Status 
{
    ERROR("Algo deu errado"),
    INTERNET(". Verifique sua conexão com a internet."),
    SUCCSESS("Dados baixados com sucesso"),
    FILE("Arquivo não encontrado. Atualize os dados");

    private String status;

    Status(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return this.status;
    }

    /**
     * Returns a complete error message
     * @param status the desired status 
     * @param text sufix to the error message
     * @return A error message
     */
    public static String getText(Status status, String text)
    {
        return status.getStatus() + text;
    }
}
