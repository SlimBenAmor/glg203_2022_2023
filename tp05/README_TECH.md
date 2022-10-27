# Notes techniques sur ce TP

Pour l'écriture des tests, un problème se pose si on utilise @SpringBootTest : l'application est créée, et **elle est lancée**.
Dans le cas d'une application web, c'est ce qu'on voudra effectivement faire pour des tests d'intégration. 
Mais pas dans notre cas, quand nous voulons simplement tester les autres couches de l'application.

On trouvera une documentation sur ce genre de problèmes ici : https://www.baeldung.com/spring-junit-prevent-runner-beans-testing-execution.

La solution que nous avons choisie est de configurer explicitement tous les objets que nous voulons utiliser.

