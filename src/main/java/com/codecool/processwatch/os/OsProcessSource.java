package com.codecool.processwatch.os;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.User;

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
//        ProcessHandle.allProcesses().forEach(p->{System.out.println(p.info().arguments());});
//        return Stream.of(new Process(1,  1, new User("root"), "init", new String[0]),
//                         new Process(42, 1, new User("Codecooler"), "processWatch", new String[] {"--cool=code", "-v"}));
//         ProcessHandle.allProcesses().forEach(p->{Process process = new Process(p.pid(), p.parent().hashCode(), new User(p.info().user().get()), p.info().command().get(), new String[0]);});
        ArrayList<Process> processList = new ArrayList<Process>();
        ProcessHandle.allProcesses().forEach(p->{processList.add(convertProcessHandleToProcess(p));});
        System.out.println(processList);
        return processList.stream();
//        return Stream.of(processList);
    }
    public Process convertProcessHandleToProcess(ProcessHandle processHandle) {
//        name converting
        return new Process(processHandle.pid(), processHandle.parent().hashCode(), new User(processHandle.info().user().orElse("")), processHandle.info().command().orElse(""), new String[0]);
   }

}
