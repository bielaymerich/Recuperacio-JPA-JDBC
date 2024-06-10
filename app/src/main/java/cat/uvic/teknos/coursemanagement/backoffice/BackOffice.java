package cat.uvic.teknos.coursemanagement.backoffice;

import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

import static cat.uvic.teknos.coursemanagement.backoffice.IOUtils.*;

public class BackOffice {
    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;


    public BackOffice(InputStream inputStream, OutputStream outputStream, RepositoryFactory repositoryFactory, ModelFactory modelFactory)  {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.out = new PrintStream(outputStream);
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    public void start() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException  {
        showBanner();
        showWelcomeMessage();

        var command = "";
        do {
            showMainMenu();
            command = readLine(in);

            switch (command) {
                case "1":
                    manageStudents();
                    break;
                case "2":
                    manageCourse();
                    break;
                case "3":
                    manageGenere();
                    break;
                case "4":
                    manageAddress();
                    break;
                default: if(!command.equalsIgnoreCase("exit"))
                    out.println("Invalid command");
            }
            break;
        } while (!command.equals("exit"));

        out.println("Bye!");
    }

    private void manageCourse() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new CourseManager(in,out, repositoryFactory, modelFactory).start();}
    private void manageAddress() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new AddressManager(in,out, repositoryFactory, modelFactory).start();}
    private void manageGenere() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new GenreManager(in,out, repositoryFactory, modelFactory).start();}
    private void manageStudents() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new StudentManager(in, out, repositoryFactory, modelFactory).start();
    }


    private void showWelcomeMessage() {
        out.println("Welcome to the Course Management Back Office");
        out.println("Select a menu option or type exit to exit the application");
    }

    private void showMainMenu() {
        out.println("1. Student");
        out.println("2. Course");
        out.println("3. Genre");
        out.println("4. Address");
    }

    private void showBanner() {
        var bannerStream = BackOffice.class.getResourceAsStream("/banner.txt");

        var banner = new BufferedReader(new InputStreamReader(bannerStream))
                .lines().collect(Collectors.joining("\n"));

        out.println(banner);
    }

}