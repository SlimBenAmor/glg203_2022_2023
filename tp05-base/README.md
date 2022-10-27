# TP05 Spring

La seconde équipe de développeurs a avancé sur le projet **CustomerApplication**. En prenant les mêmes conventions que pour le catalogue, elle a aboutit à un projet fonctionnel, relié à une base de données.

Des évolutions sont cependant anticipées. L'équipe de développeur souhaite se familiariser avec le framework **Spring** et l'application **CustomerApplication**, assez simple, semble être une cible possible.

Par ailleurs, le client vient de nous demander des évolutions

- le système doit maintenant sauvegarder les données client dans une base de données ;
- le Use Case **Créer un client** est modifié : le système doit maintenant attribuer un identifiant au client.

Le projet `tp05-base` correspond à une version n'utilisant pas `Spring`. Tout l'enjeu du projet de cette semaine sera de compléter le projet `tp05-spring`.

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

#### Exceptions

2. si l'un des champs est manquant, une erreur est signalée (et la saisie reprend à zéro)

#### Post-conditions
- Un client est créé.


## Conseils pour compiler et lancer l'application

Pour compiler l'application, il faut que mysql tourne dans un environnement docker.


Commencez par détruire les environnements liés aux tp précédent (ou au moins, assurez-vous qu'ils ne tournent pas)
Pour cela, la commande serait :

~~~bash
docker compose -p tp5-test -f docker-compose-test-db.yml up
~~~

Le « `-p tp-test4` »  permet de nommer le conteneur. Sinon, il prend comme nom le nom du dossier du tp, et cela vous empêche d'avoir simultanément un serveur de test et un serveur de développement.

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

Si vous désirez vous passer de docker, vous pouvez modifier les ports dans `PetstoreConnectionFactory` (ça n'est pas grave, car nous utiliserons la classe d'origine pour nos tests).
