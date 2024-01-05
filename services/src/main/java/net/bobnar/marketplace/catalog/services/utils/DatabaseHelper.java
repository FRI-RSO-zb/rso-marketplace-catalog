package net.bobnar.marketplace.catalog.services.utils;

import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.sessions.ArrayRecord;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@RequestScoped
public class DatabaseHelper {
    @Inject
    private EntityManager em;

    public List<Object[]> executeQuery(String query) {
        Query q = em.createNativeQuery(query);
        List<Object[]> items = q.getResultList();

        return items;
    }

    public List<Map<String, ?>> getTableItems(String tableName) {
        Query query = em.createNativeQuery("SELECT * FROM " + tableName)
                .setHint(QueryHints.RESULT_TYPE, ResultType.Map);
        List<Map<String, ?>> items = query.getResultList();

        return items;
    }

    public String getTableContentInsertStatement(String tableName) {
        StringBuilder result = new StringBuilder();

        Query query = em.createNativeQuery("SELECT * FROM " + tableName)
                .setHint(QueryHints.RESULT_TYPE, ResultType.Map);

        @SuppressWarnings("unchecked")
        List<Map<String, ?>> items = query.getResultList();

        if (!items.isEmpty()) {
            result.append("INSERT INTO ")
                    .append(tableName)
                    .append("(");

            ArrayList<String> fields = new ArrayList<>();
            //noinspection unchecked
            for (DatabaseField field : (Vector<DatabaseField>)((ArrayRecord)items.get(0)).getFields()) {
                fields.add(field.getName());
            }
            result.append(String.join(", ", fields));

            result.append(") ");

            result.append("VALUES");
            ArrayList<String> allValues = new ArrayList<String>();
            for (Map<String, ?> item : items) {
                ArrayList<String> values = new ArrayList<String>();
                //noinspection unchecked
                for (DatabaseField field : (Vector<DatabaseField>)((ArrayRecord)item).getFields()) {
                    Object val = item.get(field.getName());

                    if (val instanceof String) {
                        values.add("'" + val + "'");
                    } else if (val != null) {
                        values.add(val.toString());
                    } else {
                        values.add("NULL");
                    }
                }

                String valuesString = "(" + String.join(", ", values) + ")";
                allValues.add(valuesString);
            }
            result.append(String.join(", ", allValues));


            result.append(";\n");
        } else {
            result.append("-- Table ")
                    .append(tableName)
                    .append(" is empty.\n");
        }

        return result.toString();
    }

}
