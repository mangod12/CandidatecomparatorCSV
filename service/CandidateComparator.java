package service;

import model.Candidate;
import java.util.*;
import java.io.*;

public class CandidateComparator {
    public static void compareAndWriteDifferences(
        Map<String, Candidate> master,
        Map<String, Candidate> changes,
        String outputPath,
        String logPath
    ) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        BufferedWriter logWriter = new BufferedWriter(new FileWriter(logPath));

        int totalChanges = 0;
        Map<String, Integer> fieldChangeCount = new HashMap<>();
        fieldChangeCount.put("Name", 0);

        for (String id : changes.keySet()) {
            Candidate newC = changes.get(id);
            Candidate oldC = master.get(id);
            if (oldC == null) {
                writer.write("NEW ENTRY: " + newC.name + " (" + id + ")\n");
            } else if (!newC.equals(oldC)) {
                writer.write("UPDATED: " + id + "\n");
                logWriter.write("UPDATED: " + id + "\n");
                if (!oldC.name.equalsIgnoreCase(newC.name)) {
                    writer.write(" - Name: " + oldC.name + " -> " + newC.name + "\n");
                    logWriter.write(" - Name changed\n");
                    fieldChangeCount.put("Name", fieldChangeCount.get("Name") + 1);
                }
                totalChanges++;
            }
        }

        logWriter.write("\nSummary of Changes:\n");
        logWriter.write("Total Changes: " + totalChanges + "\n");
        for (Map.Entry<String, Integer> entry : fieldChangeCount.entrySet()) {
            logWriter.write(entry.getKey() + " Changes: " + entry.getValue() + "\n");
        }
        writer.close();
        logWriter.close();
    }

    // Returns a list of differences for table display: [Candidate ID, Field, Master Value, Changes Value]
    public static java.util.List<String[]> getDifferencesList(
            Map<String, Candidate> master,
            Map<String, Candidate> changes) {
        java.util.List<String[]> diffs = new java.util.ArrayList<>();
        for (String id : changes.keySet()) {
            Candidate newC = changes.get(id);
            Candidate oldC = master.get(id);
            if (oldC == null) {
                // New entry
                diffs.add(new String[]{id, "NEW ENTRY", "", newC.name});
            } else if (!newC.equals(oldC)) {
                if (!oldC.name.equalsIgnoreCase(newC.name)) {
                    diffs.add(new String[]{id, "Name", oldC.name, newC.name});
                }
                // Do NOT add yyyymm/date changes to the GUI table
            }
        }
        return diffs;
    }
}
