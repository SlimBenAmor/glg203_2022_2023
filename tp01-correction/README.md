# TP 01 : prise en main

Ce TP vous permet de prendre en main le système de correction de vos futurs TP. On vous demande d'écrire la classe Customer.java qui satisfait les tests unitaires qui vous sont fournis.
La classe Customer représente un client; elle possède les attributs suivants, tous de type String : 

~~~java
private String id;
private String firstname;
private String lastname;
private String telephone; 
private String street1; 
private String street2; 
private String city; 
private String state; 
private String zipcode; 
private String country; 
private String mail;
~~~

Nous avons aussi fourni une classe `CustomerRepository` qui permet de stocker des objets `Customer` identifiés par leur ID.

~~~plantuml
@startuml
class CustomerRepository {
    find(id) : Customer
    remove(id) : boolean
    insert(customer) : boolean
}
CustomerRepository o-- "*" Customer
@enduml
~~~

Les méthodes `remove` et `insert` renvoient un booléen qui indique si l'opération souhaitée est réussie.

Dans ce TP, les tests vont servir de spécification. Il faudra donc les lire attentivement. **VOUS NE DEVEZ EN AUCUN CAS LES MODIFIER** (mais vous pouvez ajouter d'autres classes de test pour vous).

1. Récupérez l'archive git des tps ;
2. ouvrer le projet `tp01` dans votre IDE favori
3. Développez la classe `Customer` en entier et faites fonctionner les test unitaires (`./gradlew test`)
4. Développez la classe `CustomerRepository` en entier (et faites fonctionner les test unitaires (`./gradlew test`))
5. faites un `commit` et un `push` pour déposer votre projet dans l'archive `git` qui vous a été attribuée ; vérifiez sur le site web que l'archive en ligne est bien synchronisée avec votre code actuel.

Pour développer les classes :

- créez les deux classes (vides) ;
- commencez par copier les champs de `Customer` ci-dessous dans la classe ;
- faites générer les *getters* correspondant par votre IDE ; pour les *setters*, réfléchissez un peu : l'un d'entre eux n'est pas forcément nécessaire ;
- regardez de quels constructeurs vous avez besoin (par rapport aux tests) ; faites-les générer par votre IDE ;
- ajoutez les méthodes nécessaires pour que le programme compile ;
- vous pouvez maintenant entreprendre d'utiliser les tests pour valider votre code.
