package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImportXlsxToDB {
    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    );



    public static void main(String[] args) {
        try {
            // Lire l'entrée depuis stdin
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Informations à fournir séparées par un pipe(|) :");
            System.out.println("DB_URL|DB_NAME|DB_USER|DB_PASSWORD|TABLE_NAME|FILE_PATH");
            String input = reader.readLine().trim();
            String[] params = input.split("\\|");

            if (params.length < 6) {
                System.err.println("Erreur: vous devez fournir tous les arguments requis!");
                System.err.println("Syntaxe: DB_URL DB_NAME DB_USER DB_PASSWORD TABLE_NAME FILE_PATH");
                return;
            }

            String dbUrl = params[0].trim();
            String dbName = params[1].trim();
            String dbUser = params[2].trim();
            String dbPassword = params[3].trim();
            String tableName = params[4].trim();
            String filePath = params[5].trim();
            System.out.println("Fichier à importer: "+ filePath);
            System.out.println("Base de données:[");
            System.out.println("URL: "+dbUrl);
            System.out.println("NOM: "+dbName);
            System.out.println("USER: "+dbUser);
            System.out.println("]");


            importData(dbUrl, dbName, dbUser, dbPassword, tableName, filePath);

        } catch (IOException e) {
            System.err.println("Erreur de lecture depuis stdin: " + e.getMessage());
        }
    }

    public static void importData(String dbUrl, String dbName, String dbUser, String dbPassword, String tableName, String filePath) {
        long startTime = System.currentTimeMillis();
        int rowCount = 0;

        System.out.println("Début de l'importation des données...");

        try (Connection conn = DriverManager.getConnection(dbUrl + dbName, dbUser, dbPassword);
             Workbook workbook = new XSSFWorkbook(new File(filePath))) {

            conn.setAutoCommit(false);
            String insertSQL = "INSERT INTO %s (matricule, nom, prenom, datedenaissance, status) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(String.format(insertSQL, tableName))) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Ignorer l'en-tête

                    pstmt.setString(1, getCellValueAsString(row.getCell(0)));
                    pstmt.setString(2, getCellValueAsString(row.getCell(1)));
                    pstmt.setString(3, getCellValueAsString(row.getCell(2)));
                    pstmt.setDate(4, parseDate(getCellValueAsString(row.getCell(3))));
                    pstmt.setString(5, getCellValueAsString(row.getCell(4)));

                    pstmt.addBatch();
                    rowCount++;
                }

                pstmt.executeBatch();
                conn.commit();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Import terminé avec succès !");
            System.out.println("Nombre de lignes insérées : " + rowCount);
            long duration=(endTime - startTime);
            long seconds=(TimeUnit.MILLISECONDS.toSeconds(duration));
            long minutes=TimeUnit.MILLISECONDS.toMinutes(duration);
            String mins=(minutes==0)?"0."+seconds:""+minutes;
            System.out.println("Temps d'exécution : " + duration + " ms, "+seconds+" sec, "+mins+" mins");

        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation des données: " + e.getMessage());
        }
    }

    public static java.sql.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                return java.sql.Date.valueOf(date);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new IllegalArgumentException("Format de date inconnu : " + dateStr);
    }

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return formatter.format(cell.getLocalDateTimeCellValue().toLocalDate());
                }
                return String.valueOf(cell.getNumericCellValue());

            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());

            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }

            case BLANK:
                return "";

            default:
                return "Valeur inconnue";
        }
    }
}
