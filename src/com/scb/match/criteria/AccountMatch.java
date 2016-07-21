package com.scb.match.criteria;

import com.scb.match.MatchType;
import com.scb.match.Matching;
import com.scb.match.model.Transaction;

import java.util.Objects;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 14/7/16.
 */
public class AccountMatch implements Matching<Transaction> {


    @Override
    public MatchType isMatch(Transaction ojb1, Transaction ojb2) {
        if (Objects.equals(ojb1, ojb2)) {
            return MatchType.STRONG;
        }

        if (ojb1 != null && ojb2 != null) {
            return ojb1.getAccountId().equals(ojb2.getAccountId()) ? MatchType.STRONG : MatchType.BREAK;
        }

        return MatchType.BREAK;
    }
}
