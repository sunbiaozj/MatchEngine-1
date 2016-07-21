package com.scb.match.criteria;

import com.scb.match.Matching;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 14/7/16.
 */
public enum MatchCriteria {
    ACCOUNT(new AccountMatch()), POSTING_DATE(new PostingDateMatch()), AMOUNT(new AmountMatch());

    private final Matching matching;

    MatchCriteria(Matching matching) {
        this.matching = matching;
    }

    public Matching getMatching() {
        return matching;
    }
}
