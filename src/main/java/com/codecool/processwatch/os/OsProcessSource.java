package com.codecool.processwatch.os;

import java.util.Optional;
import java.util.stream.Stream;

import com.codecool.processwatch.domain.*;
import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.gui.ProcessView;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.namespace.QName;

/**
 * A process source using the Java {@code ProcessHandle} API to retrieve information
 * about the current processes.
 */
public class OsProcessSource implements ProcessSource {
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Process> getProcesses() {

        ProcessHandle.allProcesses().forEach(p->{ System.out.println(new User(p.info().user().get())+" "+ p.info().arguments()+" "+p.info().commandLine());});



        return Stream.of(new Process(1,  1, new User("root"), "init", new String[0]),
                         new Process(42, 1, new User("Codecooler"), "processWatch", new String[] {"--cool=code", "-v"}));

//return ProcessHandle.allProcesses().forEach(p->{Process process = new Process(p.pid(), p.parent().hashCode(), new User(p.info().user().get()),new Optional());});
    return ProcessHandle.allProcesses()
    }
}
