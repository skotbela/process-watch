package com.codecool.processwatch.os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.User;
import com.sun.jdi.connect.Connector;

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
        ArrayList<Process> processList = new ArrayList<Process>();
        ProcessHandle.allProcesses().forEach(p->{processList.add(convertProcessHandleToProcess(p));});
        return processList.stream();

    }
    public Process convertProcessHandleToProcess(ProcessHandle processHandle) {
        String name=processHandle.info().command().orElse("");
        ArrayList<String> parts=new ArrayList<String>();
        for(String n:name.split("/")){
            parts.add(n);
        }
        int index=0;
        if (parts.size()==1){parts.add("Information is not available");
        index=0;}
        index=parts.size()-1;
        String[] emptyStringArray = new String[1];
        emptyStringArray[0] = "Information is not available";
        return new Process(processHandle.pid(), processHandle.parent().hashCode(),
                new User(processHandle.info().user().orElse("Information is not available")),
                parts.get(index), processHandle.info().arguments().orElse(emptyStringArray),
                processHandle.info().startInstant().get().toString(),
                processHandle.info().totalCpuDuration().get().toString());
   }

}
