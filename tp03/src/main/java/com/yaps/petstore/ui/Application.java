package com.yaps.petstore.ui;

import com.yaps.petstore.exceptions.DuplicateKeyException;
import com.yaps.petstore.exceptions.NotFoundException;
import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.model.Customer;
import com.yaps.petstore.model.CustomerDAO;
import com.yaps.petstore.ui.Menu.MenuEntry;

/**
 * This text menu is used by the employee to manage Customer information.
 * It can find, create, remove and update Customers.
 */
public final class Application {

    private final CustomerDAO dao = new CustomerDAO();

    private Menu mainMenu;
    private Menu yesNoMenu;
    private SimpleIO simpleIO;


    // ======================================
    // = Main Method =
    // ======================================
    public static void main(final String[] args) {
        Application application = new Application();
        application.runApp();
    }

    public Application() {
        simpleIO = new SimpleIO();
        mainMenu = buildMainMenu();
        yesNoMenu = buildYesNoMenu();
    }

    private Menu buildMainMenu() {
        MenuEntry[] entries = {
                new MenuEntry("c", "Create Customer"),
                new MenuEntry("f", "Find Customer"),
                new MenuEntry("d", "Delete Customer"),
                new MenuEntry("u", "Update Customer"),
                new MenuEntry("q", "Quit"),
        };
        return new Menu(entries, simpleIO);
    }

    private Menu buildYesNoMenu() {
        return new Menu(new MenuEntry[] {
                new MenuEntry("y", "yes"),
                new MenuEntry("n", "no")
        },
                simpleIO);
    }

    // ======================================
    // = Presentation Methods =
    // ======================================
    private void runApp() {

        try {
            String menuChoice;
            boolean quitNow = false;
            do {
                // Note : the Menu could be improved to allow to headers, line skips, etc...
                // but we stay simple now.
                simpleIO.printf("\n\t------------------  Y A P S  -----------------\n");
                simpleIO.printf("\t------------------ Pet Store -----------------\n\n");
                menuChoice = mainMenu.choose();
                switch (menuChoice) {
                    case "c":
                        createCustomer();
                        break;
                    case "f":
                        findCustomer();
                        break;
                    case "d":
                        deleteCustomer();
                        break;
                    case "u":
                        updateCustomer();
                        break;
                    case "q":
                        quitNow = true;
                        break;
                    default:
                        throw new Error("bug in the menu. Should not happen.");
                }
            } while (!quitNow);
        } catch (Exception e) {
            showMessage("\n\tMenu Exception : \n\t" + e.getMessage());
        }
    }

    private void createCustomer() {
        String id = askForString("Customer id: ");
        String firstname = askForString("First name: ");
        String lastname = askForString("Last name: ");
        String telephone = askForString("Phone number: ");
        String street1 = askForString("Street (1): ");
        String street2 = askForString("Street (2): ");
        String city = askForString("City: ");
        String state = askForString("State: ");
        String zipCode = askForString("Zip code: ");
        String country = askForString("Country :");
        try {
            final Customer customer = new Customer(id, firstname, lastname);
            customer.setTelephone(telephone);
            customer.setStreet1(street1);
            customer.setStreet2(street2);
            customer.setCity(city);
            customer.setState(state);
            customer.setZipcode(zipCode);
            customer.setCountry(country);
            dao.insert(customer);
            showMessage("\n\tInfo : Customer created.\n\n");
            showMessage("" + customer + "\n\n");
            waitEnter();
        } catch (DuplicateKeyException e) {
            showMessage("\n\tWarning : This Customer already exists !");
        } catch (Exception e) {
            showMessage("\n\tError : Cannot create this Customer !!!\n\t" + e.getMessage());
        }

    }

    private void findCustomer() {
        showMessage("\n\n\t---   Find a Customer   ---");
        String id = askForString("Customer id: ");
        try {
            // Calls the method that retrieves all the data of the object
            final Customer customer = dao.find(id);
            showMessage("\t" + customer);
            waitEnter();
        } catch (NotFoundException e) {
            showMessage("\n\tWarning : This Customer doesn't exist !");
        } catch (Exception e) {
            showMessage("\n\tError : Cannot find this Customer !!! \n\t" + e.getMessage());
        }
    }

    private void deleteCustomer() {
        showMessage("\n\n\t---   Delete a Customer   ---");
        String id = askForString("Customer id: ");

        try {
            // Calls the method that retreives all the data of the object
            final Customer customer = dao.find(id);
            showMessage("\n" + customer + '\n');

            showMessage("\tDo you want to remove this Customer (y/n) : ");
            String yesOrNo = yesNoMenu.choose();

            switch (yesOrNo) {
                case "y":
                    dao.remove(customer.getId());
                    showMessage("Customer deleted");
                    waitEnter();
                    break;
                case "n":
                    showMessage("Customer not deleted");
                    waitEnter();
                    break;
                default:
                    throw new Error("should not happen");
            }
            waitEnter();
        } catch (NotFoundException e) {
            showMessage("\n\tWarning : This Customer doesn't exist !");
        } catch (Exception e) {
            showMessage("\n\tError : Cannot find this Customer !!! \n\t" + e.getMessage());
        }
    }

    private void updateCustomer() {

        showMessage("\n\n\t---   Update a Customer   ---");
        String id = askForString("Customer id: ");

        try {
            // We work on a copy of the original customer...
            Customer updatedCustomer = new Customer(dao.find(id));
            Menu updateMenu = buildUpdateMenu();

            String choice;
            do {
                showMessage("\n");
                showMessage("Customer to edit\n" + updatedCustomer);
                showMessage("\n");
                choice = updateMenu.choose();
                // Backup of the current customer value, in case the choice
                // leads to an error.
                Customer savedCustomer = new Customer(updatedCustomer);
                try {
                    switch (choice) {
                        case "f": {
                            String newName = askForString("Updated first name : ");
                            updatedCustomer.setFirstname(newName);
                            break;
                        }
                        case "l": {
                            String newName = askForString("Updated last name : ");
                            updatedCustomer.setLastname(newName);
                            break;
                        }
                        case "t": {
                            String newTelephone = askForString("Updated telephone : ");
                            updatedCustomer.setTelephone(newTelephone);
                            break;
                        }
                        case "s1": {
                            String newStreet = askForString("Updated street 1 : ");
                            updatedCustomer.setStreet1(newStreet);
                            break;
                        }
                        case "s2": {
                            String newStreet = askForString("Updated street 2 : ");
                            updatedCustomer.setStreet2(newStreet);
                            break;
                        }
                        case "c": {
                            String city = askForString("Updated city: ");
                            updatedCustomer.setCity(city);
                            break;
                        }
                        case "s3": {
                            String state = askForString("Updated State: ");
                            updatedCustomer.setState(state);
                            break;
                        }
                        case "z": {
                            String zipcode = askForString("Updated zip code: ");
                            updatedCustomer.setZipcode(zipcode);
                            break;
                        }
                        case "co": {
                            String country = askForString("Updated country: ");
                            updatedCustomer.setCountry(country);
                            break;
                        }
                        case "done": {
                            // Nothing.
                            break;
                        }
                        default:
                            throw new Error("should not happen");
                    }
                    updatedCustomer.checkData();
                    showMessage("updated customer is " + updatedCustomer);
                    waitEnter();
                } catch (ValidationException e) {
                    showMessage("invalid choice : " + e.getMessage());
                    updatedCustomer = savedCustomer;
                }
            } while (!choice.equals("done"));
            showMessage("Do you want to validate the changes :");
            String yesNo = yesNoMenu.choose();
            try {
                if ("y".equals(yesNo)) {
                    dao.update(updatedCustomer);
                }
            } catch (ValidationException e) {
                // Should not happen (as there are currently only exception
                // because of individual fields, and those have been managed earlier)
                showMessage("Update failed : " + e.getMessage());
            }
        } catch (NotFoundException e) {
            showMessage("Customer not found for id :" + id);
        }

    }

    private Menu buildUpdateMenu() {
        MenuEntry[] entries = {
                new MenuEntry("done", "done updating"),
                new MenuEntry("f", "First name"),
                new MenuEntry("l", "Last name"),
                new MenuEntry("t", "telephone"),
                new MenuEntry("s1", "street 1"),
                new MenuEntry("s2", "street 2"),
                new MenuEntry("c", "city"),
                new MenuEntry("s3", "state"),
                new MenuEntry("z", "zipcode"),
                new MenuEntry("co", "country"),
        };
        return new Menu(entries, simpleIO);
    }

    /**
     * Display a message on a line
     * 
     * @param message
     */
    private void showMessage(String message) {
        simpleIO.printf("%s\n", message);
    }

    /**
     * Asks for a given answer (which can be empty)
     * 
     * @param message : message to display
     * @return
     */
    private String askForString(String message) {
        simpleIO.printf(message);
        simpleIO.flush();
        return simpleIO.readLine().trim();
    }

    private void waitEnter() {
        showMessage("\n\tPress enter to continue...");
        simpleIO.readLine();
    }

}
