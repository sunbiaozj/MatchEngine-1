package com.scb.match.criteria;

import com.scb.match.MatchType;
import com.scb.match.Matching;
import com.scb.match.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 14/7/16.
 */
public class PostingDateMatch implements Matching<Transaction> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    @Override
    public MatchType isMatch(Transaction ojb1, Transaction ojb2) {
        if (Objects.equals(ojb1, ojb2)) {
            return MatchType.STRONG;
        }

        if (ojb1 != null && ojb2 != null) {
            Date postingDate1 = ojb1.getPostingDate();
            Date postingDate2 = ojb2.getPostingDate();

            long diff = Math.abs(postingDate1.getTime() - postingDate2.getTime());
            long days = diff / (1000 * 60 * 60 * 24);
            if (days == 0) {
                return MatchType.STRONG;
            } else if(days == 1) {
                return MatchType.WEAK;
            } if (days < 4) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(postingDate1);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(postingDate2);
                if ((calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) && calendar1.after(calendar2)
                        || calendar2.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && calendar2.after(calendar1)) {
                    return MatchType.WEAK;
                }
            }
        }
        return MatchType.BREAK;
    }
}
