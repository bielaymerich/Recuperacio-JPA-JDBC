package cat.uvic.teknos.coursemanagement.backoffice;

import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.models.Address;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import java.util.*;

import static cat.uvic.teknos.coursemanagement.backoffice.IOUtils.*;

public class AddressManager {
    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final Properties properties = new Properties();
    private final ModelFactory modelFactory;

    public AddressManager(BufferedReader in, PrintStream out, RepositoryFactory repositoryFactory, ModelFactory modelFactory) throws IOException {
        this.in = in;
        this.out = out;

        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        properties.load(App.class.getResourceAsStream("/app.properties"));
    }

    public void start() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        out.println("Addresses:");
        out.println("Type:");
        out.println("1 to insert a new address");
        out.println("2 to modify a address data");
        out.println("3 to show a address details");
        out.println("4 to show all the address");
        out.println("5 to delete a address data");
        out.println("exit to exit");

        var command = "";
        do {
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> get();
                case "4" -> getAll();
                case "5" -> delete();
                default -> { if(!command.equalsIgnoreCase("exit"))out.println("Invalid command");
                }}

        } while (!command.equalsIgnoreCase("exit"));

        out.println("Bye!");
    }
    private void delete(){
        var addressRepository = repositoryFactory.getAddressRepository();
        var address = modelFactory.createAddress();
        out.println("Please enter the id of the address you would like to delete");
        int id = Integer.parseInt(readLine(in));
        address.setId(id);
        addressRepository.delete(address);
    }
    private void get() {
        out.println("Please enter the id of the address you would like to search");
        int id = Integer.parseInt(readLine(in));
        var addressRepository = repositoryFactory.getAddressRepository();
        var address = addressRepository.get(id);
        String[] headers = {"ID","STREET","ZIP CODE"};
        String[][] addressT = {{String.valueOf(address.getId()),address.getStreet(),address.getZip()}};
        if(address != null) {
            System.out.println(AsciiTable.getTable(headers,addressT));
        }


    }

    private void insert() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException{
        var address = modelFactory.createAddress();
        var addressRepository = repositoryFactory.getAddressRepository();

        System.out.println("Please enter the street of the address");
        String street = readLine(in);
        address.setStreet(street);
        System.out.println("Please enter the zip code of the address");
        String zipCode = readLine(in);
        address.setZip(zipCode);

        addressRepository.save(address);
        out.println("Address inserted successfully");
    }

    private void update() {
        var address = modelFactory.createAddress();
        var addressRepository = repositoryFactory.getAddressRepository();
        Set<Address> addresses = addressRepository.getAll();
        for (var ad : addresses) {
            System.out.println("ID:" + ad.getId() + "     " + "STREET: " + ad.getStreet() + " ZIP CODE: " + ad.getZip());
        }
        System.out.println("Please enter the id of the address you want to update");
        int id = Integer.parseInt(readLine(in));
        address.setId(id);
        System.out.println("Please enter the street of the address");
        String street = readLine(in);
        address.setStreet(street);
        System.out.println("Please enter the zip code of the address");
        String zipCode = readLine(in);
        address.setZip(zipCode);

        addressRepository.save(address);
        out.println("Address inserted successfully");
    }

    private void getAll() {
        out.println("These are all the addresses: ");
        var addressRepository = repositoryFactory.getAddressRepository();
        Set<Address> addresses = addressRepository.getAll();
        String[] headers = {"ID","STREET","ZIP CODE"};
        String[] [] addressesList = new String[addresses.toArray().length][headers.length];
        var index = 0;
        for (var ad : addresses) {
            String[] addressT = {String.valueOf(ad.getId()),ad.getStreet(),ad.getZip()};
            addressesList[index] = addressT;
            index ++;
        }
        System.out.println(AsciiTable.getTable(headers,addressesList));
    }
}