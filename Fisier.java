import java.io.*;
public class Fisier implements TipFisier{
    private final String nume;
    private Folder parinte;
    public Fisier(String nume)
    {
        parinte = null;
        this.nume = nume;
    }
    public void setParinte(Folder parinte)
    {
        this.parinte = parinte;
    }
    public Folder getParinte()
    {
        return parinte;
    }
    public String getNume()
    {
        return nume;
    }
    public void lsR(PrintStream output, PrintStream errors, SistemDeFisiere fileSystem) {}
    public void pwd(PrintStream output){}
    public void ls(PrintStream output, PrintStream errors, SistemDeFisiere fileSystem){}
}
