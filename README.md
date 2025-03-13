
# Importation de données Excel dans une base de données relationnelle

## Description

Cette application permet d'importer des données depuis un fichier Excel (`.xlsx`) vers une base de données SQL. Elle est compatible avec PostgreSQL, MySQL, et SQL Server.

## Prérequis

Avant d'utiliser l'application, assurez-vous d'avoir installé les prérequis suivants :

- Java 17 ou supérieur
- Maven installé pour la compilation du projet
- Une base de données SQL (PostgreSQL, MySQL, ou SQL Server)
- Un fichier Excel (`.xlsx`) à importer
- Les informations de connexion à la base de données

### Scripts d'automatisation

Pour installer automatiquement les prérequis sous linux, vous pouvez lancer les **scripts prerequis_deb_ubuntu.sh** (systèmes debian/ubuntu) ou **prerequis_rehl.sh** (systèmes Red Hat)

## Compilation du projet

Si nécessaire, compilez le projet avec Maven ou Gradle.

### Avec Maven :

```bash
mvn clean package
```

### Avec Gradle :

```bash
gradle build
```

## Utilisation

### Format de l'entrée

L'application attend que vous lui fournissiez une entrée au format suivant :

```
DB_URL|DB_NAME|DB_USER|DB_PASSWORD|TABLE_NAME|FILE_PATH
```

- **DB_URL** : L'URL de la base de données, exemple : `jdbc:postgresql://127.0.0.1:5432/`
- **DB_NAME** : Le nom de la base de données
- **DB_USER** : L'utilisateur de la base de données
- **DB_PASSWORD** : Le mot de passe de l'utilisateur
- **TABLE_NAME** : Le nom de la table dans laquelle les données seront insérées
- **FILE_PATH** : Le chemin vers le fichier Excel à importer

### Exemple d'entrée :

```
jdbc:postgresql://127.0.0.1:5432/|mydb|postgres|password|users|C:\path\to\your\file.xlsx
```

### Lancer l'application

Sous **Windows** ou **Linux**, vous pouvez exécuter le programme en utilisant la commande suivante.

### Sous Windows :

1. Ouvrez une invite de commande.
2. Accédez au répertoire contenant le fichier JAR (répertoire target par défaut).
3. Exécutez la commande suivante :

```bash
echo "jdbc:postgresql://127.0.0.1:5432/|mydb|postgres|password|users|C:\path\to\your\file.xlsx" | java -jar importexcel-1.0.jar
```

### Ou plus simplement
```bash
java -jar importexcel-1.0.jar
```
### Et passer les données ensuite
```bash
jdbc:postgresql://127.0.0.1:5432/|asin_test|postgres|root|people|D:\ALT_WORK\PROJET_ASIN\people sample.xlsx 
```

### Explication de la commande

- La commande **echo** envoie les informations de connexion à la base de données et le chemin du fichier Excel à l'application.
- `java -jar importexcel-1.0.jar` exécute le fichier JAR compilé de l'application.
- Ou plus simplement `java -jar importexcel-1.0.jar`une fois lancé, attends en paramètre les données **pipe-separated**

### Configuration de la base de données

L'application suppose que la table dans la base de données existe déjà. La table doit avoir les colonnes suivantes :

- **matricule** : Le matricule de la personne (type `VARCHAR` ou `TEXT`)
- **nom** : Le nom de la personne (type `VARCHAR` ou `TEXT`)
- **prenom** : Le prénom de la personne (type `VARCHAR` ou `TEXT`)
- **datedenaissance** : La date de naissance de la personne (type `DATE`)
- **status** : Le statut de la personne (type `VARCHAR` ou `TEXT`)

Voici un exemple de création de table PostgreSQL :

```sql
CREATE TABLE users (
    matricule VARCHAR(50),
    nom VARCHAR(100),
    prenom VARCHAR(100),
    datedenaissance DATE,
    status VARCHAR(50)
);
```

### Logs et Messages

- L'application affiche des messages dans la console pour indiquer l'état de l'importation (nombre de lignes insérées, durée de l'exécution, etc.).
- En cas d'erreur, des messages détaillés sont affichés pour faciliter le diagnostic.

### Exemple de sortie de la console

```
Veuillez entrer les informations suivantes séparées par un pipe(|) :
DB_URL|DB_NAME|DB_USER|DB_PASSWORD|TABLE_NAME|FILE_PATH
Fichier à importer: C:\path\to\your\file.xlsx
Base de données:[
URL: jdbc:postgresql://127.0.0.1:5432/
NOM: mydb
USER: postgres
]
Début de l'importation des données...
Import terminé avec succès !
Nombre de lignes insérées : 100
Temps d'exécution : 1500 ms, 1 sec, 0.0 min
```

## Erreurs courantes

- **Erreur de format de date** : Si le fichier Excel contient des dates avec un format inconnu, l'application lève une exception.
#### Formats de date pris en compte:

```
yyyy-MM-dd
MM/dd/yyyy
MM-dd-yyyy
dd/MM/yyyy
dd-MM-yyyy
yyyy/MM/dd
```
- **Erreur de connexion à la base de données** : Vérifiez que les informations de connexion sont correctes et que la base de données est accessible.
- **Erreur de lecture du fichier** : Vérifiez que le chemin de votre fichier est correct; il ne doit inclure les caractères **" ou '**

## Capture d'écran:

Les captures de résultats se trouvent dans le dossier screenshoots

