package com.scb.match.service;

import com.scb.match.MatchType;
import com.scb.match.ReconType;
import com.scb.match.criteria.MatchCriteria;
import com.scb.match.model.BreakItem;
import com.scb.match.model.MatchItem;
import com.scb.match.model.Transaction;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatchingService {

    private static final String EMPTY = "";
    private String fileXPath = "x.txt";
    private String fileYPath = "y.txt";
    private final Map<String, BreakItem> breakItemMapX = new HashMap<>();
    private final Map<String, BreakItem> breakItemMapY = new HashMap<>();
    private final Map<String, MatchItem> weakMatchItemMap = new HashMap<>();
    private final LinkedList<MatchItem> weakMatchItemList = new LinkedList<>();
    private final Map<String, MatchItem> strongMatchItemMap = new HashMap<>();
    private final LinkedList<MatchItem> strongMatchItemList = new LinkedList<>();
    private final ArrayList<Transaction> transListX = new ArrayList<>();

    public void reconcile(ReconType reconType) {
        switch (reconType) {
            case LINE_BY_LINE:
                doLineByLine();
                break;
            case X_AS_REFERENCE:
                doWithXAsReference();
                break;
            default:
                doWithXAsReference();
                System.out.println();
                break;
        }
    }

    /**
     * Assume both files have identical number of lines. We want to match line by line.
     */
    private void doLineByLine() {
        File fileX = new File(fileXPath);
        File fileY = new File(fileYPath);
        try (FileInputStream fisX = new FileInputStream(fileX);
             BufferedReader brX = new BufferedReader(new InputStreamReader(fisX));
             FileInputStream fisY = new FileInputStream(fileY);
             BufferedReader brY = new BufferedReader(new InputStreamReader(fisY));
        ) {
            String lineX = null;
            String lineY = null;
            while ((lineX = brX.readLine()) != null && (lineY = brY.readLine()) != null) {
                Transaction transactionX = populate(lineX);
                Transaction transactionY = populate(lineY);
                List<MatchType> matchResults = new ArrayList<>();

                MatchType accountMatchType = MatchCriteria.ACCOUNT.getMatching().isMatch(transactionX, transactionY);
                matchResults.add(accountMatchType);
                if (accountMatchType != MatchType.BREAK) {
                    for (MatchCriteria matchCriteria : MatchCriteria.values()) {
                        if (matchCriteria != MatchCriteria.ACCOUNT) {
                            matchResults.add(matchCriteria.getMatching().isMatch(transactionX, transactionY));
                        }
                    }
                }

                if (isBreak(matchResults)) {
                    BreakItem breakItemX = new BreakItem(transactionX.getTransactionId());
                    BreakItem breakItemY = new BreakItem(transactionY.getTransactionId());
                    breakItemMapX.put(breakItemX.getId(), breakItemX);
                    breakItemMapY.put(breakItemY.getId(), breakItemY);
                } else {
                    MatchItem matchItem = new MatchItem(transactionX.getTransactionId(), transactionY.getTransactionId());
                    matchItem.setMatchType(isWeak(matchResults) ? MatchType.WEAK : MatchType.STRONG);
                    setMatchLevel(matchItem, transactionX, transactionY, false);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printReport();
    }

    private void doWithXAsReference() {
        File fileX = new File(fileXPath);
        File fileY = new File(fileYPath);

        try (Stream<String> stream = Files.lines(Paths.get(fileX.getPath()))) {
            stream.forEach(s -> {
                transListX.add(populate(s));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> stream = Files.lines(Paths.get(fileY.getPath()))) {
            stream.forEach(s -> {
                Transaction transactionY = populate(s);
                final MatchItem matchItem = new MatchItem();
                Optional<Transaction> optional = transListX.stream().filter(transactionX -> {
                    List<MatchType> matchResults = new ArrayList<>();

                    MatchType accountMatchType = MatchCriteria.ACCOUNT.getMatching().isMatch(transactionX, transactionY);
                    matchResults.add(accountMatchType);
                    if(strongMatchItemMap.get(transactionX.getTransactionId()) != null
                            || weakMatchItemMap.get(transactionX.getTransactionId()) != null) {
                        return false;
                    } else {
                        if (accountMatchType != MatchType.BREAK) {
                            for (MatchCriteria matchCriteria : MatchCriteria.values()) {
                                if (matchCriteria != MatchCriteria.ACCOUNT) {
                                    matchResults.add(matchCriteria.getMatching().isMatch(transactionX, transactionY));
                                }
                            }
                        }
                        if (isBreak(matchResults)) {
                            return false;
                        } else {
                            matchItem.setId1(transactionX.getTransactionId());
                            matchItem.setId2(transactionY.getTransactionId());
                            matchItem.setMatchType(isWeak(matchResults) ? MatchType.WEAK : MatchType.STRONG);
                            setMatchLevel(matchItem, transactionX, transactionY, true);
                            return true;
                        }
                    }
                }).findFirst();

                // This means the transactionY is a break
                if (!optional.isPresent()) {
                    BreakItem breakItemY = new BreakItem(transactionY.getTransactionId());
                    breakItemMapY.put(breakItemY.getId(), breakItemY);
                }
            });

            // Get all X transactions that have no match and put into BreakMap
            transListX.parallelStream().filter(transaction -> {
                if(strongMatchItemMap.get(transaction.getTransactionId()) == null
                        && weakMatchItemMap.get(transaction.getTransactionId()) == null) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList()).forEach(tXBreak -> {
                BreakItem breakItem = new BreakItem(tXBreak.getTransactionId());
                breakItemMapX.put(breakItem.getId(), breakItem);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        printReport();
    }

    private void printReport() {
        System.out.println("Report");
        System.out.println("# XY exact matches");
        strongMatchItemList.forEach((v) -> {
            System.out.println(v.getId1() + v.getId2());
        });

        System.out.println("# XY weak matches");
        weakMatchItemList.forEach((v) -> {
            System.out.print(v.getId1() + v.getId2() + ", ");
        });
        System.out.println();
        System.out.println("# X breaks");
        breakItemMapX.forEach((k, v) -> {
            System.out.print(k + ", ");
        });

        System.out.println();
        System.out.println("# Y breaks");
        breakItemMapY.forEach((k, v) -> {
            System.out.print(k + ", ");
        });
    }

    private void setMatchLevel(MatchItem matchItem, Transaction transactionX, Transaction transactionY, boolean leftIdOnly) {
        if (matchItem.getMatchType() == MatchType.STRONG) {
            strongMatchItemMap.put(constructKey(transactionX, transactionY, leftIdOnly), matchItem);
            strongMatchItemList.add(matchItem);
        } else {
            weakMatchItemMap.put(constructKey(transactionX, transactionY, leftIdOnly), matchItem);
            weakMatchItemList.add(matchItem);
        }
    }

    private String constructKey(Transaction transactionX, Transaction transactionY, boolean leftIdOnly) {
        return transactionX.getTransactionId() + (leftIdOnly ? EMPTY : transactionY.getTransactionId());
    }

    private String constructKey(Transaction transaction) {
        return transaction.getTransactionId();
    }

    private boolean isWeak(List<MatchType> matchResults) {
        return matchResults.stream().filter(matchType -> matchType == MatchType.WEAK).findFirst().isPresent();
    }

    private boolean isBreak(List<MatchType> matchResults) {
        return matchResults.stream().filter((m) -> m == MatchType.BREAK).findFirst().isPresent();
    }

    /**
     * Assume input has correct format.
     *
     * @param line
     * @return
     */
    private Transaction populate(String line) {
        String[] inputs = line.split(";");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            return new Transaction(inputs[0].trim(),
                    inputs[1].trim(),
                    dateFormat.parse(inputs[2].trim()),
                    Double.parseDouble(inputs[3].trim()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, BreakItem> getBreakItemMapX() {
        return breakItemMapX;
    }

    public Map<String, BreakItem> getBreakItemMapY() {
        return breakItemMapY;
    }

    public Map<String, MatchItem> getWeakMatchItemMap() {
        return weakMatchItemMap;
    }

    public Map<String, MatchItem> getStrongMatchItemMap() {
        return strongMatchItemMap;
    }

    public String getFileXPath() {
        return fileXPath;
    }

    public void setFileXPath(String fileXPath) {
        this.fileXPath = fileXPath;
    }

    public String getFileYPath() {
        return fileYPath;
    }

    public void setFileYPath(String fileYPath) {
        this.fileYPath = fileYPath;
    }

    public LinkedList<MatchItem> getWeakMatchItemList() {
        return weakMatchItemList;
    }

    public LinkedList<MatchItem> getStrongMatchItemList() {
        return strongMatchItemList;
    }

    public ArrayList<Transaction> getTransListX() {
        return transListX;
    }
}
