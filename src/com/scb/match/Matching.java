package com.scb.match;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 14/7/16.
 */
public interface Matching<T> {

    MatchType isMatch(T ojb1, T ojb2);

}
