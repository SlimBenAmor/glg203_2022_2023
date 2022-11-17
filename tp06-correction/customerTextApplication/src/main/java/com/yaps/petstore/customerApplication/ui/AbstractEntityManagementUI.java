package com.yaps.petstore.customerApplication.ui;

import java.util.List;
import java.util.Optional;

import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.common.model.DomainObject;
import com.yaps.common.service.EntityService;
import com.yaps.common.ui.Menu;
import com.yaps.common.ui.SimpleIOInterface;

/**
 * Template for Category/Product/Item management ui. The code for
 * editing/listing/etc... each kind of DomainObject is pretty repetitive.
 * Instead of copy/pasting and editing, we tried to abstract this code using the
 * <b>Template Method</b> pattern.
 */
public abstract class AbstractEntityManagementUI<E extends DomainObject> {

	protected SimpleIOInterface io;

	private String entityName;

	/**
	 * A Y/N Menu. Should move to a separate class for it is used in multiple
	 * places.
	 */
	protected Menu yesNoMenu;

	/**
	 * Initialise the AbstractEntityManagementUI
	 * 
	 * @param io         the input/output used by the software
	 * @param entityName the name of the kind of DomainObject to edit (e.g.
	 *                   "product").
	 */
	protected AbstractEntityManagementUI(SimpleIOInterface io, String entityName) {
		this.io = io;
		this.entityName = entityName;
		this.yesNoMenu = new Menu(io).add("y", "yes").add("n", "no");
	}

	/**
	 * Get the service used by this ui.
	 * Note that it's an abstract method, to allow implementations to use more specific classes than a generic EntityService.
	 * @return
	 */
	protected abstract EntityService<E> getService();

	/**
	 * Returns the kind of domain objects edited by this ui element.
	 * <p>
	 * Used in text display, dialogs, etc.
	 * 
	 * @return
	 */
	private String getEntityName() {
		return entityName;
	}

	/**
	 * This menu is dynamically created depending on the application state.
	 * 
	 * @return
	 */
	private Menu buildManagementMenu() {
		Menu manageMenu = new Menu(io).setWelcomeText("Please choose one action").add("l", "list entries");
		if (getService().isNotEmpty()) {
			manageMenu.add("v", "view entry");
		}
		if (canCreate()) {
			manageMenu.add("c", "create entry");
		}
		if (getService().isNotEmpty()) {
			manageMenu.add("e", "edit entry").add("d", "delete entry");
		}
		manageMenu.add("q", "quit");
		return manageMenu;
	}

	public void run() {

		String choice;
		do {
			choice = buildManagementMenu().choose();
			switch (choice) {
			case "l":
				listEntities();
				break;
			case "v":
				viewEntity();
				break;
			case "c":
				createEntity();
				break;
			case "e":
				editEntity();
				break;
			case "d":
				deleteEntity();
				break;
			}
		} while (!"q".equals(choice));
	}

	/**
	 * Is it possible to create an entity of this kind. Checks pre-conditions.
	 * 
	 * @return
	 */
	protected boolean canCreate() {
		return true;
	}

	private void deleteEntity() {
		io.println();
		final String id = io.askForString("please enter the id of the " + entityName + " to delete");
		Optional<E> found = getService().findById(id);
		if (found.isEmpty()) {
			io.printf("Sorry, nothing available for id %s%n", id);
		} else {
			E cat = found.get();
			io.println("found:");
			io.println(cat.toString());
			String yesNo = yesNoMenu.setWelcomeText("Are you sure you want to delete this entry").choose();
			if ("y".equals(yesNo)) {
				try {
					getService().remove(id);
					io.println("element deleted");
				} catch (ObjectNotFoundException e) {
					// Should not happen, we have just checked.
					// or someone has deleted it meanwhile ?
					io.println("The element seems to have been already deleted!");
				}
			}
		}

		io.waitEnter();
	}

	private void editEntity() {
		io.println();
		String id = io.askForString("Id of %s to edit".formatted(getEntityName()));
		getService().findById(id).ifPresentOrElse(entity -> {
			Menu editMenu = createEditMenu().add("q", "quit");
			String answer;
			do {
				io.println("Current value of %s".formatted(getEntityName()));
				io.println(entity.toString());
				answer = editMenu.choose();
				if (!"q".equals(answer)) {
					processEditing(answer, entity);
				}
			} while (!"q".equals(answer));
			askIfOkAndSave(entity);
		}, () -> io.printf("Sorry, %s %s is unknown%n", getEntityName(), id));
	}

	/**
	 * Ask if edit was ok, and save the domain object if needed.
	 * 
	 * @param dao    a DAO for the domain object
	 * @param entity the domain object to save
	 */
	private void askIfOkAndSave(E entity) {
		io.println("edited %s:".formatted(getEntityName()));
		io.println(entity.toString());
		String yesNo = yesNoMenu.setWelcomeText("Do you want to save your edit").choose();
		if ("y".equals(yesNo)) {
			try {
				getService().update(entity);
			} catch (ObjectNotFoundException e) {
				io.println("Object was not found. This is a bug.");
			} catch (CheckException e) {
				io.printf("%s is not correct: %s%n", getEntityName(), e.getMessage());
			}
		}
	}

	private void createEntity() {
			E entity = askForEntityData();
			io.println(entity.toString());
			String confirmation = yesNoMenu.setWelcomeText("save new %s ?".formatted(getEntityName())).choose();
			if ("y".equals(confirmation)) {				
				try {
					String id = getService().save(entity);
					io.printf("new %s saved with id: %s%n", getEntityName(), id);
				} catch (DuplicateKeyException e) {
					io.printf("Id %s is already used%n", entity.getId());
				} catch (CheckException e) {
					io.printf("Invalid data : %s%n", e.getMessage());
				}
			}
	
	}

	private void viewEntity() {
		io.println();
		final String id = io.askForString("please enter the id of the %s to display: ".formatted(getEntityName()));
			getService().findById(id).ifPresentOrElse(e -> io.println(e.toString()),
					() -> io.printf("Sorry, nothing available for id %s%n", id));		
		io.waitEnter();
	}

	private void listEntities() {
		io.println();
		List<E> list = getService().findAll();
		if (list.isEmpty()) {
			io.println("Sorry, list is empty");
		} else {
			for (E p : list) {
				io.printf("%s%n", p.shortDisplay());
			}
		}
		io.waitEnter();

	}

	/**
	 * Creates a menu for choosing fields in edition.
	 * 
	 * @return
	 */
	protected abstract Menu createEditMenu();

	/**
	 * Process the editing of the entity for a particular field
	 * <p>
	 * Should ask the value for the chosen field, and set it.
	 * <p>
	 * A connection is provided in case one wants to display other information from
	 * the database. E.g. When inputing product info, one might want to list the
	 * available categories.
	 *
	 * @param connection : a connection to the database.
	 * @param fieldCode  : the menu code for choosing the field
	 * @param entity     : the entity to edit.
	 */
	protected abstract void processEditing(String fieldCode, E entity);

	/**
	 * Ask the data for an entity.
	 * <p>
	 * A connection is provided in case one wants to display other information from
	 * the database. E.g. When inputing product info, one might want to list the
	 * available categories.
	 * 
	 * @param connection
	 * @return
	 */
	protected abstract E askForEntityData();

}
