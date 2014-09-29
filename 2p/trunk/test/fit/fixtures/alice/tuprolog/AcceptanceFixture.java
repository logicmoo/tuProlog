package alice.tuprolog;

import fit.ColumnFixture;
import fit.Counts;
import fit.Fixture;
import fit.Parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AcceptanceFixture extends ColumnFixture {

	public String section;

    protected String input;
    protected Parse tables;
    protected Fixture fixture;
    protected Counts runCounts = new Counts();
    protected String footnote = null;

    @SuppressWarnings("unchecked")
	protected void run() throws Exception {
        input = read(new File(file(section)));
        fixture = new Fixture();
        tables = new Parse(input, new String[] {"table", "tr", "td"});
        fixture.doTables(tables);
        runCounts.tally(fixture.counts);
        summary.put("counts run", runCounts);
    }

    protected String file(String title) {
		String words = title.substring(title.indexOf(" ")).toLowerCase();
		return "test/fit/doc/" + Fixture.camel(words) + ".html";
	}

    public int right() throws Exception {
        run();
        return fixture.counts.right;
    }

    public int wrong() {
        return fixture.counts.wrong;
    }

    public int ignores() {
        return fixture.counts.ignores;
    }

    public int exceptions() {
        return fixture.counts.exceptions;
    }

    protected String read(File input) {
		footnote = null; // reset footnotes for the new file
        char chars[] = new char[(int)(input.length())];
        try {
            FileReader in = new FileReader(input);
            in.read(chars);
            in.close();
            return new String(chars);
        } catch (IOException e) {
        	e.printStackTrace();
            return "";
        }
    }


    // Footnote /////////////////////////////////

    Parse fileCell;

    public void doRow(Parse row) {
        fileCell = row.leaf();
        super.doRow(row);
    }

    public void wrong(Parse cell) {
        super.wrong(cell);
        if (footnote == null) {
            //footnote = tables.footnote();
            footnote = footnote(tables, section);
            fileCell.addToBody(footnote);
            printDetailedInfo(section, fixture.counts);
        }
    }
    
    @Override
    public void right(Parse cell) 
    {
    	super.right(cell);
    	if (footnote == null) {
            //footnote = tables.footnote();
            footnote = footnote(tables, section);
            fileCell.addToBody(footnote);
            printDetailedInfo(section, fixture.counts);
        }
    }
    
    private void printDetailedInfo(String testName, Counts counts)
    {
    	String name = testName.substring(testName.indexOf(" ")).trim();
    	String line1 = name + " results:";
    	String line2 = counts.right + " right, " + counts.wrong + " wrong, " + counts.ignores + " ignored, " + counts.exceptions + " exceptions"; 
    	System.out.println(line1);
    	System.out.println("\t"+line2);
    }

    public static int footnoteFiles = 0;
    private static String reportDir = "build/reports/fit/";

    public String footnote(Parse tables, String testName) {
    	
        if (footnoteFiles >= 25) {
            return "[-]";
        } else {
			int thisFootnote = ++footnoteFiles;
			String footnoteDir = "details/";
            String html = reportDir + footnoteDir + testName + ".html";
            try {
				File dir = new File(reportDir + footnoteDir);
				dir.mkdir();
                File file = new File(html);
                file.delete();
                PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                tables.print(output);
                output.close();
                return " <a href=\"" + footnoteDir + testName + ".html" + "\">[" + thisFootnote + "]</a>";
            } catch (IOException e) {
				System.out.println(e.getMessage());
                return "[!]";
            }
        }
    }

}