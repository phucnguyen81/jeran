package org.lou.jeran.db;

import java.sql.ResultSet;

/**
 * Converts result-set to another type.
 * 
 * @author phuc
 */
public interface RsMapper<T> {
    T map(ResultSet rs) throws Exception;
}