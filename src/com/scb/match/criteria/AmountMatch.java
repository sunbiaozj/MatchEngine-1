package com.scb.match.criteria;

import com.scb.match.MatchType;
import com.scb.match.Matching;
import com.scb.match.model.Transaction;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 14/7/16.
 */
public class AmountMatch implements Matching<Transaction> {

    @Override
    public MatchType isMatch(Transaction ojb1, Transaction ojb2) {
        if (Objects.equals(ojb1, ojb2)) {
            return MatchType.STRONG;
        }

        if (ojb1 != null && ojb2 != null) {
            if (ojb1.getAmount() - ojb2.getAmount() == 0) {
                return MatchType.STRONG;
            }

            double diff = Math.abs(ojb1.getAmount() - ojb2.getAmount());
            DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
            String str = decimalFormat.format(diff);
//            System.out.println(str);
            diff = Double.parseDouble(decimalFormat.format(diff));
//            System.out.println(diff);
            if(diff <= 0.01d) {
                return MatchType.WEAK;
            } else {
                return MatchType.BREAK;
            }
        }
        return MatchType.BREAK;
    }
}
