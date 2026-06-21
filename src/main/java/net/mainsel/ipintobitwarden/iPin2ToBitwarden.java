package net.mainsel.ipintobitwarden;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.mainsel.ipintobitwarden.objects.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class iPin2ToBitwarden {
    private final LogHandler logHandler;

    public iPin2ToBitwarden() {
        this.logHandler = new LogHandler();
    }

    public void handelConvert() {
        var csvPath = askCsvFilePath();
        var iPinEntries = loadIPinCsv(csvPath);
        var bitwardenEntries = convertIPinToBitwardenJson(iPinEntries);
        exportBitwardenJson(csvPath, bitwardenEntries);
    }

    private String askCsvFilePath() {
        var jFileChooser = new JFileChooser();
        var fileNameExtensionFilter = new FileNameExtensionFilter("CSV-Datei", "csv");
        jFileChooser.setDialogTitle("iPin CSV auswählen");
        jFileChooser.setFileFilter(fileNameExtensionFilter);
        jFileChooser.setMultiSelectionEnabled(false);
        var fileChooserResult = jFileChooser.showOpenDialog(null);

        if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
            var selectedFile = jFileChooser.getSelectedFile();
            logHandler.logInfo("Datei wurde gefunden und wird nun eingelesen...");
            return selectedFile.getPath();
        }

        logHandler.logError("Es wurde keine CSV-Datei ausgewählt");
        throw new RuntimeException("Es wurde keine CSV-Datei ausgewählt");
    }

    private List<IPinEntry> loadIPinCsv(String csvFilePath) {
        List<IPinEntry> result = new ArrayList<>();
        File csvFile = new File(csvFilePath);

        try {
            var csvLines = Files.readAllLines(csvFile.toPath());

            if (csvLines.getFirst().equals("Group,Entry Name,Field Name,Field Value,Field Type,Note,isSecure,isLink")) {
                csvLines.removeFirst();
            }

            StringBuilder currentValue = null;
            IPinCsvColumns currentIPinColumn = IPinCsvColumns.Group;
            IPinEntry currentIPinEntry = null;
            var valueCouldEnd = false;
            var nextIsStart = true;

            for (var csvLine : csvLines) {
                if (nextIsStart || currentIPinColumn == IPinCsvColumns.isLink) {
                    if (currentIPinEntry != null) {
                        result.add(currentIPinEntry);
                        currentIPinEntry = null;
                        currentIPinColumn = IPinCsvColumns.Group;
                    }
                } else {
                    if (currentValue == null) {
                        currentValue = new StringBuilder();
                    }

                    currentValue.append("\n");
                }

                for (var lineChar : csvLine.toCharArray()) {
                    if (lineChar == '"') {
                        // "
                        if (nextIsStart) {
                            nextIsStart = false;
                        } else {
                            valueCouldEnd = true;
                        }
                    } else if (lineChar == ',') {
                        // ,
                        if (valueCouldEnd) {
                            nextIsStart = true;
                            valueCouldEnd = false;

                            if (currentIPinEntry == null) {
                                currentIPinEntry = new IPinEntry();
                            }

                            switch (currentIPinColumn) {
                                case Group -> currentIPinEntry.setGroup(currentValue != null ? currentValue.toString() : null);
                                case EntryName -> currentIPinEntry.setEntry(currentValue != null ? currentValue.toString() : null);
                                case FieldName -> currentIPinEntry.setFieldName(currentValue != null ? currentValue.toString() : null);
                                case FieldValue -> currentIPinEntry.setFieldValue(currentValue != null ? currentValue.toString() : null);
                                case Note -> currentIPinEntry.setNote(currentValue != null ? currentValue.toString() : null);
                                case isSecure -> currentIPinEntry.setSecure(currentValue != null && Boolean.parseBoolean(currentValue.toString()));
                                case isLink -> currentIPinEntry.setLink(currentValue != null && Boolean.parseBoolean(currentValue.toString()));
                                case FieldType -> {
                                }
                            }

                            currentIPinColumn = currentIPinColumn.next();
                            currentValue = null;
                        } else {
                            if (nextIsStart) {
                                currentIPinColumn = currentIPinColumn.next();
                            } else {
                                if (currentValue == null) {
                                    currentValue = new StringBuilder();
                                }

                                currentValue.append(',');
                            }
                        }
                    } else {
                        if (currentValue == null) {
                            currentValue = new StringBuilder();
                        }

                        if (valueCouldEnd) {
                            currentValue.append('"');
                            valueCouldEnd = false;
                        }

                        currentValue.append(lineChar);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private List<BitwardenEntry> convertIPinToBitwardenJson(List<IPinEntry> iPinEntryList) {
        logHandler.logInfo("Es wurden " + (long) iPinEntryList.size() + " iPin Zeilen aus der CSV-Datei geladen.");

        var bitwardenEntryList = new ArrayList<BitwardenEntry>();
        var groupedEntries = iPinEntryList.stream().collect(Collectors.groupingBy(IPinEntry::getEntry));

        logHandler.logInfo("Es wurden insgesamt " + groupedEntries.size() + " iPin Einträge gefunden.");
        logHandler.logInfo("Die Konvertierung wird nun gestartet...");

        groupedEntries.forEach((groupedEntryName, entries) -> {
            var bitwardenEntry = new BitwardenEntry(1, groupedEntryName, entries.getFirst().getNote());

            var usernameEntry = getIPinEntryFromListByFieldName("Benutzername", entries);
            var passwortEntry = getIPinEntryFromListByFieldName("Passwort", entries);
            var urisEntries = getAllIPinUrlEntries(entries);

            var bitwardenEntryLogin = new BitwardenEntryLogin(urisEntries.stream().map(IPinEntry::getFieldValue).toList(), usernameEntry != null ? usernameEntry.getFieldValue() : null, passwortEntry != null ? passwortEntry.getFieldValue() : null);
            bitwardenEntry.setLogin(bitwardenEntryLogin);

            var customIPinFields = getAllCustomIPinEntryFields(entries);
            customIPinFields.forEach(customIPinField -> {
                if (customIPinField.getFieldValue() != null) {
                    var bitwardenField = new BitwardenField(customIPinField.getFieldName(), customIPinField.getFieldValue(), customIPinField.isSecure() ? 2 : 1);
                    bitwardenEntry.addField(bitwardenField);
                }
            });

            bitwardenEntryList.add(bitwardenEntry);
        });

        logHandler.logInfo("Die Konvertierung zu Bitwarden wurde beendet.");

        return bitwardenEntryList;
    }

    private void exportBitwardenJson(String csvFilePath, List<BitwardenEntry> bitwardenEntryList) {
        logHandler.logInfo("Der Export der JSON-Datei wird gestartet...");

        var bitwardenJsonPath = Paths.get(new File(csvFilePath).getParent(), "BitwardenJsonConverted.json");
        var bitwardenJson = new BitwardenJson(bitwardenEntryList);
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(bitwardenJsonPath.toString()), bitwardenJson);

            logHandler.logInfo("Der Export der JSON-Datei wurde beendet.");
            logHandler.logInfo("Die Datei liegt nun unter folgendem Pfad " + bitwardenJsonPath);
        } catch (IOException e) {
            logHandler.logError(e.toString());
            throw new RuntimeException(e);
        }
    }

    private IPinEntry getIPinEntryFromListByFieldName(String fieldName, List<IPinEntry> iPinEntryList) {
        return iPinEntryList.stream()
                .filter(x -> x.getFieldName() != null && x.getFieldName().equals(fieldName))
                .findFirst()
                .orElse(null);
    }

    private List<IPinEntry> getAllIPinUrlEntries(List<IPinEntry> iPinEntryList) {
        return iPinEntryList.stream()
                .filter(x -> x.isLink() && x.getFieldName() != null && x.getFieldName().equals("URL"))
                .toList();
    }

    private List<IPinEntry> getAllCustomIPinEntryFields(List<IPinEntry> iPinEntryList) {
        var defaultFieldNames = Arrays.asList("Benutzername", "Passwort");

        return iPinEntryList.stream()
                .filter(x -> !x.isLink() && x.getFieldName() != null && !x.getFieldName().isEmpty() && !defaultFieldNames.contains(x.getFieldName()))
                .toList();
    }
}
