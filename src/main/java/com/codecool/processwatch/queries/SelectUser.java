package com.codecool.processwatch.queries;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.Query;


import java.util.stream.Stream;

/**
 * This is the identity query.  It selects everything from its source.
 */
public class SelectUser implements Query {
    private String keyword;
    /**
     * {@inheritDoc}
     */
    public SelectUser(String keyword){
        this.keyword=keyword;
    }
    @Override
    public Stream<Process> run(Stream<Process> input) {

        return input.filter(i->i.getUserName().equals(keyword));
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}