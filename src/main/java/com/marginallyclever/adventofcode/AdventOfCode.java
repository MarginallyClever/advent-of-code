package com.marginallyclever.adventofcode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AdventOfCode {
    public AdventOfCode() {
        String name = this.getClass().getSimpleName();
        processFile(name+".txt");
    }

    private void processFile(String filename) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(this.getClass().getResourceAsStream(filename))))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        postProcess();
    }

    /**
     * Process a single line from the input file.
     * @param line The line to process.
     */
    public abstract void processLine(String line);

    /**
     * Called after all lines have been processed.
     */
    public void postProcess() {}
}
