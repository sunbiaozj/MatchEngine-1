package com.scb.match.model;

import com.scb.match.MatchType;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 14/7/16.
 */
public class MatchItem {

    private String id1;
    private String id2;
    private MatchType matchType;

    public MatchItem(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public MatchItem() {

    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MatchItem{");
        sb.append(id1).append(id2).append(' ');
        sb.append(matchType);
        sb.append("}");
        return sb.toString();
    }
}