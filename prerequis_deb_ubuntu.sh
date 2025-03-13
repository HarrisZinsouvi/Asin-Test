#!/bin/bash

# Mettre à jour les paquets
echo "Mise à jour des paquets..."
sudo apt update -y

# Installer Java 17
echo "Installation de Java 17..."
sudo apt install openjdk-17-jdk -y

# Vérification de l'installation de Java
echo "Vérification de Java..."
java -version

# Installer Maven
echo "Installation de Maven..."
sudo apt install maven -y

# Vérification de l'installation de Maven
echo "Vérification de Maven..."
mvn -v

# Installer PostgreSQL (optionnel, peut être modifié pour MySQL/SQL Server)
echo "Installation de PostgreSQL..."
sudo apt install postgresql postgresql-contrib -y

# Démarrer le service PostgreSQL
echo "Démarrage du service PostgreSQL..."
sudo service postgresql start

# Si vous utilisez une autre base de données, assurez-vous de configurer les clients appropriés

echo "Installation terminée. Java, Maven et PostgreSQL sont installés."
echo "Vous pouvez maintenant passer à l'étape de compilation et d'exécution."
