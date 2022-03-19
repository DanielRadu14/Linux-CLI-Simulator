import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException{

        File file = new File(args[0]);
        Scanner input = new Scanner(file);
        FileOutputStream o = new FileOutputStream(args[1]);
        PrintStream output = new PrintStream(o);
        FileOutputStream e = new FileOutputStream(args[2]);
        PrintStream errors = new PrintStream(e);

        SistemDeFisiere fileSystem = new SistemDeFisiere();       
        
        CommandFactory factory = CommandFactory.getInstance();
        CommandControl control = CommandControl.getInstance();
        
        
        while(input.hasNext())
        {
            String comanda = input.next();
            fileSystem.getNrComanda(output, errors);
            control.setCommand(factory.createCommand(comanda, input, output, errors, fileSystem));            
            control.execute();
        }
    }
    
}
