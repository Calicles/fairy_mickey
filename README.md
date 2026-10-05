# Fairy Mickey

Petit jeu d'exploration en Java Swing : Mickey traverse des décors et ramasse des clés.

## Prérequis

- **JDK 8 ou plus récent** (testé avec Java 21) — `java -version` pour vérifier.
- **Maven 3.6+** — `mvn -v` pour vérifier. Sans Maven, voir « Avec IntelliJ » plus bas.

## Lancer le jeu

```bash
git clone https://github.com/Calicles/fairy_mickey.git   # ou : git pull origin main
cd fairy_mickey
mvn package
java -jar target/fairy_mickey.jar
```

`mvn package` compile, lance les tests unitaires et produit `target/fairy_mickey.jar`,
un jar autonome (environ 87 Mo à cause des musiques) qui contient tout ce qu'il faut.

Pour reconstruire sans relancer les tests : `mvn package -DskipTests`.
Pour lancer uniquement les tests : `mvn test`.

### Avec IntelliJ

1. *File > Open* sur le dossier du projet, puis accepter l'import Maven
   (ou clic droit sur `pom.xml` > *Maven > Reload project*).
2. Lancer `com.antoine.Main` (flèche verte dans `src/main/java/com/antoine/Main.java`).

## Commandes

| Touche | Action |
|---|---|
| Flèches | Déplacer Mickey |
| Échap | Quitter |

Dans les décors « en ligne » (1D), seules deux directions sont actives
(gauche/droite ou haut/bas selon le décor).

## Checklist de test

Corrections à vérifier après la dernière série de changements :

- [ ] **Fenêtre** : elle tient entièrement dans l'écran (le zoom s'adapte ; il vaut au plus 3).
- [ ] **Clavier** : une touche autre qu'une flèche (lettre, espace, Maj) ne fait pas bouger Mickey.
- [ ] **Clavier** : maintenir ← puis appuyer sur ↑ et relâcher ↑ : Mickey reprend vers la gauche
      (dans un décor libre, par exemple le décor de la forêt à trois chemins).
- [ ] **Animation** : la marche est fluide et utilise les 5 images de chaque direction.
- [ ] **Suivi de ligne** : dans le premier décor, Mickey rejoint le chemin sans trembler.
- [ ] **Entrées des décors** : en revenant dans un décor à plusieurs sorties, Mickey apparaît
      près de la sortie par laquelle il revient, tourné vers l'intérieur.
      Cas typique : décor 1 → 3 → (sortie de gauche) 4 → 2 : il doit arriver **à gauche** du décor 2.
- [ ] **Clés** : toucher une clé la fait disparaître du décor et allume sa case dans l'inventaire
      (argent dans le décor du bout de chemin à droite, bronze dans l'intérieur de la maison).
- [ ] **Musique** : le bruitage du menu puis la musique s'enchaînent, la piste suivante démarre
      à la fin de la première, le son n'est pas saturé.
- [ ] **Sans carte son** (casque débranché, etc.) : le jeu démarre quand même, en silence,
      avec un message « son désactivé » dans la console.

## Limites connues

- Les clés ramassées ne sont pas encore enregistrées sur le joueur (seul l'inventaire les affiche).
- La clé d'or n'est placée dans aucun décor et il n'y a pas encore de fin de partie.
- Le format MP3 n'est pas lu par Java Sound : seules les musiques `.wav` de `musics.json` sont jouées.
