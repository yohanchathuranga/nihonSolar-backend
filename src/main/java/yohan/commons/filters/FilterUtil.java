package yohan.commons.filters;

import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.InvalidInputException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilterUtil {

    private static String generatePropertyClause(DOPropertyFilter pFilter) throws CustomException {
        try {
            String pClause = pFilter.getProperty();
            switch (pFilter.getOperator()) {
                case EQUAL:
                    pClause += " = '" + pFilter.getValue() + "' ";
                    break;
                case NOT_EQUAL:
                    pClause += " <> '" + pFilter.getValue() + "' ";
                    break;
                case LESS_THAN:
                    pClause += " < " + pFilter.getValue() + " ";
                    break;
                case GREATER_THAN:
                    pClause += " > " + pFilter.getValue() + " ";
                    break;
                case LIKE:
                    if (!(pFilter.getValue() == null || pFilter.getValue().toString().isEmpty())) {
                        //  pClause += " like '%" + pFilter.getValue() + "%' ";
                        pClause = " lower(" + pClause + ") like LOWER('%" + pFilter.getValue() + "%') ";
                    } else {
                        pClause = null;
                    }
                    break;
                case NOT_LIKE:
                    if (!(pFilter.getValue() == null || pFilter.getValue().toString().isEmpty())) {
                        //  pClause += " like '%" + pFilter.getValue() + "%' ";
                        pClause = " lower(" + pClause + ") not like LOWER('%" + pFilter.getValue() + "%') ";
                    } else {
                        pClause = null;
                    }
                    break;
            }
            return pClause;

        } catch (Exception ex) {
            throw new InvalidInputException("Generating whare clause failed");
        }
    }

    public static String generateWhereClause(ArrayList<DOPropertyFilter> filterData) throws CustomException {
        try {
            StringBuffer buff = new StringBuffer();

            String temp;
            boolean grouped;
            DOPropertyFilter f, g;
            FilterGroupingOperator go;

            ArrayList<DOPropertyFilter> usedFilters = new ArrayList<>();

            for (int i = 0; i < filterData.size(); i++) {
                f = filterData.get(i);
                temp = generatePropertyClause(f);
                if (temp == null) {
                    continue;
                }

                grouped = false;
                if (usedFilters.contains(f)) {
                    continue;
                }

                //check grouped with this property
                if (f.getGroup() != null && !f.getGroup().isEmpty()) {
                    //check with group name
                    for (int j = i + 1; j < filterData.size(); j++) {
                        g = filterData.get(j);

                        if (f.getGroup().equals(g.getGroup())) {
                            //grouped
                            grouped = true;

                            go = g.getGroupingOperator();
                            if (go == null) {
                                go = FilterGroupingOperator.AND;
                            }

                            usedFilters.add(g);

                            switch (go) {
                                case AND:
                                    temp += " AND " + generatePropertyClause(g);
                                    break;

                                case OR:
                                    temp += " OR " + generatePropertyClause(g);
                                    break;
                            }

                        }

                    }

                } else {
                    //check with field name
                    for (int j = i + 1; j < filterData.size(); j++) {
                        g = filterData.get(j);

                        if (f.getProperty() != null && f.getProperty().equals(g.getGroupProperty())) {
                            //grouped
                            grouped = true;

                            go = g.getGroupingOperator();
                            if (go == null) {
                                go = FilterGroupingOperator.AND;
                            }

                            usedFilters.add(g);

                            switch (go) {
                                case AND:
                                    temp += " AND " + generatePropertyClause(g);
                                    break;

                                case OR:
                                    temp += " OR " + generatePropertyClause(g);
                                    break;
                            }

                        }
                    }
                }//end else

                if (grouped) {
                    temp = "(" + temp + ") ";
                }

                if (temp != null && !temp.isEmpty()) {
                    if (f.getGroupingOperator() != null) {
                        switch (f.getGroupingOperator()) {
                            case AND:
                                buff.append(" AND ");
                                break;

                            case OR:
                                buff.append(" OR ");
                                break;
                        }

                    } else {
                        //buff.append(" AND ");
                    }

                    buff.append(temp);
                }
                usedFilters.add(f);
            }

            return buff.toString();
        } catch (Exception ex) {
            throw new InvalidInputException("Generating whare clause failed");
        }
    }

    public static void main(String[] args) {

        try {
            ArrayList<DOPropertyFilter> filterData = new ArrayList<>();

            DOPropertyFilter f1 = new DOPropertyFilter();
            f1.setProperty("name");
            f1.setValue("nandana");
            f1.setOperator(FilterOperator.LIKE);

            filterData.add(f1);

            DOPropertyFilter f2 = new DOPropertyFilter();
            f2.setProperty("ip");
            f2.setValue("127.0.0.1");
            f2.setOperator(FilterOperator.EQUAL);
            filterData.add(f2);

            DOPropertyFilter f3 = new DOPropertyFilter();
            f3.setProperty("ip");
            f3.setValue("127.0.0.2");
            f3.setOperator(FilterOperator.EQUAL);
            f3.setGroupProperty("ip");
            f3.setGroupingOperator(FilterGroupingOperator.OR);

            filterData.add(f3);

            DOPropertyFilter f4 = new DOPropertyFilter();
            f4.setProperty("ip");
            f4.setValue("127.0.0.3");
            f4.setOperator(FilterOperator.EQUAL);
            f4.setGroupProperty("ip");
            f4.setGroupingOperator(FilterGroupingOperator.OR);

            filterData.add(f4);

            String w = FilterUtil.generateWhereClause(filterData);
            System.out.println(">>" + w);
        } catch (CustomException ex) {
            Logger.getLogger(FilterUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
