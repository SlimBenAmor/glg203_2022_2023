# TP 03 Annotations

Dans ce TP, nous allons essayer d'utiliser des annotations pour automatiser les mécanismes de validation des objets.
Les *use-case* restent les mêmes que dans le TP précédent.

On a appliqué un **Refactoring** au code :
comme le mécanisme de validation devient plus souple, on ne veut pas le limiter à la classe `Customer` :
on change les noms des exceptions en conséquence (`CustomerException` → `ValidationException` ; `CustomerCreateException` → `CreateException`, etc.).


## Introduction des annotations et de l'introspection

Au lieu d'écrire à la main le code de `checkData`, on se dit qu'on aimerait l'automatiser.
Pour cela :

- on va utiliser des annotations pour **marquer** les champs à valider en spécifiant les caractéristiques souhaitées pour le champ en question (par exemple `IsNotEmpty`) ;
- une classe, nommée `Validator`, sera chargée de la validation des objets. Elle utilisera l'introspection pour examiner la liste des champs disponible, et pour chaque champ annoté, elle vérifiera que la valeur du champ est valide, et lèvera une exception le cas échéant.

La méthode `checkData()` de la classe `Customer` deviendra alors :

~~~java
/**
 * This method checks the integrity of the object data.
 * 
 * @throws ValidationException if data is invalid
 */
public void checkData() throws ValidationException {
	new Validator().validateObject(this);
}
~~~

L'étape logique suivante sera sans doute d'extraire cette méthode de la classe `Customer`, soit complètement (en faisant de la validation un traitement orthogonal aux POJOs), soit en se basant sur l'héritage (en plaçant par exemple `checkData` dans une classe `ValidatedEntity` qui serait la *parente* de `Customer`)

### Note (java 8 et +)
On pourrait même utiliser une interface, depuis que les interfaces java peuvent porter des méthodes par défaut.

On définirait l'interface :

~~~java
public interface ValidatedEntity {
    default void checkData() throws ValidationException {		
		new Validator().validateObject(this);
	}
}
~~~

et on aurait simplement :

~~~java
public final class Customer implements ValidatedEntity {
...
// sans checkData...
}
~~~

## Annotation  `@IsNotEmpty`

On décide donc de créer le framework de validation, qu'on place dans le package `com.yaps.petstore.validation`. Il aurait vocation à devenir une bibliothèque extérieure, et à avoir son propre projet, puisqu'il serait générique et réutilisable. 

On commence par définir une annotation `@IsNotEmpty`, qui sera portée par les *getters* des propriétés concernées.

Vous trouverez son code dans `com.yaps.petstore.validation.annotation`.


L'annotation `@IsNotEmpty` a donc la forme :

~~~java
@Retention(RetentionPolicy.RUNTIME) // elle est utilisée à l'exécution
@Target(ElementType.METHOD) // elle marque des méthodes (en fait des getters)
public @interface IsNotEmpty {
    String message() default "empty data"; // elle porte un message, qui par défaut est "empty data"
}
~~~

On l'utilisera ainsi :

~~~java
public final class Customer  {
...
	@IsNotEmpty(message = "Invalid customer id")
	public String getId() {
		return this.id;
	}
...
}
~~~

Elle est validée par la méthode `validateObject` de la classe `com.yaps.petstore.validation.Validator` :

~~~java
 /**
   * Cette méthode valide un objet à partir de ses annotations.
   * 
   * NE PAS LA MODIFIER !
   * @param o
   * @throws ValidationException
   */
  public void validateObject(Object o) throws ValidationException {
      // On récupère la classe de l'objet pour accéder aux annotations :
      Class<? extends Object> clazz = o.getClass();
      // on parcourt les méthodes de la classe pour chercher les getters annotés
      Method[] methods = clazz.getDeclaredMethods();
      for (Method m : methods) {
          // on regarde si c'est un getter (voir la méthode dans la classe Validator)
          if (isGetter(m)) {
              // On cherche les annotations de validation...
              for (Annotation a : m.getAnnotations()) {
                  // Ce sont celles qui sont enregistrées dans la map...
                  MethodChecker processor = annotationProcessorMap.get(a.annotationType());
                  if (processor != null) {
                      // on récupère le processeur et on l'utilise pour valider la propriété.
                      Optional<ValidationErrorMessage> error = processor.checkMethod(a, m, o);
                      if (error.isPresent()) {
                          throw new ValidationException(error.get().getMessage());
                      }
                      // si ValidationException n'était pas une exception déclarée, on pourrait utiliser :
                      // processor.checkMethod(a, m, o).ifPresent(e -> {throw new
                      // ValidationException(e.getMessage())});
                  }
              }
          }
      }
  }
~~~

Le `MethodChecker` est utilisé pour valider un type d'annotation sur une propriété. C'est une interface, qui a comme code :


~~~java
public interface MethodChecker {   
    /**
     * Checks if the result of calling m on o gives a reasonable value, according to annotation a.
     * Normally, a method checker is specific to a certain kind of annotations ; the annotation 
     * is passed as a parameter to access possible annotation arguments.
     * @param a the annotation being processed
     * @param m the getter method for the property to check
     * @param o the object to check
     * @return an error message if an error has been detected.
     */
    Optional<ValidationErrorMessage> checkMethod(Annotation a, Method m, Object o);
}
~~~

La méthode `checkMethod` renvoie un `Optional`, qui est :

- soit vide s'il n'y a pas d'erreur ;
- soit un `ValidationErrorMessage` s'il y a une erreur.

Comme `MethodChecker` est une [interface fonctionnelle](https://www.baeldung.com/java-8-functional-interfaces), on peut l'implémenter à l'aide d'une *lambda expression*. C'est ce qui est fait dans le constructeur de la classe `Validator` :

~~~java
public Validator() {
	// On configure le validateur
	annotationProcessorMap.put(IsNotEmpty.class, this::checkIsNotEmpty);
}
~~~

Ici, on dit en gros : le method checker associé à `IsNotEmpty` sera implémenté en appelant la méthode `checkIsNotEmpty`
de `Validator`. On aurait aussi pu écrire :

~~~java
public Validator() {
	// On configure le validateur
	annotationProcessorMap.put(IsNotEmpty.class, (a,m,o) -> checkIsNotEmpty(a,m,o));
}
~~~

La méthode `checkIsNotEmpty` va appeler la méthode `m` sur l'objet `o` et vérifier que le résultat n'est « pas vide ».
Comme souvent dans ce genre de système, la définition de « vide » a des valeurs différentes selon le type de propriété :

- pour un objet en général, il ne peut être `null` ;
- pour une `String`, elle ne peut être vide ;
- de même pour une `Collection`.

~~~java
private Optional<ValidationErrorMessage> checkIsNotEmpty(Annotation a, Method m, Object o) {
	try {
		IsNotEmpty actualAnnotation = (IsNotEmpty) a; // On récupère l'annotation avec son vrai type
		Object res = m.invoke(o); // on appelle le getter sur o ; 
		boolean ok = true;
		if (res == null) {
			ok = false;
		} else if (res instanceof String) {
			String s = (String) res;
			ok = !s.isEmpty();
		} else if (res instanceof Collection) {
			Collection<?> col = (Collection<?>) res;
			ok = !col.isEmpty();
		}
		if (ok) {
			return Optional.empty();
		} else {
			return Optional.of(
					new ValidationErrorMessage(actualAnnotation.message()));
		}
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		throw new RuntimeException(e);
	}
}
~~~

## Question 1

En modifiant uniquement la classe `Customer.java` (et en respectant les indications qui s'y trouvent), arrangez-vous pour que *tous* les tests de `com.yaps.petstore.CustomerTest` passent (le test `testWithPhoneNotOk` devrait cependant échouer).

## Question 2

On veut maintenant ajouter une contrainte supplémentaire :
le numéro de téléphone ne peut contenir que des chiffres (c'est un peu irréaliste, mais l'idée est de rester simple ; la bonne approche serait 
d'utiliser des *expressions régulières*).

On veut définir pour cela une annotation `@ContainsOnly` qui permettrait d'écrire :

~~~java
@ContainsOnly(value = "0123456789 ", message="telephone should only contain digits")
public String getTelephone() {
    return this.telephone;
}
~~~

On décide par ailleurs que la contrainte autorise le numéro de téléphone à être vide (ce qui n'est pas forcément intuitif, mais
qui est logique : un numéro vide ne contient en effet pas de caractère autre que des chiffres, puisqu'il ne contient aucun caractère).

- Définir l'annotation correspondante ;
- décommenter la ligne qui utilise `@ContainsOnly` dans `Customer` ;
- ajouter le code nécessaire dans `Validator` pour que la contrainte soit comprise et utilisée par celui-ci.
- faites passer les tests de `CustomerTestWithPhone`.


## Épilogue

On s'aperçoit que la spécification `JSR 380` (java-validation-api) existe, et qu'il y a de nombreuses bibliothèques qui l'implémentent (par exemple `hibernate-validator`). Nous la retrouverons par la suite.

Par ailleurs, si on regarde la classe `Application`, on s'aperçoit que l'usage des exceptions ici nous a conduit à 
les utiliser plus ou moins comme des structures de contrôle. La logique de notre application est un peu bancale, toujours pour les mêmes raisons :
Un objet de la classe `Customer` peut représenter **à la fois** :

- un client en cours de saisie, avec éventuellement des erreurs ;
- un client stocké en base, qui devrait vérifier tous ses invariants.

La logique d'utilisation d'un système de typage serait de distinguer les deux :

- une classe `CustomerForm`, qui représenterait un "formulaire" d'édition de customer ;
- une classe `Customer`, dont les objets vérifient tous les invariants.

Dans ce cas, la vérification devrait se faire dans le *constructeur* de `Customer`.

