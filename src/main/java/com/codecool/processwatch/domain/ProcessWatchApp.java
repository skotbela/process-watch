package com.codecool.processwatch.domain;

/**
 * An application that can display the processes running on the system.
 * <p>
 * It will only display data that can be accessed from this process.
 * <p>
 * A query can be applied to filter the displayed processes.
 */
public class ProcessWatchApp {
    protected static ProcessSource processSource;
    protected static ProcessDisplay processDisplay;
    protected static Query actualQuery;

    /**
     * Create an instance of the application.
     *
     * @param processSource  an object that can read the system processes.
     * @param processDisplay an object that can display the processes.
     * @param startingQuery  a query to apply for filtering the displayed processes.
     */
    public ProcessWatchApp(ProcessSource processSource,
                           ProcessDisplay processDisplay,
                           Query startingQuery) {
        this.processSource = processSource;
        this.processDisplay = processDisplay;
        this.actualQuery = startingQuery;
        refresh();
    }



    /**
     * Refresh the display by reading the current processes from the source.
     */
    public static void refresh() {
        var in = processSource.getProcesses();
        var out = actualQuery.run(in);
        processDisplay.display(out);
    }

    /**
     * Set a new query to filter processes.
     *
     * @param query a {@code Query} to use for process filtering.
     */
    public void setQuery(Query query) {
        this.actualQuery = query;
        refresh();
    }
    public void fresh(){
        refresh();
    }


}
