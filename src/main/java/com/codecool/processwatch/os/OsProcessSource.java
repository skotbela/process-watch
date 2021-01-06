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
//        ProcessHandle.allProcesses().forEach(p->{System.out.println(p.info().arguments().map(Object::toString));});
//        ProcessHandle.allProcesses().forEach(p->{System.out.println(p.info().arguments());});
//        return Stream.of(new Process(1,  1, new User("root"), "init", new String[0]),
//                         new Process(42, 1, new User("Codecooler"), "processWatch", new String[] {"--cool=code", "-v"}));
//         ProcessHandle.allProcesses().forEach(p->{Process process = new Process(p.pid(), p.parent().hashCode(), new User(p.info().user().get()), p.info().command().get(), new String[0]);});
        ArrayList<Process> processList = new ArrayList<Process>();
        ProcessHandle.allProcesses().forEach(p->{processList.add(convertProcessHandleToProcess(p));});
        //System.out.println(processList);
        return processList.stream();

    }
    public Process convertProcessHandleToProcess(ProcessHandle processHandle) {
//        name converting
        String name=processHandle.info().command().orElse("");
        ArrayList<String> parts=new ArrayList<String>();
        for(String n:name.split("/")){
            parts.add(n);
        }
        int index=0;
//        System.out.println(parts.size());
        if (parts.size()==1){parts.add("Information is not available");
        index=0;}
        index=parts.size()-1;
//        System.out.println(parts.get(index));
        String[] emptyStringArray = new String[1];
        emptyStringArray[0] = "Information is not available";
        //System.out.println(processHandle.info().command().orElse(""));
//        System.out.println(processHandle.info().arguments().toString());
//        System.out.println(processHandle.info().startInstant().toString());
//        System.out.println(new Process(processHandle.pid(), processHandle.parent().hashCode(), new User(processHandle.info().user().orElse("Information is not available")), parts.get(index), processHandle.info().arguments().orElse(emptyStringArray), processHandle.info().startInstant().toString()));
        return new Process(processHandle.pid(), processHandle.parent().hashCode(), new User(processHandle.info().user().orElse("Information is not available")), parts.get(index), processHandle.info().arguments().orElse(emptyStringArray), processHandle.info().startInstant().get().toString(), processHandle.info().totalCpuDuration().get().toString());
   }

}
