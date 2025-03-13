#!/bin/bash

# Mettre à jour les paquets
echo "Mise à jour des paquets..."
sudo yum update -y

# Installer Java 17
echo "Installation de Java 17..."
sudo yum install java-17-openjdk-devel -y

# Vérification de l'installation de Java
echo "Vérification de Java..."
java -version

# Installer Maven
echo "Installation de Maven..."
sudo yum install maven -y

# Vérification de l'installation de Maven
echo "Vérification de Maven..."
mvn -v

# Installer PostgreSQL (optionnel, peut être modifié pour MySQL/SQL Server)
echo "Installation de PostgreSQL..."
sudo yum install postgresql-server postgresql-contrib -y

# Initialiser la base de données PostgreSQL
echo "Initialisation de PostgreSQL..."
sudo postgresql-setup initdb

# Démarrer et activer le service PostgreSQL
echo "Démarrage du service PostgreSQL..."
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Vérifier l'état du service PostgreSQL
echo "Vérification de PostgreSQL..."
sudo systemctl status postgresql


echo "Installation terminée. Java, Maven et PostgreSQL sont installés."
echo "Vous pouvez maintenant passer à l'étape de compilation et d'exécution."
