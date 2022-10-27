# TP05 Spring

La seconde équipe de développeurs a avancé sur le projet **CustomerApplication**. En prenant les mêmes conventions que pour le catalogue, elle a aboutit à un projet fonctionnel, relié à une base de données.

Des évolutions sont cependant anticipées. L'équipe de développeur souhaite se familiariser avec le framework **Spring** et l'application **CustomerApplication**, assez simple, semble être une cible possible.

Par ailleurs, le client vient de nous demander des évolutions

- le système doit maintenant sauvegarder les données client dans une base de données ;
- le Use Case **Créer un client** est modifié : le système doit maintenant attribuer un identifiant au client.

## Expression des besoins

Globalement inchangée, sauf pour l'existance d'une base de données et :

### Créer un client

#### Résumé
Permet à Bill de saisir les coordonnées des clients. 

#### Acteurs
- Bill

#### Pré-conditions
- le serveur de bases de données est accessible et la base est en place ;
- Le client ne doit pas exister dans le système ;

#### Description

Bill entre les informations suivantes sur le client :

- First Name : prénom
- Last Name : nom de famille
- Telephone : numéro de téléphone où l'on peut joindre le client
- Street 1 et Street 2 : ces deux zones permettent de saisir l'adresse du client.
- City : ville de résidence
- State : état de résidence (uniquement pour les clients américains)
- Zipcode : code postal
- Country : pays de résidence

seuls les champs First Name et Last Name sont obligatoires 

Le système donne un identifiant unique au client, et celui-ci est sauvegardé dans la base de données.

Un message de confirmation affiche l'identifiant du client.

#### Exceptions

2. si l'un des champs est manquant, une erreur est signalée (et la saisie reprend à zéro)

#### Post-conditions
- Un client est créé.

## Architecture de l'application

En regardant notre première version, on s'aperçoit que :

- les DAO, qui sont des classes de bas niveau, sont utilisées directement par notre interface utilisateur : par exemple 
   ~~~java
   io.println();
        final String id = io.askForString("please enter the id of the " + entityName + " to delete");
        ConnectionHelper.doWithConnection(io, connection -> {
            D dao = createDAO(connection);
            Optional<E> found = dao.findById(id);
            ...
        });
        io.waitEnter();
   ~~~

- en particulier, celle-ci contient le code qui choisit l'id d'une nouvelle entrée, ce qui est une décision qui devrait revenir à la couche métier.

- on décide d'intercaler entre la couche UI et la couche DAO une nouvelle couche, la couche CustomerService, pour abstraire les décisions qu'on peut prendre. Cela va paraître un peu artificiel pour un temps, car le programme faisant à la base du CRUD, sa logique est très proche de celle de la DAO.

- on désire par ailleurs profiter des capacités de Spring Boot pour ne plus stocker en dur dans nos classe les identifiants de base de donnée. On va donc utiliser application.properties pour ce faire. 

- La création à la volée des DAO quand on en a besoin fonctionne bien, mais elle lie par ailleurs la classe utilisatrice (qui sera maintenant `CustomerService`) à l'implémentation des DAO (et à l'utilisation du JDBC, puisque le créateur du DAO doit fournir une `Connection`). On décide donc aussi d'abstraire tout cela.

  - on va abstraire les DAO derrière une interface pour permettre le test unitaire du service ;
  - les DAO seront injectées dans le service par Spring ;
  - on va créer une classe charger d'ouvrir et fermer une connexion à la demande - c'est un pis aller, on verra mieux quand on étudiera JPA et surtout quand on parlera des *transactions*.
  - on délègue la gestion des ids à une classe appropriée, `IdSequenceDAO`

L'architecture de l'application sera donc, en terme d'objets :

~~~plantuml
@startuml
hide empty members
object customerApplication
object customerUI
object simpleIO
object customerService
object customerDAO
object connectionManager

customerApplication --> customerUI
customerUI --> customerService
customerUI -> simpleIO
customerService --> customerDAO
customerDAO --> connectionManager
@enduml
~~~

Et de classes (en négligeant les classes abstraites utilisées pour factoriser le code) :
~~~plantuml
@startuml
hide empty fields
class CustomerApplication {}
package ui {
    class CustomerUI
    interface SimpleIO
    class SimpleIOImpl
}

package domain {
    interface CustomerService
    class CustomerServiceImpl
}

package persistence {
    interface DAO<T> {

    }
    interface IdSequenceDAO {
        getCurrentMaxId(tableName) : int
        setCurrentMaxId(String tableName, int newMaxId)

    }
    class CustomerDAOImpl    
    interface ConnectionManager
    class ConnectionManagerImpl
    class IdSequenceDAOImpl

}

CustomerApplication --> CustomerUI
CustomerUI --> CustomerService
CustomerService <|.. CustomerServiceImpl
SimpleIO <- CustomerUI
SimpleIO <|.. SimpleIOImpl
IdSequenceDAO <|..  IdSequenceDAOImpl

CustomerServiceImpl --> DAO
CustomerServiceImpl --> IdSequenceDAO
CustomerDAOImpl --> ConnectionManager
IdSequenceDAOImpl --> ConnectionManager

DAO <|.. CustomerDAOImpl
ConnectionManager <|.. ConnectionManagerImpl
@enduml
~~~

### Détails

#### Couche de persistance

On crée l'interface `ConnectionManager` et son implémentation.  Elles gèrent les connexions, 
en les ouvrant et les fermant au besoin. On s'aperçoit que c'est aussi le bon endroit pour déplacer le code de notre classe `ConnectionHelper` qui n'a rien à faire dans l'interface utilisateur.

Par rapport à **Spring**, le `ConnectionManagerImpl` ne devrait pas être déclaré comme un `@Component`. En effet, c'est un objet très général, qu'il faut paramétrer. En revanche, on 
aura tout intérêt à le déclarer comme `@Bean` dans un fichier de configuration - on en profitera pour y injecter les identifiants pour la connexion, récupérées dans `application.properties`.

~~~plantuml
@startuml
hide empty members
interface ConnectionManager {
    + void open()
    + void close()
    + getConnection() : Connection
    + doWithConnection(consumer : Consumer<Connection>)
    + computeWithConnection(function : Function<Connection, T>) : T
}

class ConnectionManagerImpl {
    - numberOfOpen : int
    - url : String
    - name : String
    - password : String
    + ConnectionManagerImpl(url,name, password)
}

ConnectionManager <|.. ConnectionManagerImpl
@enduml
~~~

Pour les DAOs, on aura l'architecture suivante :

~~~plantuml
@startuml
interface DAO<T> {
    + save(entity: T)
    + update(entity: T)
    + findById(id: String) : Optional<T>
    + findAll() : List<T>
    + remove(id: String)
}

abstract class AbstractDAO<T> {    
    + remove(id: String)
}

class CustomerDAOImpl    
interface ConnectionManager {
    + void open()
    + void close()
    + getConnection() : Connection
    + doWithConnection(consumer : Consumer<Connection>)
    + computeWithConnection(function : Function<Connection, T>) : T
}

DAO <|.. AbstractDAO
AbstractDAO <|-- CustomerDAOImpl 
AbstractDAO -> ConnectionManager
@enduml
~~~

- On prendra la convention que c'est **l'utilisateur de la DAO qui doit ouvrir et fermer les connexions**
- les méthodes pour gérer les identifiants, placées dans un premier temps dans la `DAO` de l'entité, sont un peu gênantes ; en effet, si pour certaines entités on préfère donner manuellement les identifiants (par exemple, pour les catégories), l'appel à ces méthodes n'aura pas lieu d'être. On décide donc de les déplacer dans leur propre classe.


## Implémentation

En pratique, on a utilisé les fonctionnalités d'**eclipse** (plus exactement de la version **spring-tool-suite** de celui-ci) pour effectuer les modifications nécessaires. VSCode a quelques fonctionnalités de *refactoring*, mais il manque par exemple *extraire interface*, qui nous sera très utile pour introduire des interfaces dans le code.


Les informations de connexion à la base sont stockées dans le fichier `application.properties`. 

La configuration **Spring** actuelle :

- la classe `CustomerApplication` est annotée avec `@SpringBootApplication`. Spring va donc automatiquement chercher des fichiers de configuration en dessous du package de cette classe ;
- certaines classes dont l'application aura besoin, comme `CustomerUI` ou `CustomerDAOImpl`, sont annotées comme des `@Component` ; elles seront trouvée quand l'injection de dépendance aura besoin d'elles ;
- dans d'autres cas, les classes des composants ne sont pas injectables directement ; c'est par exemple le cas de `SimpleIOImpl` (ou de `IdSequenceDAOImpl`) ; dans ces cas, il faut faire créer les beans en question par une classe de configuration, comme `UIConfig`.

**On vous demande** :

- d'écrire la classe `CustomerServiceImpl` ;
- de l'annoter correctement
- de compléter la classe DAOConfig ;
- de vérifier que le code tourne bien (lancer `gradle run --console=plain`)
- faire passer les tests
- ne **rien** modifier dans le package `com.yaps.common` !

Regardez bien les messsages d'erreur de Spring. Le but de l'exercice est en particulier de vous faire apprendre à lire ces messages pour corriger la configuration.


Vous pouvez jeter un œil à la classe de test `DBUnitConfig` pour avoir une idée de la manière dont on peut utiliser les données d'application.properties.



## Conseils pour compiler et lancer l'application

Pour compiler l'application, il faut que mysql tourne dans un environnement docker.


Commencez par détruire les environnements liés aux tp précédent (ou au moins, assurez-vous qu'ils ne tournent pas)
Pour cela, la commande serait :

~~~bash
docker compose -p tp5-test -f docker-compose-test-db.yml up
~~~

Le « `-p tp5-test` »  permet de nommer le conteneur. Sinon, il prend comme nom le nom du dossier du tp, et cela vous empêche d'avoir simultanément un serveur de test et un serveur de développement.

De même, avant de lancer le programme, faites 
~~~bash
docker compose -p tp5 up
~~~

Pour vous débarrasser des conteneurs :

~~~bash
docker compose -p tp5-test down
docker compose -p tp5 down
~~~

(en cas de problème, voir la documentation de docker pour supprimer des conteneurs à la main).

Si vous désirez vous passer de docker,
vous pouvez modifier le contenu des fichiers `application.properties` et `application-test.properties`.

## Remarques finale

- On aurait pu (dû) écrire des tests unitaires pour le Service, en remplaçant ses dépendances par des simulacres (« mocks »), afin de tester sans dépendre du reste de l'application ; mais comme notre but est de vous faire jouer avec l'architecture Spring, cette approche aurait conduit à vous prémâcher le travail.

- Spring dispose d'un framework spécialisé pour une utilisation simplifiée du JDBC. Quand vous aurez vu l'usage de JPA, qui est plus complexe et plus puissant, vous pourrez sans problème mettre en œuvre **Spring Data JDBC**. Nous ne l'avons pas utilisé ici, car ce qui nous intéresse c'est le mécanisme de configuration de Spring ;

- en particulier, il existe des annotations standardisées qu'on peut insérer dans le `application.properties` pour déclarer la base de données à utiliser

- Il serait intéressant de gérer les différentes versions de la base de données avec un outil comme **Flyway**. Dans la mesure où celui-ci est bien intégré à Spring JPA, nous attendrons le moment où cette partie du framework sera utilisée pour nous y mettre.

- on profite de Spring Boot pour mettre en place un système de log (actuellement utilisé seulement pour le DAO) ; l'intérêt est multiple :
  - on va pouvoir éventuellement diriger les logs où bon nous semble (par exemple vers une base de données) ;
  - on va pouvoir choisir (à partir du fichier `application.properties`) le niveau de logs souhaité pour **telle ou telle partie** du logiciel.

  En pratique on déclare dans `application.properties` :
  
~~~properties
logging.level.root=off
logging.level.com.yaps.petstore.customerApplication.dao=INFO
~~~

  Ce qui indique que le niveau de log pour **toutes** les classes de `com.yaps.petstore.customerApplication.dao` sera "info" (et off pour les autres).

  Ensuite, dans les classes pour lesquelles on désire un log, il suffit d'écrire :

  ~~~
  public class CustomerDAOImpl extends AbstractDAO<Customer> {
    // crée le logger...
    Logger logger = LogManager.getLogger(CustomerDAOImpl.class);

    public void f() {
        ...
        logger.info("appel de f...");
        ...
    }
  ~~~
  